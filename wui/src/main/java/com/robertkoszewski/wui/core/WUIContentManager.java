/**************************************************************************\
 * Copyright (c) 2018 Robert Koszewski                                    *
 *                                                                        *
 * Permission is hereby granted, free of charge, to any person obtaining  *
 * a copy of this software and associated documentation files (the        *
 * "Software"), to deal in the Software without restriction, including    *
 * without limitation the rights to use, copy, modify, merge, publish,    *
 * distribute, sublicense, and/or sell copies of the Software, and to     *
 * permit persons to whom the Software is furnished to do so, subject to  *
 * the following conditions:                                              *
 *                                                                        *
 * The above copyright notice and this permission notice shall be         *
 * included in all copies or substantial portions of the Software.        *
 *                                                                        *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,        *
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF     *
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND                  *
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE *
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION *
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION  *
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.        *
\**************************************************************************/

package com.robertkoszewski.wui.core;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.server.*;
import com.robertkoszewski.wui.server.response.*;
import com.robertkoszewski.wui.template.BaseContent;
import com.robertkoszewski.wui.template.ElementTemplate;
import com.robertkoszewski.wui.template.WindowTemplate;
import com.robertkoszewski.wui.ui.element.Node;
import com.robertkoszewski.wui.ui.element.Parent;

import net.sf.image4j.codec.ico.ICOEncoder;

/**
 * WUI Content Manager
 * @author Robert Koszewski
 */
public class WUIContentManager implements ContentManager {

	// Static Variables
	private static final int MAX_ICON_SIZE_PX = 64;
	
	// Logger
	protected Logger log = LoggerFactory.getLogger(WUIContentManager.class);
	
	// Variables
	private final WindowTemplate template;
	private final ResourceManager resourceManager;
	private final SessionManager sessionManager;

	// Cache and Resources
	private Map<String, View> views = new HashMap<String, View>();
	private URL icon = null; // Define a more flexible Icon class to allow to define different icon sizes.
	
	// TODO: Temporary Variables. Move them where corresponds
	// private Map<String, BaseElement> element_cache = new HashMap<String, BaseElement>();
	private Map<String, ElementTemplate> element_definition_cache = new HashMap<String, ElementTemplate>();
	
	// Constructor
	
	public WUIContentManager(WindowTemplate template) {
		this.template = template;
		this.resourceManager = new WUIResourceManager();
		this.sessionManager = new SessionManager(this, template);
	}
	
	@Override
	public void addView(String url, View view) {
		views.put(url, view);
	}
	
	@Override
	public View getView(String url) {
		return views.get(url);
	}

	@Override
	public void removeView(String url) {
		views.remove(url);
	}
	
	// Response Manager Methods
	/**
	 * Generate Response
	 */
	@Override
	public Response getResponse(Request request) {
		return getResponse(request, null);
	}
	
	/**
	 * Generate Response
	 * @param request
	 * @param metadata
	 * @return
	 */
	public Response getResponse(Request request, RequestMetadata metadata) {

		// Response
		Response response = null;
		Map<String, Cookie> cookies = new HashMap<String, Cookie>();

		// Get Session
		Session session = sessionManager.getSession(request, cookies);

		// Is Content Request?
		RequestType requestType = null;
		String wuiRequestHeader = request.getHeader("x-wui-request");
		
		// Decode Request Type
		if(wuiRequestHeader != null) {
			try {
				requestType = RequestType.valueOf(wuiRequestHeader.toUpperCase());
			}catch(Exception e) {
				requestType = RequestType.UNKNOWN;
			}
		}

		// Debug
		log.debug("SERVING CONTENT: " + request.getURL() + " (REQUEST TYPE: " + requestType + ")");

		if(requestType != null) { // WUI Request

			ViewInstance view;
			
			switch (requestType) {
			case EVENT: // Action Performed Request Type
				response =  triggerEvent(session.getViewInstance(request.getURL()), request);
				break;

			case CONTENT: // Content Request Type
				// Serve View
				view = session.getViewInstance(request.getURL());

				long timestamp = 0;
				try {
					timestamp = Long.parseUnsignedLong(request.getFirstParameter("t"));
				}catch(Exception e) { /* Ignore */ }
				
				response = toContentResponse(view, timestamp, metadata);
				break;
				
			case ELEMENT: // Element Definition Request Type
				// Serve View
				response = getElementDefinition(request);
				break;
				
			case UNKNOWN: // Unknown Request Type
				response = new WUIStringResponse("text/html", "ERROR: Unkown Action");
				break;
				
			default: // Unimplemented Request Type
				response = new WUIStringResponse("text/html", "ERROR: Action Not Implemented");
			}

		} else { // Regular Request	

			String staticRequestType = request.getFirstParameter("requestType");
			
			if(staticRequestType != null) {
				switch(staticRequestType) {
				// Streamed Element
				case "streamedElement": 
					response = getStreamedElementData(request, session);
					break;
				// CSS Dependency	
				case "cssDependency":
					response = getCSSDependency(request, session);
					break;
				// JS Dependency
				case "jsDependency": 
					response = getJSDependency(request, session);
					break;
				}
				
				
			}else {
				
				// STATIC REQUEST
				String url = request.getURL();
			
				// Predefined URLs
				switch(url) {
				
					// SERVE: Application Icon
					case "/favicon.ico":
						try {
							response = new WUIFileResponse("image/x-icon", imageToICO(icon));
						} catch (Exception e) {}
						break;
						
					case "/favicon.png":
						try {
							response = new WUIFileResponse("image/png", imageToFormat(icon, "png"));
						} catch (Exception e) {}
						break;
						
					default: // Everything Else
						
						// Show Current Template or Resource
						ViewInstance view = session.getViewInstance(request.getURL()); // TODO: Inline this if nowhere else required
						
						if(view != null) {
							response = new WUIStringResponse("text/html", template.getTemplateHTML(resourceManager));
							
						}else {
							
							// Serve Resource
							InputStream resource = resourceManager.getResource(url);
	
							if(resource != null) {
								if(log.isDebugEnabled()) log.debug("SERVING RESOURCE: "+request.getURL());
								response = new WUIFileResponse("text/javascript", resource);
							}
						}
				}
			}
			
			// No Response Content (ERROR 404)
			if(response == null) {
				response = new WUIStringResponse("text/html","ERROR: Resource not found");
			}
		}
		
		// Send Cookies and any aggregated data
		response.getCookies().putAll(cookies); // Set Cookies

		// Return Response
		return response;
	}

	/**
	 * Get Web Socket Response
	 */
	@Override
	public RequestResponse getWebSocketResponse(String message) {
		System.out.println("PAYLOAD: " + message);
		WSRequest request = new Gson().fromJson(message, WSRequest.class);
		return getWebSocketResponse(request);
	}
	
	/**
	 * Get Web Socket Response
	 */
	@Override
	public RequestResponse getWebSocketResponse(Request request) {
		RequestMetadata metadata = new RequestMetadata();
		Response response = getResponse(request, metadata);
		return new RequestResponse(){

			@Override
			public Request getRequest() {
				WSRequest wsrequest;
				
				if(request instanceof WSRequest) {
					wsrequest = (WSRequest) request;
				}else {
					wsrequest = new WSRequest(request);
					wsrequest.cloneRequest(request);
				}
				
				// Update Timestamp
				wsrequest.setParameter("t", metadata.latestTimestamp + "");
				
				return wsrequest;
			}

			@Override
			public Response getResponse() {
				return response;
			}

			@Override
			public boolean isContentRequest() {
				return metadata.isContentRequest;
			}
			
		};
	}

	/**
	 * Set Application Icon
	 */
	@Override
	public void setIcon(URL resource) {
		icon = resource;
	}
	
	/**
	 * Perform Action
	 * @param viewInstance 
	 * @param request
	 * @return
	 */
	private Response triggerEvent(ViewInstance viewInstance, Request request) {
		String elmentUUID = request.getHeader("x-wui-element");
		String eventID = request.getHeader("x-wui-event");
		
		if(log.isDebugEnabled()) log.debug("TRIGGERED EVENT ON ELEMENT " + elmentUUID);	

		viewInstance.triggerEventOnElement(elmentUUID, eventID, request.getPostBody()); // TODO: Pass data
		
		return new WUIStringResponse("text/html", "EVENT TRIGGERED");
	}
	
	/**
	 * Element Definition Response
	 * @param request
	 * @return
	 */
	private Response getElementDefinition(Request request) {
		// TODO: Implement multiple element retrieval during one request
		System.out.println("PARAMETERS!!: " + request.getParameters().toString());
		
		List<String> elementID = request.getParameter("element");
		
		if(elementID.size() != 0) {
			
			System.out.println("ELEMENT: " + elementID.get(0) + " - " + element_definition_cache.get(elementID.get(0)).getElementDefinition());
			
			return new WUIStringResponse("text/json", element_definition_cache.get(elementID.get(0)).getElementDefinition());
			//return new WUIJsonResponse(element_definition_cache.get(elementID.get(0)));
		}
		
		return new WUIJsonResponse(null); // TODO: Return error.
		
	}
	
	/**
	 * Get Streamed Element
	 * @param request
	 * @param session
	 * @return
	 */
	private Response getStreamedElementData(Request request, Session session) {
		ViewInstance view = session.getViewInstance(request.getURL());
		
		String elementID = request.getFirstParameter("element");
		
		if(elementID == null) {
			return new WUIJsonResponse(null); // TODO: Return error.
		}
		
		Node element = view.getElement(elementID);
		
		if(element == null) {
			return new WUIJsonResponse(null); // TODO: Return error.
		}

		try {
			return new WUIFileResponse(element.getStreamedResourceMimeType(), element.getStreamedResource());
		} catch (IOException e) {
			return new WUIJsonResponse(null); // TODO: Return error. 404 not found.
		}
	}

	/**
	 * Content Response Generator
	 * @param controller
	 * @return
	 */
	private Response toContentResponse(ViewInstance view, long remote_timestamp, RequestMetadata metadata) {
		// Template Node
		//Node root = new Node();
		//root.element = 
		//root.nodes = controller.viewUpdate().getContent().toArray();
			
		// Return Error if controller is null
		if(view == null) {
			return new WUIStringResponse("text/html", "Content Not Found");
		}

		// Freeze request till data changes
		if(remote_timestamp != 0) {
			try {
				view.waitForViewChange();
			} catch (InterruptedException e) {
				return null; // Abort any action when interrupted
			}
		}
		
		// Build Node Tree
		BaseContent<?, ?, ?> content = view.getContent(); // TODO: Improve this.
		
		// Build Content Response
		ContentResponse content_response = new ContentResponse();
		content_response.title = content.getTitle();
		
		// Concurrency Fix
		ArrayList<Node> lockedElements = new ArrayList<Node>();
		
		// Process Nodes
		BuilderData bdata = new BuilderData();
		bdata.remote_timestamp = remote_timestamp;
		buildResponseTree(content.getChildren(), bdata, lockedElements);

		// Check Content Data

		if(remote_timestamp < content.getDataTimestamp()) {
			// Store New Updated Data
			content_response.data = content.getDataObject();
		}

		long content_nesting_timestamp = content.getNestingTimestamp();
		if(content_nesting_timestamp > remote_timestamp) { 
			bdata.timestamp = content_nesting_timestamp;
			Map<String, UUID[]> root_child_nodes = new HashMap<String, UUID[]>();

			Iterator<Entry<String, List<Node>>> cit = content.getChildren().entrySet().iterator();
			while(cit.hasNext()) {
				Entry<String, List<Node>> branch = cit.next();
				List<Node> bnodes = branch.getValue();
				UUID[] child_node_uids = new UUID[bnodes.size()];
				Iterator<Node> bnit = bnodes.iterator();
				int i = 0;
				while(bnit.hasNext()) {
					child_node_uids[i++] = bnit.next().getUUID();
				}

				root_child_nodes.put(branch.getKey(), child_node_uids); // Create Child Node List
			}
			
			content_response.root = root_child_nodes;
		} 

		// Set Content Response
		content_response.timestamp = bdata.timestamp;
		content_response.nodes = bdata.nodes; // Nodes

		// Update Metadata
		if(metadata != null) {
			metadata.latestTimestamp = bdata.timestamp;
			metadata.isContentRequest = true;
		}

		// CONCURRENCY: Unlock Locked Elements
		Iterator<Node> lit = lockedElements.iterator();
		while(lit.hasNext()) lit.next().endRead();
		
		// Return Response
		return new WUIStringResponse("text/json", (new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()).toJson(content_response));
	}
	
	
	
	/**
	 * Content Response Class
	 */
	private class BuilderData {
		public long remote_timestamp = 0;
		public long timestamp = 0;
		// public boolean nesting_changed = false;
		public Map<UUID, NodeObject> nodes = new HashMap<UUID, NodeObject>(); // Element Updates
	}

	
	/**
	 * Build Response Tree
	 * @param parent
	 * @param current
	 * @param bdata
	 * @param lockedElements 
	 * @return
	 */
	private void buildResponseTree(Map<String, List<Node>> current, BuilderData bdata, ArrayList<Node> lockedElements) {
		
		// Nodes Object
		Map<String, NodeObject[]> nodes = new HashMap<String, NodeObject[]>();
		
		// Iterate trough all Child Nodes
		Iterator<Entry<String, List<Node>>> bit = current.entrySet().iterator(); 
		while(bit.hasNext()) {
			// Branch Iteration
			Entry<String, List<Node>> branch = bit.next();
			List<Node> elements = branch.getValue();
			NodeObject[] root = new NodeObject[elements.size()];
			
			// Build Node Array
			int i = 0;
			for(Node e : branch.getValue()) {
				// CONCURRENCY: Lock Node to prevent Writing
				e.startRead();
				lockedElements.add(e);

				// Full Node
				NodeObject n = new NodeObject();
				n.timestamp = e.getElementTimestamp();
				n.element = e.getElementName();
				n.uuid = e.getUUID();
				
				// Store Last Timestamp
				if(n.timestamp > bdata.timestamp) bdata.timestamp = n.timestamp; // Element Timestamp

				// Register Update
				boolean update_added = false;
				
				// Element Data Changed
				if(bdata.remote_timestamp < e.getElementDataTimestamp()) {
					// Store New Updated Data
					n.data = e.getElementData();
					bdata.nodes.put(n.uuid, n); // Add Updated Element
					update_added = true;
				}
				
				// Element Children Changed
				if(e instanceof Parent) {
					Parent parent = (Parent) e;
					
					Map<String, List<Node>> children = parent.getChildren();

					if(bdata.remote_timestamp < parent.getNestingTimestamp()) {
						// Store New Updated Data
						System.out.println("NODE UPDATE HAPPEND");
						
						Map<String, UUID[]> childlist = new HashMap<String, UUID[]>();
						Iterator<Entry<String, List<Node>>> cit = children.entrySet().iterator();
						while(cit.hasNext()) {
							Entry<String, List<Node>> cen = cit.next();
							List<Node> cel = cen.getValue();
							UUID[] chelements = new UUID[cel.size()];
							int nindex = 0;
							Iterator<Node> cnit = cel.iterator();
							while(cnit.hasNext())
								chelements[nindex++] = cnit.next().getUUID();
							childlist.put(cen.getKey(), chelements);
						}
						n.children = childlist;
						
						if(!update_added) bdata.nodes.put(n.uuid, n); // Add Updated Element
						// bdata.nesting_changed = true;
					}
					
					buildResponseTree(children, bdata, lockedElements); // Build Child Tree
				}
				
				// Element Changed
				if(update_added == false && bdata.remote_timestamp < e.getElementTimestamp()) {
					// Store New Updated Data
					bdata.nodes.put(n.uuid, n); // Add Updated Element
					//update_added = true;
				}

				root[i++] = n;

				// Add Element Definition
				// TODO: Rewrite (Add Element Definition Cache to Window?)
				if(!element_definition_cache.containsKey(e.getElementName())) {
					element_definition_cache.put(e.getElementName(), e.getElementDefinition()); 
				}
				
			}
			
			// Add Nodes to Branch
			nodes.put(branch.getKey(), root);
		}
	}
	
	// Helper Methods
	
	/**
	 * Convert Icon to ICO
	 * @param resource
	 * @return
	 * @throws Exception 
	 */
	private InputStream imageToICO(URL resource) throws Exception {
		if(resource == null) throw new Exception("No Icon Defined");
		BufferedImage bi = ImageIO.read(resource); // Read Image File
		
		// Rescale when necessary
		if(bi.getWidth() > MAX_ICON_SIZE_PX || bi.getHeight() > MAX_ICON_SIZE_PX)
			bi = Scalr.resize(bi, MAX_ICON_SIZE_PX); // Resize Icon to 64x64
		
		// Encode Icon
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ICOEncoder.write(bi, os); // Encode to ICO
		return new ByteArrayInputStream(os.toByteArray());
	}
	
	/**
	 * Get Image to Image Format
	 * @param resource
	 * @param targetFormat
	 * @return
	 * @throws Exception
	 */
	private InputStream imageToFormat(URL resource, String targetFormat) throws Exception {
		if(resource == null) throw new Exception("No Icon Defined");
		
		// Check if file is already in the targetFormat
		targetFormat = targetFormat.toLowerCase();
		if(FilenameUtils.getExtension(resource.toURI().getPath()).toLowerCase().equals(targetFormat)) {
			// System.out.println("FORMAT IS CORRECT. NOT CONVERTING.");
			return resource.openStream();
		}
		
		// System.out.println("CONVERTING IMAGE");
		// Convert Image to proper format
		BufferedImage bi = ImageIO.read(resource);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bi, targetFormat, os);
		return new ByteArrayInputStream(os.toByteArray());
	}
	
	// Simple CSS Dependency Manager (No Versioning)
	private HashMap<String, CSSDependency> cssDependencies = new HashMap<String, CSSDependency>();
	private HashMap<String, JSDependency> jsDependencies = new HashMap<String, JSDependency>();

	/**
	 * Add Web Dependency
	 */
	@Override
	public void addDependency(WebDependency dependency) {
		if(dependency instanceof CSSDependency) {
			cssDependencies.put(dependency.name, (CSSDependency) dependency);
		}else 
		if(dependency instanceof JSDependency) {
			jsDependencies.put(dependency.name, (JSDependency) dependency);
		}else {
			log.error("ERROR: Unknown web dependency type: ", dependency);
		}
		
	}
	
	/**
	 * Get JavaScript Dependency
	 * @param request
	 * @param session
	 * @return
	 */
	private Response getJSDependency(Request request, Session session) {
		System.out.println("REQUETSTING JS DEPENDENCY: " + request.getFirstParameter("name") + " / " + jsDependencies.size());
		String name = request.getFirstParameter("name");
		// String version = request.getFirstParameter("version");
		
		if(name != null && jsDependencies.containsKey(name)) {
			JSDependency dep = jsDependencies.get(name);
			try {
				return new WUIFileResponse("text/javascript", dep.file.openStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new WUIStringResponse("text/html","ERROR: Resource not found");
	}

	/**
	 * Get CSS Dependency
	 * @param request
	 * @param session
	 * @return
	 */
	private Response getCSSDependency(Request request, Session session) {
		System.out.println("REQUETSTING CSS DEPENDENCY: " + request.getFirstParameter("name") + " / " + cssDependencies.size());
		String name = request.getFirstParameter("name");
		// String version = request.getFirstParameter("version");
		
		if(name != null && cssDependencies.containsKey(name)) {
			CSSDependency dep = cssDependencies.get(name);
			try {
				return new WUIFileResponse("text/css", dep.file.openStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return new WUIStringResponse("text/html","ERROR: Resource not found");
	}
}

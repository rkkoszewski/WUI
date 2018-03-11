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
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.server.*;
import com.robertkoszewski.wui.server.response.*;
import com.robertkoszewski.wui.template.BaseContent;
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.template.ElementTemplate;
import com.robertkoszewski.wui.template.WindowTemplate;
import com.robertkoszewski.wui.ui.element.Node;
import com.robertkoszewski.wui.ui.element.Parent;
import com.robertkoszewski.wui.ui.element.type.NodeData;

import net.sf.image4j.codec.ico.ICOEncoder;

/**
 * WUI Content Manager
 * @author Robert Koszewski
 */
public class WUIContentManager implements ContentManager {

	// Static Variables
	private static final int MAX_ICON_SIZE_PX = 64;
	
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
		System.out.println("SERVING CONTENT: " + request.getURL() + " (REQUEST TYPE: " + requestType + ")");

		if(requestType != null) { // WUI Request

			ViewInstance view;
			
			switch (requestType) {
			case ACTION: // Action Performed Request Type
				response =  performAction(session.getViewInstance(request.getURL()), request);
				break;

			case CONTENT: // Content Request Type
				// Serve View
				view = session.getViewInstance(request.getURL());
				
				//WUIController controller = pages.get(request.getURL()); // TODO: Inline this if nowhere else required
				
				long timestamp = 0;
				try {
					timestamp = Long.parseUnsignedLong(request.getParameter("t").get(0));
				}catch(Exception e) {}
				
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
							try {
								System.out.println("SERVING RESOURCE: "+request.getURL());
								response = new WUIFileResponse("text/javascript", resource);
								
							} catch (FileNotFoundException e) {
								// Ignore Error
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
	private Response performAction(ViewInstance viewInstance, Request request) {
		
		String elmentUUID = request.getHeader("x-wui-element");
		
		System.out.println("ACTION PERFORMED ON ELEMENT " + elmentUUID);
		
		viewInstance.performActionOnElement(elmentUUID);
		
		/*
		BaseElement element = element_cache.get(elmentUUID);
		
		if(element != null) {
			if(element instanceof ActionableElement) {
				// Perform Action
				((ActionableElement) element).actionPerformed();
			}
		}
		*/
		
		return new WUIStringResponse("text/html", "ACTION PERFORMED");
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
			return new WUIJsonResponse(element_definition_cache.get(elementID.get(0)));
		}
		
		return new WUIJsonResponse(null); // TODO: Return error.
		
	}
	
	int l = 0;

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
		
		BuilderData bdata = new BuilderData();
		bdata.remote_timestamp = remote_timestamp;
		Map<String, NodeObject[]> nodes = buildResponseTree(content.getChildren(), bdata);

		// Check Content Data
		/*
		if(remote_timestamp < content.getDataTimestamp()) {
			// Store New Updated Data
			updates.add(n);
		}
		*/
		long content_nesting_timestamp = content.getNestingTimestamp();
		if(content_nesting_timestamp > remote_timestamp) { 
			bdata.timestamp = content_nesting_timestamp;
			System.out.println("NODE UPDATE HAPPEND");
			bdata.nesting_changed = true;
		} 
		
		// Build Content Response
		ContentResponse content_response = new ContentResponse();
		content_response.title = content.getTitle();
		content_response.timestamp = bdata.timestamp;
		
		if(remote_timestamp == 0 || bdata.nesting_changed) {
			content_response.type = UpdateStrategy.FULL;
			content_response.nodes = nodes; // Full Content Update
		}else {
			// Partial Data Change Response
			content_response.type = UpdateStrategy.PARTIAL;
			content_response.updates = bdata.updates; // TODO: If updates.size == 0 then do EMPTY RESPONSE (Should not happen though, as we're locking the thread while no updates occur)
		}
		
		// Update Metadata
		if(metadata != null) {
			metadata.latestTimestamp = bdata.timestamp;
			metadata.isContentRequest = true;
		}

		// Return Response
		return new WUIStringResponse("text/json", (new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()).toJson(content_response));
	}
	
	
	
	/**
	 * Content Response Class
	 */
	private class BuilderData {
		public long remote_timestamp = 0;
		public long timestamp = 0;
		public boolean nesting_changed = false;
		public Vector<NodeObject> updates = new Vector<NodeObject>(); // Element Updates
	}

	
	/**
	 * Build Response Tree
	 * @param parent
	 * @param current
	 * @param bdata
	 * @return
	 */
	private Map<String, NodeObject[]> buildResponseTree(Map<String, List<Node>> current, BuilderData bdata) {
		
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

				// Full Node
				NodeObject n = new NodeObject();
				n.timestamp = e.getElementTimestamp();
				n.element = e.getElementName();
				n.uuid = e.getUUID().toString();
				
				// Store Last Timestamp
				if(n.timestamp > bdata.timestamp) bdata.timestamp = n.timestamp; // Element Timestamp

				// Element with Data
				if(e instanceof NodeData) {
					n.data = ((NodeData) e).getElementData();
				}

				// Register Update
				boolean update_added = false;
				
				// Update Data
				if(bdata.remote_timestamp < e.getElementDataTimestamp()) {
					// Store New Updated Data
					if(e instanceof NodeData) {
						//pn.data = ((NodeData) e).getElementData();
					}
					//bdata.updates.add(pn);
					update_added = true;
				}
				
				// Element with Dynamic Data
				if(e instanceof Parent) {
					Parent parent = (Parent) e;
					
					n.children = buildResponseTree(parent.getChildren(), bdata); // Build Child Tree
					
					long nesting_timestamp = parent.getNestingTimestamp();
					if(nesting_timestamp > bdata.timestamp) bdata.timestamp = nesting_timestamp; // Element DataTimestamp
					
					if(bdata.remote_timestamp < nesting_timestamp) {
						// Store New Updated Data
						//
						System.out.println("NODE UPDATE HAPPEND");
						
						//if(update_added == false) updates.add(n);
						
						
						bdata.nesting_changed = true;
					}
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
		

		return nodes;
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
}

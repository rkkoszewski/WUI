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
import java.util.UUID;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;

import com.google.gson.Gson;
import com.robertkoszewski.wui.WUIController;
import com.robertkoszewski.wui.element.Element;
import com.robertkoszewski.wui.element.feature.ActionableElement;
import com.robertkoszewski.wui.element.feature.ElementWithData;
import com.robertkoszewski.wui.element.feature.ElementWithDynamicData;
import com.robertkoszewski.wui.server.*;
import com.robertkoszewski.wui.server.responses.*;
import com.robertkoszewski.wui.templates.Content;
import com.robertkoszewski.wui.templates.ElementTemplate;
import com.robertkoszewski.wui.templates.WindowTemplate;

import net.sf.image4j.codec.ico.ICOEncoder;

public class WUIContentManager implements ContentManager {

	// Static Variables
	private static final int MAX_ICON_SIZE_PX = 64;
	private static final String WUI_SESSION_ID = "WUISESSIONID";
	
	// Variables
	private final WindowTemplate template;
	private final ResourceManager resourceManager;
	private Map<String, WUIController> pages = new HashMap<String, WUIController>();
	private URL icon = null; // Define a more flexible Icon class to allow to define different icon sizes.
	
	// TODO: Temporary Variables. Move them where corresponds
	private Map<String, Element> element_cache = new HashMap<String, Element>();
	private Map<String, ElementTemplate> element_definition_cache = new HashMap<String, ElementTemplate>();
	
	public WUIContentManager(WindowTemplate template) {
		this.template = template;
		this.resourceManager = new WUIResourceManager();
	}

	// Content Management Methods
	@Override
	public void addController(String url, WUIController content) {
		pages.put(url, content);
		
	}
	
	// Response Manager Methods
	@Override
	public Response getResponse(Request request) {
		
		// Response
		Response response = null;
		Map<String, Cookie> cookies = new HashMap<String, Cookie>();
		
		// Has Session?
		String sessionID = request.getCookie(WUI_SESSION_ID);
		// @DEBUG System.out.println("CURRENT SESSION ID: " + sessionID);
		if(sessionID == null) {
			// Generate new Session ID
			sessionID = newSessionID();
			cookies.put(WUI_SESSION_ID, new Cookie(sessionID)); // Update Session ID Cookie
		}

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

			switch (requestType) {
			case ACTION: // Action Performed Request Type
				response =  performAction(request);
				break;

			case CONTENT: // Content Request Type
				// Serve View
				WUIController controller = pages.get(request.getURL()); // TODO: Inline this if nowhere else required
				
				long timestamp = 0;
				try {
					timestamp = Long.parseUnsignedLong(request.getParameter("t").get(0));
				}catch(Exception e) {}
				
				response = toContentResponse(controller, timestamp);
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
					WUIController controller = pages.get(request.getURL()); // TODO: Inline this if nowhere else required
					if(controller != null) {
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
	 * Set Application Icon
	 */
	@Override
	public void setIcon(URL resource) {
		icon = resource;
	}
	
	/**
	 * Generate Session ID (TODO: Improve this and make it more secure)
	 * @return Session ID
	 */
	private String newSessionID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * Perform Action
	 * @param request
	 * @return
	 */
	private Response performAction(Request request) {
		
		String elmentUUID = request.getHeader("x-wui-element");
		
		System.out.println("ACTION PERFORMED ON ELEMENT " + elmentUUID);
		
		
		Element element = element_cache.get(elmentUUID);
		
		if(element != null) {
			if(element instanceof ActionableElement) {
				// Perform Action
				((ActionableElement) element).actionPerformed();
			}
		}
		
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
	private Response toContentResponse(WUIController controller, long remote_timestamp) {
		// Template Node
		//Node root = new Node();
		//root.element = 
		//root.nodes = controller.viewUpdate().getContent().toArray();
			
		// Return Error if controller is null
		if(controller == null) {
			return new WUIStringResponse("text/html", "Content Not Found");
		}
		
		// Element Cache - An Element needs to be part of the DOM in order to be able to be called
		// TODO: Move Element Cache to Content Interface to be part of the Content (Decide?)
		Map<String, Element> element_cache = new HashMap<String, Element>();
		
		
		// Freeze request till data changes
		Lock lock = controller.getLock();
		if(remote_timestamp != 0) {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {}
			}
		}
		
		// Build Node Tree
		Content content = controller.getContent(); // TODO: Improve this.
		Map<String, Node[]> nodes = new HashMap<String, Node[]>();
		long timestamp = 0;
		
		// Element Updates
		Vector<Node> updates = new Vector<Node>();

		// Create Node Branch
		Iterator<Entry<String, Element[]>> bit = content.getElements().entrySet().iterator();
		while(bit.hasNext()) {
			// Branch Iteration
			Entry<String, Element[]> branch = bit.next();
			Element[] elements = branch.getValue();
			Node[] root = new Node[elements.length];
			
			// Build Node Array
			int i = 0;
			for(Element e : branch.getValue()) {
				
				e.addElementUpdateLock(null, lock);
				
				Node n = new Node();
				//n.timestamp = e.getElementTimestamp();
				n.element = e.getElementName();
				n.uuid = e.getElementUUID().toString();
				
				// Store Last Timestamp
				if(n.timestamp > timestamp) timestamp = n.timestamp; // Element Timestamp

				// Element with Data
				if(e instanceof ElementWithData) {
					n.data = ((ElementWithData) e).getElementData();
					
				}
				
				// Element with Dynamic Data
				if(e instanceof ElementWithDynamicData) {
					long element_data_timestamp = ((ElementWithDynamicData) e).getDataTimestamp();
					if(element_data_timestamp > timestamp) timestamp = element_data_timestamp; // Element DataTimestamp
					
					if(remote_timestamp < element_data_timestamp) {
						// Store New Updated Data
						updates.add(n);
					}
				}

				root[i++] = n;
				
				// Add to Element Cache
				// TODO: Rewrite
				element_cache.put(e.getElementUUID().toString(), e);
				
				// Add Element Definition
				// TODO: Rewrite
				if(!element_definition_cache.containsKey(e.getElementName())) {
					element_definition_cache.put(e.getElementName(), e.getElementDefinition()); 
				}
				
			}
			
			// Add Nodes to Branch
			nodes.put(branch.getKey(), root);
		}
		
		
		// Build Content Response
		ContentResponse content_response = new ContentResponse();
		content_response.title = content.getTitle();
		content_response.timestamp = timestamp;
		
		if(remote_timestamp == 0) {
			content_response.type = UpdateStrategy.FULL;
			content_response.nodes = nodes; // Full Content Update
		}else {
			// Partial Data Change Response
			content_response.type = UpdateStrategy.PARTIALDATA;
			content_response.updates = updates; // TODO: If updates.size == 0 then do EMPTY RESPONSE
		}

		// Update Element Cache
		this.element_cache = element_cache;

		//template.
		return new WUIStringResponse("text/json", (new Gson()).toJson(content_response));
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

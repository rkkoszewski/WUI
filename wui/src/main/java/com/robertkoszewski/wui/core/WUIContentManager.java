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
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;

import com.google.gson.Gson;
import com.robertkoszewski.wui.WUIController;
import com.robertkoszewski.wui.elements.Element;
import com.robertkoszewski.wui.server.*;
import com.robertkoszewski.wui.server.responses.*;
import com.robertkoszewski.wui.templates.WindowTemplate;

import net.sf.image4j.codec.ico.ICOEncoder;

public class WUIContentManager implements ContentManager {

	// Static Variables
	private static final int MAX_ICON_SIZE_PX = 64;
	
	// Variables
	private final WindowTemplate template;
	private final ResourceManager resourceManager;
	private Map<String, WUIController> pages = new HashMap<String, WUIController>();
	private URL icon = null; // Define a more flexible Icon class to allow to define different icon sizes.
	
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
		WUIController controller = pages.get(request.getURL());
		
		// Is Content Request?
		boolean isContentRequest = request.getHeader("x-wui-request") != null;
		
		// Debug
		System.out.println("SERVING CONTENT: " + request.getURL() + " (IS CONTENT REQUEST: " + isContentRequest + ")");

		if(isContentRequest) {
			// Content Request
			// Serve View
			if(controller == null) {
				return new WUIStringResponse("text/html", "Content Not Found");
			}else {
				return new WUIStringResponse("text/json", toContentResponse(controller));
			}

		} else {
			
			String url = request.getURL();
			
			// Predefined URLs
			switch(url) {
			
				// SERVE: Application Icon
				case "/favicon.ico":
					try {
						return new WUIFileResponse("image/x-icon", imageToICO(icon));
					} catch (Exception e) {
						// return 404 not found
					}
					break;
					
				case "/favicon.png":
					try {
						return new WUIFileResponse("image/png", imageToFormat(icon, "png"));
					} catch (Exception e) {
						// return 404 not found
					}
					break;
					
			
			}
			
			// Show Current Template or Resource
			if(controller != null) {
				return new WUIStringResponse("text/html", template.getTemplateHTML(resourceManager));
			}
			
			// Serve Resource
			InputStream resource = resourceManager.getResource(url);

			if(resource != null) {
				try {
					System.out.println("SERVING RESOURCE: "+request.getURL());
					return new WUIFileResponse("text/javascript", resource);
					
				} catch (FileNotFoundException e) {
					// Ignore Error
				}
			}
			
			return new WUIStringResponse("text/html","ERROR: Resource not found");
		}

	}

	/**
	 * Set Application Icon
	 */
	@Override
	public void setIcon(URL resource) {
		icon = resource;
	}
	
	// Content Response Generator
	
	private String toContentResponse(WUIController controller) {
		// Template Node
		//Node root = new Node();
		//root.element = 
		//root.nodes = controller.viewUpdate().getContent().toArray();
		
		Vector<Element> content = controller.viewUpdate().getContent();
		Node[] root = new Node[content.size()];

		Iterator<Element> it = content.iterator();
		
		int i = 0;
		while(it.hasNext()) {
			Element e = it.next();
			
			Node n = new Node();
			n.timestamp = e.getTimestamp();
			n.data = e.getElementData();
			n.element = e.getElementName();
			
			root[i++] = n;
		}

		//template.
		return (new Gson()).toJson(root);
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

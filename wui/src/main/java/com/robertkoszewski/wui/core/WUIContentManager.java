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

import java.util.HashMap;
import java.util.Map;

import com.robertkoszewski.wui.WUIController;
import com.robertkoszewski.wui.server.*;
import com.robertkoszewski.wui.server.responses.*;
import com.robertkoszewski.wui.templates.WindowTemplate;

public class WUIContentManager implements ContentManager {

	private final WindowTemplate template;
	private final ResourceManager resourceManager;
	private Map<String, WUIController> pages = new HashMap<String, WUIController>();
	
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
		boolean isContentRequest = request.getHeader("x-wui-content-request") != null;
		
		if(isContentRequest) {
			
			
		}else {
			// Show Current Template
			return new WUIStringResponse("text/html", template.getTemplateHTML(resourceManager));
			
		}
		
		
		System.out.println("SERVING VIEW: " + request.getURL() + " (IS CONTENT REQUEST: " + isContentRequest + ")");

		// Serve View
		if(controller == null)
			return new WUIStringResponse("text/html", "Content Not Found");
		else
			return new WUIStringResponse("text/html", controller.viewUpdate().getHTML());
	}
}

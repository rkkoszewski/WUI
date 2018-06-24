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

package com.robertkoszewski.wui;

import java.awt.GraphicsEnvironment;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.robertkoszewski.wui.core.ContentManager;
import com.robertkoszewski.wui.core.WUIContentManager;
import com.robertkoszewski.wui.core.WebDependency;
import com.robertkoszewski.wui.renderer.Renderer;
import com.robertkoszewski.wui.renderer.RendererFactory;
import com.robertkoszewski.wui.renderer.RendererHeadless;
import com.robertkoszewski.wui.renderer.RendererNative;
import com.robertkoszewski.wui.server.Server;
import com.robertkoszewski.wui.server.ServerFactory;
import com.robertkoszewski.wui.server.ServerNotFoundException;
import com.robertkoszewski.wui.template.BasicTemplate;
import com.robertkoszewski.wui.template.WindowTemplate;
import com.robertkoszewski.wui.utils.SocketUtils;

/**
 * WUI Window Implementation
 * @author Robert Koszewski
 */
public class WUIEngine {
	
	//private final WindowTemplate template;
	private final Server server;
	private final Renderer renderer;
	private final ContentManager contentManager;
	private final Preferences preferences;
	
	// Logger
	protected final Logger log = LoggerFactory.getLogger(WUIEngine.class);
	
	/*
	 * Constructors 
	 */
	public WUIEngine() throws InstantiationException, IllegalAccessException, ServerNotFoundException {
		this(null, new BasicTemplate());
	}
	
	public WUIEngine(WindowTemplate template) throws InstantiationException, IllegalAccessException, ServerNotFoundException {
		this(null, template);
	}
	
	public WUIEngine(Preferences preferences) throws InstantiationException, IllegalAccessException, ServerNotFoundException {
		this(preferences, new BasicTemplate());
	}
	
	public WUIEngine(Preferences preferences, WindowTemplate template) throws InstantiationException, IllegalAccessException, ServerNotFoundException {
		this.contentManager = new WUIContentManager(template);
		this.server = getServerInstance();
		this.renderer = getRendererInstance();
		// Process Preferences
		if(preferences == null) preferences = new Preferences();
		this.preferences = preferences;
	}
	
	/**
	 * Initialize Server
	 * @return
	 * @throws ServerNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private Server getServerInstance() throws InstantiationException, IllegalAccessException, ServerNotFoundException {
		return ServerFactory.getServerInstance();
	}
	
	/**
	 * Initialize Renderer
	 * @return
	 */
	private Renderer getRendererInstance() {
		try {
			return RendererFactory.getRendererInstance();
		} catch (Exception e) {
			System.err.println("ERROR: Could not instantiate Renderer. Defaulting to Native Browser.");
			e.printStackTrace();
		};
		
		// Check if we're running in a Headless Environment
		if (GraphicsEnvironment.isHeadless()) {
			return new RendererHeadless(); // Defaults to Headless Renderer (In Headless Environment)
		}else {
			return new RendererNative(); // Defaults to Native Renderer
		}
	}
	
	

	/*
	 * Methods
	 */
	public void showView() {
		this.showView("/");
	}
	
	public void showView(String viewURL) {
		try {
			Integer port = parseInt(preferences.getSetting("port"));
			// Get Port Number
			if(port == null) {
				port = SocketUtils.getOpenPort();
			}
			
			this.server.startServer(port, contentManager); // Start Server
			
			// Process URL
			if(!viewURL.startsWith("/")) {
				viewURL = "/" + viewURL;
			}
			
			renderer.open("http://localhost:" + port + viewURL, "App Title", null, false, null, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Autoconf this
	}
	
	public void addView(String url, View view) {
		contentManager.addView(url, view);
	}

	public void setIcon(URL resource) {
		contentManager.setIcon(resource);
	}
	
	
	// Helper Methods
	
	/**
	 * Parse Integer
	 * @param number
	 * @return
	 */
	private Integer parseInt(String number) {
		try {
			return Integer.parseInt(number);
		}catch(Exception e) {
			return null;
		}
	}

	/**
	 * Add Dependencies
	 * @param dependencies
	 */
	public void addDependency(WebDependency... dependencies) {
		for(WebDependency dependency: dependencies)
			contentManager.addDependency(dependency);
	}
}

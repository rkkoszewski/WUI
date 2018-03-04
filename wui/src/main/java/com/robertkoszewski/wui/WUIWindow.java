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

import java.net.URL;
import com.robertkoszewski.wui.core.ContentManager;
import com.robertkoszewski.wui.core.WUIContentManager;
import com.robertkoszewski.wui.server.Server;
import com.robertkoszewski.wui.server.ServerFactory;
import com.robertkoszewski.wui.server.ServerNotFoundException;
import com.robertkoszewski.wui.template.BasicTemplate;
import com.robertkoszewski.wui.template.WindowTemplate;

/**
 * WUI Window Implementation
 * @author Robert Koszewski
 */
public class WUIWindow {
	
	//private final WindowTemplate template;
	private final Server server;
	private final ContentManager contentManager;
	
	/*
	 * Constructors 
	 */
	
	public WUIWindow() {
		this(new BasicTemplate());
	}
	
	public WUIWindow(WindowTemplate template) {
		//this.template = template;
		//this.resources = new WUIResourceManager();
		this.contentManager = new WUIContentManager(template);
		this.server = initializeServer();
	}
	
	private Server initializeServer() {
		try {
			return ServerFactory.getServerInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return null; // TODO: Handle this better. We don't want to catch exceptions, but we don't want unexpected nulls
	}
	
	/*
	 * Methods
	 */
	
	public void open() {
		try {
			this.server.startServer(8080, contentManager);
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
}

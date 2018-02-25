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

import com.robertkoszewski.wui.View;

/**
 * Session Class
 * @author Robert Koszewski
 */
public class Session {
	
	private Map<String, ViewInstance> view_instances = new HashMap<String, ViewInstance>();
	private final SessionManager session_manager;
	
	public Session(SessionManager session_manager) {
		this.session_manager = session_manager;
	}
	
	/**
	 * Get View Instance
	 * @param url
	 * @return View Instance
	 */
	public ViewInstance getViewInstance(String url) {
		ViewInstance view_instance = view_instances.get(url);
		
		// View Instance Not Available
		if(view_instance == null) {
			// Initialize View Instance
			View view = session_manager.content_manager.getView(url); // Get View Intializer
			
			if(view != null) {
				view_instance = view.getViewInstance(session_manager.template); // Create New View Instance
				view_instances.put(url, view_instance); // Store Instance		
			}
		}

		return view_instance;
	}
}

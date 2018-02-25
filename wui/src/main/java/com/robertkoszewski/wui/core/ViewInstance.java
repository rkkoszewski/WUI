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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.robertkoszewski.wui.element.Element;
import com.robertkoszewski.wui.templates.Content;

/**
 * View Instance
 * @author Robert Koszewski
 */
public class ViewInstance {
	
	private Map<String, Element> element_uuid_to_element_cache = new HashMap<String, Element>();
	private Lock view_lock = new ReentrantLock();
	private final Content content;
	
	public ViewInstance(Content content) {
		this.content = content;
	}
	
	/**
	 * Get View Content
	 * @return
	 */
	public Content getContent() {
		return content;
	}
	
	/**
	 * Perform Action on Element
	 * @param element_uuid
	 * @return
	 */
	public boolean performActionOnElement(String element_uuid) {
		Element el = element_uuid_to_element_cache.get(element_uuid);
		if(el == null) return false; // Return False if Element is not found
		el.actionPerformed(); // Perform Action
		return true; // Return True
	}
	
	public Lock getViewLock() { // TODO: Change this to a waitForViewChange and viewChanged method
		return view_lock;
	}
}

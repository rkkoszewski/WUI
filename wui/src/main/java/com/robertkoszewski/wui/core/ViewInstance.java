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

import com.robertkoszewski.wui.template.BaseContent;
import com.robertkoszewski.wui.template.WindowTemplate;
import com.robertkoszewski.wui.ui.element.Node;

/**
 * View Instance
 * @author Robert Koszewski
 */
public class ViewInstance {
	
	private Map<String, Node> element_uuid_to_element = new HashMap<String, Node>();
	private Lock view_lock = new ReentrantLock();
	private final BaseContent<?, ?, ?> content;
	
	public ViewInstance(WindowTemplate template) {
		this.content = template.getContentInstance(this);
	}
	
	/**
	 * Get View Content
	 * @return
	 */
	public BaseContent<?, ?, ?> getContent() {
		return content;
	}
	
	/**
	 * Perform Action on Element
	 * @param element_uuid
	 * @return
	 */
	public boolean performActionOnElement(String element_uuid) {
		Node el = element_uuid_to_element.get(element_uuid);
		if(el == null) return false; // Return False if Element is not found
		el.actionPerformed(); // Perform Action
		return true; // Return True
	}
	
	/**
	 * Get Element by UUID
	 * @param element_uuid
	 * @return
	 */
	public Node getElement(String element_uuid) {
		return element_uuid_to_element.get(element_uuid);
	}
	
	/**
	 * Notify that View has changed
	 */
	public void viewChanged() {
		synchronized(view_lock){
			view_lock.notifyAll();
		}
	}
	
	/**
	 * Wait for View Change
	 * @throws InterruptedException 
	 */
	public void waitForViewChange() throws InterruptedException {
		synchronized (view_lock) {
			view_lock.wait();
		}
	}

	/**
	 * Add Element to Cache
	 * @param element
	 */
	public void addElementToCache(Node element) {
		element_uuid_to_element.put(element.getUUID().toString(), element);
	}

	/**
	 * Remove Element from Cache
	 * @param node_uuid
	 */
	public void removeElementFromCache(String node_uuid) {
		element_uuid_to_element.remove(node_uuid);
	}
}

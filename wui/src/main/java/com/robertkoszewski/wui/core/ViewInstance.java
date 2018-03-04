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

import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.template.ContentData;
import com.robertkoszewski.wui.ui.element.feature.BaseElement;

/**
 * View Instance
 * @author Robert Koszewski
 */
public class ViewInstance {
	
	private Map<String, BaseElement> element_uuid_to_element = new HashMap<String, BaseElement>();
	private Lock view_lock = new ReentrantLock();
	private final ContentData content;
	
	public ViewInstance(ContentData content) {
		content.setViewInstance(this);
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
		BaseElement el = element_uuid_to_element.get(element_uuid);
		if(el == null) return false; // Return False if Element is not found
		el.actionPerformed(); // Perform Action
		return true; // Return True
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
	public void addElementToCache(BaseElement element) {
		element_uuid_to_element.put(element.getElementUUID().toString(), element);
	}
}

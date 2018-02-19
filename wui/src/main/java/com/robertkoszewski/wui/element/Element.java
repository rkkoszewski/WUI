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

package com.robertkoszewski.wui.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

import com.robertkoszewski.wui.element.feature.ActionableElement;
import com.robertkoszewski.wui.element.feature.ElementWithElementTimestamp;
import com.robertkoszewski.wui.utils.Utils;

/**
 * Base Element
 * @author Robert Koszewski
 */
public abstract class Element implements ElementWithElementTimestamp, ActionableElement {

	private ArrayList<Runnable> action_performed_callback;
	//private ArrayList<Lock> container_locks = new ArrayList<Lock>();
	private Lock lock;
	
	private long data_timestamp = Utils.getTimestamp(); // Element UID	
	private String element_name = this.getClass().getSimpleName();//.getName();
	private UUID element_uuid = UUID.randomUUID();

	/**
	 * Get Element Name
	 * @return
	 */
	public String getElementName() {
		return element_name;
	}
	
	/**
	 * Element UUID
	 * @return
	 */
	public UUID getElementUUID() {
		return element_uuid;
	}
	
	/**
	 * Returns the Element Last Modified time stamp
	 * @return Time stamp
	 */
	public long getElementTimestamp(){
		return data_timestamp;
	}
	
	/**
	 * Update the Element time stamp
	 */
	public void updateElementTimestamp(){
		this.data_timestamp = Utils.getTimestamp();
		triggerElementUpdate();
	}

	/**
	 * Trigger Action Performed
	 */
	public void actionPerformed() {
		if(action_performed_callback == null) return;
		Iterator<Runnable> it = action_performed_callback.iterator();
		while(it.hasNext())
			it.next().run();
	}
	
	/**
	 * Add Action Listener
	 */
	public void addActionListener(Runnable callback) {
		if(action_performed_callback == null) action_performed_callback= new ArrayList<Runnable>(); // Lazy Instantiation
		action_performed_callback.add(callback);
		updateElementTimestamp(); // TODO: Only run this when a new Action Type is added
	}	
	
	/**
	 * Remove Action Listener
	 */
	public void removeActionListener(Runnable callback) {
		if(action_performed_callback == null) return;
		action_performed_callback.remove(callback);
		updateElementTimestamp(); // TODO: Only run this when a new Action Type is added
	}
	
	/**
	 * Notify Element Update
	 */
	public void triggerElementUpdate() {
		if(lock == null) return;
		System.out.println("ELEMENT UPDATE TRIGGERED");
		synchronized(lock) {
			lock.notifyAll();
		}
	}
	
	public void addElementUpdateLock(Object element, Lock lock) {
		this.lock = lock;
	}
	
}

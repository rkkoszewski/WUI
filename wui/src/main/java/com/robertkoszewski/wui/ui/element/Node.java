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

package com.robertkoszewski.wui.ui.element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.robertkoszewski.wui.core.ViewInstance;
import com.robertkoszewski.wui.ui.element.type.EventTarget;
import com.robertkoszewski.wui.ui.element.type.HTMLElement;
import com.robertkoszewski.wui.ui.element.type.NodeData;
import com.robertkoszewski.wui.utils.Utils;

/**
 * It is the base class for elements that can be represented in the WUI Graphical Interface.
 * Any Node can have a parent Node, but if it is the root node then NULL will be returned.
 * @author Robert Koszewski
 */
public abstract class Node implements EventTarget, NodeData, HTMLElement{
	
	// Variables
	private UUID element_uuid = UUID.randomUUID(); // Element Unique Identifiable ID
	private String id; // Element ID
	private ArrayList<Runnable> action_performed_callback; // Action Performed Callbacks
	private long element_timestamp = Utils.getChangeTimestamp(); // Element UID	
	protected final Map<ViewInstance, Node> views = new HashMap<ViewInstance, Node>(); // Views Container
	
	// Element Data
	protected final Map<String, String> data = new HashMap<String, String>(); // Element Data
	private long element_data_timestamp = 0; // Element Data Timestamp
	
	
	/**
	 * Get Element Name
	 * @return
	 */
	public String getElementName() {
		return this.getClass().getSimpleName(); //.getName(); // TODO: Change this to a Full Path
	}
	
	/**
	 * Returns the Element Last Modified time stamp
	 * @return Time stamp
	 */
	public long getElementTimestamp(){
		return element_timestamp;
	}
	
	/**
	 * Update the Element time stamp
	 */
	public void updateElement(){
		this.element_timestamp = Utils.getChangeTimestamp();
		// Notify Element Update
		Iterator<ViewInstance> vit = views.keySet().iterator();
		while(vit.hasNext())
			vit.next().viewChanged();
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
		updateElement(); // TODO: Only run this when a new Action Type is added
	}	
	
	@Override
	public void removeActionListener(Runnable callback) {
		action_performed_callback.remove(callback);
		updateElement(); // TODO: Only run this when a Action Type is really removed
	}
	
	/**
	 * Get Element Data
	 */
	public Object getElementData() {
		return data;
	}
	
	/**
	 * Get Element Data Timestamp
	 */
	public long getElementDataTimestamp() {
		return element_data_timestamp;
	}
	
	/**
	 * Update ELement Data Timestamp
	 */
	protected void updateElementDataTimestamp() {
		element_data_timestamp = Utils.getChangeTimestamp();
		updateElement();
	}
	
	/**
	 * Add Element to View
	 * @param view
	 * @param parent_element
	 */
	public void addElementToView(ViewInstance view, Node parent_element) {
		// TODO: Check if element is already in View, then eider throw exception or remove old position
		if(views.get(view) != null) throw new NullPointerException("STILL NOT IMPLEMENTED: Element Exists in View.");
		views.put(view, parent_element);
		view.addElementToCache(this);
	};
	
	/**
	 * Remove Element from View
	 * @param view
	 */
	public void removeElementFromView(ViewInstance view) {
		views.remove(view);
		view.removeElementFromCache(getUUID().toString());
	}	
	
	/**
	 * Element UUID
	 * @return
	 */
	public UUID getUUID() {
		return element_uuid;
	}
	
	// ID
	/**
	 * Set Element ID
	 * @param id
	 */
	public void setID(String id) {
		this.id = id;
	}
	
	/**
	 * Get Element ID
	 * @return
	 */
	public String getID() {
		return id;
	}
}

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

package com.robertkoszewski.wui.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.robertkoszewski.wui.core.ViewInstance;
import com.robertkoszewski.wui.structs.TimestampedViewNotifyingEnumMap;
import com.robertkoszewski.wui.ui.element.Node;
import com.robertkoszewski.wui.utils.Utils;

/**
 * Base Content
 * @author Robert Koszewski
 */
public abstract class BaseContent<E extends Enum<E>, T extends Enum<T>, X> implements Content {

	// Variables
	private final ViewInstance viewInstance;
	protected TimestampedViewNotifyingEnumMap<T, X> data;
	protected TimestampedViewNotifyingEnumMap<E, List<Node>> children;
	private Map<String, Object> shared_data = new HashMap<String, Object>();

	// Constructor
	public BaseContent(Class<E> child_class, Class<T> data_class, ViewInstance viewInstance) {
		this.viewInstance = viewInstance;
		children = new TimestampedViewNotifyingEnumMap<E, List<Node>>(child_class, viewInstance);
		data = new TimestampedViewNotifyingEnumMap<T, X>(data_class, viewInstance);
	}
	
	// Node Management
	
	/**
	 * Add Element to Branch
	 * @param id
	 * @param element
	 */
	protected void addElement(E id, Node element) {
		List<Node> branch = children.get(id);
		boolean triggered = false; // TODO: This is just a workaround. Implement this properly.
		if(branch == null) { 
			branch = new ArrayList<Node>();
			children.put(id, branch);
			triggered = true;
		}
		// Add Element to View
		element.addElementToView(viewInstance, null); // TODO: Double check this NULL here.
		// Add Element		
		branch.add(element);
		if(!triggered) children.triggerContentChanged();
	}
	
	/**
	 * Get Nesting Timestamp
	 * @return
	 */
	public long getNestingTimestamp() {
		return children.getTimestamp();
	}

	/**
	 * Add Child Element
	 * @param key
	 * @param element
	 * @param instance
	 */
	/*
	protected void addChildElement(E key, Element element, ViewInstance instance) {
		List<Element> branch = children.get(key);
		// Instantiate Array if not available
		if(branch == null) { 
			branch = new ArrayList<Element>();
			children.put(key, branch);
		}
		// Add Element to View
		element.addElementToView(instance, this);
		// Put into Array
		branch.add(element);
		// Update Nesting Timestamp
//		updateNestingTimestamp();
	}
	*/
	
	/**
	 * Remove Element from Page
	 * @param element
	 */
	public void removeElement(Node element){
		/*
		content.remove(element);
		updateElementTimestamp();
		updateNestingTimestamp();
		*/
	}
	
	/**
	 * Remove Element at Index
	 * @param index
	 */
	public void removeElementAt(int index){
		//content.remove(index);
	}
	
	// Data Methods
	
	/**
	 * Return Element Object
	 */
	public Map<String, List<Node>> getChildren() {
		/*
		Map<String, Node[]> elements = new HashMap<String, Node[]>();
		Iterator<Entry<E, List<Node>>> cit = children.entrySet().iterator();
		while(cit.hasNext()) {
			Entry<E, List<Node>> entry = cit.next();
			List<Node> value = entry.getValue();
			elements.put(entry.getKey().name(), value.toArray(new Node[value.size()]));
		}
		return elements;
		*/
		Map<String, List<Node>> elements = new HashMap<String, List<Node>>();
		Iterator<Entry<E, List<Node>>> cit = children.entrySet().iterator();
		while(cit.hasNext()) {
			Entry<E, List<Node>> entry = cit.next();
			elements.put(entry.getKey().name(), entry.getValue());
		}
		return elements;
	}
	
	/**
	 * Return Element Object
	 */
	public Object getDataObject() {
		return data;
	}
	
	/**
	 * Get Data Timestamp
	 * @return
	 */
	public long getDataTimestamp() {
		return data.getTimestamp();
	}

	// Shared Data Methods
	
	@Override
	public void setSharedData(String name, Object obj) {
		shared_data.put(name, obj);
	}

	@Override
	public Object getSharedData(String name) {
		return shared_data.get(name);
	}

	@Override
	public <T> T getSharedData(String name, Class<T> classOfT) {
		return classOfT.cast(shared_data.get(name));
	}
	
	// TODO: Remove this. This is just a temporary workaround to keep the ViewContext
	@Override
	public ViewInstance getViewInstance() {
		return viewInstance;
	}
}

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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.robertkoszewski.wui.core.ViewInstance;
import com.robertkoszewski.wui.ui.element.Element;
import com.robertkoszewski.wui.ui.element.feature.BaseElement;
import com.robertkoszewski.wui.ui.element.feature.BaseRootElement;

/**
 * Base Content
 * @author Robert Koszewski
 */
public abstract class BaseContent<E extends Enum<E>, T extends Enum<T>, X> extends BaseRootElement implements Content, ContentData {

	// Variables
	protected ViewInstance viewInstance;
	protected EnumMap<T, X> data;
	protected EnumMap<E, List<Element>> children;

	// Constructor
	public BaseContent(Class<E> child_class, Class<T> data_class) {
		children = new EnumMap<E, List<Element>>(child_class);
		data = new EnumMap<T, X>(data_class);
	}
	
	
	// Node Management
	
	/**
	 * Add Element to Branch
	 * @param id
	 * @param element
	 */
	protected void addElement(E id, Element element) {
		List<Element> branch = children.get(id);
		if(branch == null) { 
			branch = new ArrayList<Element>();
			children.put(id, branch);
		}
		// Add Element to View
		element.addElementToView(viewInstance, this);
				
		branch.add(element); // TODO: DO something with viewInstance
		
	}
	
	/**
	 * Add Child Element
	 * @param key
	 * @param element
	 * @param instance
	 */
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
	
	/**
	 * Remove Element from Page
	 * @param element
	 */
	public void removeElement(BaseElement element){
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
	
	
	@Override
	public void setViewInstance(ViewInstance viewInstance) {
		this.viewInstance = viewInstance;
	}
	
	// Data Methods
	
	/**
	 * Return Element Object
	 */
	public Map<String, Element[]> getElements() {
		Map<String, Element[]> elements = new HashMap<String, Element[]>();
		Iterator<Entry<E, List<Element>>> cit = children.entrySet().iterator();
		while(cit.hasNext()) {
			Entry<E, List<Element>> entry = cit.next();
			List<Element> value = entry.getValue();
			elements.put(entry.getKey().name(), value.toArray(new Element[value.size()]));
		}
		return elements;
	}
	
	/**
	 * Return Element Object
	 */
	public Object getDataObject() {
		return data;
	}
}

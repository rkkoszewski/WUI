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

package com.robertkoszewski.wui.ui.element.feature;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.robertkoszewski.wui.core.ViewInstance;
import com.robertkoszewski.wui.ui.element.Element;
import com.robertkoszewski.wui.ui.element.NodeElement;
import com.robertkoszewski.wui.utils.Utils;

/**
 * Element With Nesting
 * @author Robert Koszewski
 */
public abstract class BaseNodeElement<T extends Enum<T>> extends BaseElement implements NodeElement{

	protected long element_nesting_timestamp = Utils.getChangeTimestamp(); // Timestamp
	private EnumMap<T, List<Element>> children; // Child Elements
	
	/**
	 * Constructor
	 * @param clazz
	 */
	public BaseNodeElement(Class<T> clazz) {
		children = new EnumMap<T, List<Element>>(clazz); 
	}
	
	/**
	 * Returns the Element Last Modified time stamp
	 * @return Time stamp
	 */
	public long getNestingTimestamp(){
		return element_nesting_timestamp;
	}
	
	/**
	 * Update the Element time stamp
	 */
	protected void updateNestingTimestamp(){
		this.element_nesting_timestamp = Utils.getChangeTimestamp();
	}
	
	/**
	 * Add Child Element
	 * @param key
	 * @param element
	 * @param instance
	 */
	protected void addChildElement(T key, BaseElement element, ViewInstance instance) {
		List<Element> branch = children.get(key);
		// Instantiate Array if not available
		if(branch == null) branch = new ArrayList<Element>();
		// Add Element to View
		element.addElementToView(instance, this);
		// Put into Array
		branch.add(element);
		// Update Nesting Timestamp
		updateNestingTimestamp();
	}
	
	public Map<T, List<Element>> getChildElements() {
		return children;
	}
	
	
}

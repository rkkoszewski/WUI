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
import java.util.List;
import java.util.Map;

import com.robertkoszewski.wui.core.ViewInstance;
import com.robertkoszewski.wui.utils.Utils;

/**
 * A Node is an Element that can have Child Elements and a Parent Element
 * @author Robert Koszewski
 */
public abstract class Parent extends Node{
	
	// Variables
	protected Map<String, List<Node>> children = new HashMap<String, List<Node>>();
	private long children_timestamp = 0;
	
	/**
	 * Get Children
	 * @return
	 */
	public Map<String, List<Node>> getChildren() {
		return children;
	};
	
	/**
	 * Add Child Element
	 * @param viewInstance
	 * @param element
	 */
	protected void addChildren(Node element, String id) {
		List<Node> branch = children.get(id);
		if(branch == null) { 
			branch = new ArrayList<Node>();
			children.put(id, branch);
		}
		// Add Element to View
		Iterator<ViewInstance> vit = views.keySet().iterator();
		while(vit.hasNext()) element.addElementToView(vit.next(), this);
		// Add Element		
		branch.add(element);
		// Update Nesting Timestamp
		updateNestingTimestamp();
	}
	
	/**
	 * Get Nesting Timestamp
	 * @return
	 */
	public long getNestingTimestamp() {
		return children_timestamp;
	}
	
	/**
	 * Update Nesting Timestamp
	 */
	protected void updateNestingTimestamp() {
		children_timestamp = Utils.getChangeTimestamp();
		updateElement();
	}
	
}

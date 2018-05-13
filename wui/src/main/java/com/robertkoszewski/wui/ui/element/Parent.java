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
import java.util.Map.Entry;

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
	protected void addChild(Node element, String id) {
		startWrite();
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
		endWrite();
	}
	
	/**
	 * Remove a existing Child Element
	 * @param element
	 * @param id
	 */
	protected void removeChild(Node element) {
		startWrite();
		// Remove Element from View
		Iterator<ViewInstance> vit = views.keySet().iterator();
		while(vit.hasNext()) element.removeElementFromView(vit.next());
		
		// Remove Element		
		Iterator<Entry<String, List<Node>>> bit = children.entrySet().iterator();
		while(bit.hasNext()) {
			bit.next().getValue().remove(element);
		}
		
		// Update Nesting Timestamp
		updateNestingTimestamp();
		endWrite();
	}
	
	/**
	 * Get Child Nodes
	 * @param id
	 * @return
	 */
	protected Node[] getChildren(String id) {
		List<Node> branch = getChildren().get(id);
		if(branch == null) return new Node[0];
		return branch.toArray(new Node[branch.size()]);
	}
	
	/**
	 * Clear all Child Nodes
	 */
	protected void clearChildren(String id) {
		Node[] nodes = getChildren(id);
		for(Node node: nodes) {
			removeChild(node);
		}
	}
	
	/**
	 * Clear all Child Nodes from all branches
	 */
	protected void clearChildren() {
		Iterator<String> br = children.keySet().iterator();
		while(br.hasNext()) {
			String id = br.next();
			Node[] nodes = getChildren(id);
			for(Node node: nodes) {
				removeChild(node);
			}
		}
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
	private void updateNestingTimestamp() {
		children_timestamp = Utils.getChangeTimestamp();
		updateElement();
	}
	
	/**
	 * Add Element to View inclusive all Child Elements
	 */
	public void addElementToView(ViewInstance view, Parent parent_element) {
		// Add Element to View
		super.addElementToView(view, parent_element); 
		// Add Child Elements to View
		Iterator<List<Node>> cit = children.values().iterator();
		while(cit.hasNext()) {
			List<Node> branch = cit.next();
			Iterator<Node> bit = branch.iterator();
			while(bit.hasNext())
				bit.next().addElementToView(view, parent_element);
		}
	};
	
}

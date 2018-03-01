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

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.robertkoszewski.wui.ui.feature.BaseElement;
import com.robertkoszewski.wui.ui.feature.ElementWithData;
import com.robertkoszewski.wui.ui.feature.ElementWithSingleNesting;
import com.robertkoszewski.wui.ui.feature.NestedElement;

/**
 * Base Content
 * @author Robert Koszewski
 */
public class BaseContent extends NestedElement<BaseContent.CNodes, String> implements Content, ElementWithData, ElementWithSingleNesting {
	
	public BaseContent() {
		super(CNodes.class);
	}

	private String title;
	private Vector<BaseElement> content = new Vector<BaseElement>();
	private Map<String, Object> data = new HashMap<String, Object>();
	
	/**
	 * Serialize a Page
	 * @return
	 */
	public Map<String, BaseElement[]> getElements(){
		Map<String, BaseElement[]> elements = new HashMap<String, BaseElement[]>();
		elements.put("body", content.toArray(new BaseElement[content.size()])); // TDDO: Protect concurrency on "content"
		return elements;
	}
	
	/**
	 * Set Page Title
	 * @param title
	 */
	public void setTitle(String title){
		this.title = title;
	}
	
	/**
	 * Get Page Title
	 * @return
	 */
	public String getTitle(){
		return title;
	}
	
	/**
	 * Add Element to Page
	 * @param element
	 */
	public void addElement(BaseElement element){
		content.addElement(element);
		updateNestingTimestamp();
	}
	
	/**
	 * Remove Element from Page
	 * @param element
	 */
	public void removeElement(BaseElement element){
		content.remove(element);
		updateElementTimestamp();
		updateNestingTimestamp();
	}
	
	/**
	 * Remove Element at Index
	 * @param index
	 */
	public void removeElementAt(int index){
		content.remove(index);
	}

	/**
	 * Get Element Data
	 */
	@Override
	public Object getElementData() {
		return title;
	}

	/**
	 * Get Child Elements
	 */
	@Override
	public BaseElement[] getChildElements() {
		return content.toArray(new BaseElement[content.size()]);
	}

	@Override
	public ElementTemplate getElementDefinition() { // TODO: THIS IS NOT A HTML ELEMENT. REMOVE THIS.
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setData(String name, Object obj) {
		data.put(name, obj);
	}

	@Override
	public Object getData(String name) {
		return data.get(name);
	}

	@Override
	public <T> T getData(String name, Class<T> classOfT) {
		return classOfT.cast(data.get(name));
	}
	
	protected enum CNodes {
		
	}
}

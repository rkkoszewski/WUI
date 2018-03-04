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

import java.util.Map;

import com.robertkoszewski.wui.ui.element.Element;
import com.robertkoszewski.wui.ui.element.RootElement;

/**
 * Content Interface
 * @author Robert Koszewski
 */
public interface Content extends RootElement{
	
	/**
	 * Set Page Title
	 * @param title
	 */
	public void setTitle(String title);
	
	/**
	 * Get Page Title
	 * @return
	 */
	public String getTitle();

	/**
	 * Add Element to Body
	 * @param element
	 */
	public void addElement(Element element);
	
	/**
	 * Remove Element from Body
	 * @param element
	 */
	public void removeElement(Element element);
	
	/**
	 * Remove Element at Index
	 * @param index
	 */
	public void removeElementAt(int index);
	
	/**
	 * Get Page Content
	 * @return
	 */
	public Map<String, Element[]> getElements();

	/**
	 * Set Content Data
	 * @param id
	 * @param obj
	 */
	public void setData(String name, Object obj);
	
	/**
	 * Get Content Data
	 * @param id
	 * @return
	 */
	public Object getData(String name);
	
	/**
	 * Get Content Data with Type Definition
	 * @param id
	 * @param clazz
	 * @return
	 */
	public <T> T getData(String name, Class<T> classOfT);

}

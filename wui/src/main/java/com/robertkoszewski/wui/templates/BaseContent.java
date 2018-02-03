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

package com.robertkoszewski.wui.templates;

import java.util.Vector;

import com.robertkoszewski.wui.elements.AbstractTimestampedElement;
import com.robertkoszewski.wui.elements.HTMLElement;

/**
 * Base Content
 * @author Robert Koszewski
 */
public class BaseContent extends AbstractTimestampedElement implements Content {
	
	private String title;
	private Vector<HTMLElement> content = new Vector<HTMLElement>();
	
	/**
	 * Serialize a Page
	 * @return
	 */
	public Vector<HTMLElement> getContent(){
		return content;
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
	public void addElement(HTMLElement element){
		content.addElement(element);
	}
	
	/**
	 * Remove Element from Page
	 * @param element
	 */
	public void removeElement(HTMLElement element){
		content.remove(element);
		updateTimestamp();
	}
	
	/**
	 * Remove Element at Index
	 * @param index
	 */
	public void removeElementAt(int index){
		content.remove(index);
	}
	
	// TESTING METHODS
	
	String html = "";

	@Override
	public void setHTML(String html) {
		this.html = html;
	}

	@Override
	public String getHTML() {
		return this.html;
	}
	
	
}

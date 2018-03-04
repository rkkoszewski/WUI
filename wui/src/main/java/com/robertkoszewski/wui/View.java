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

package com.robertkoszewski.wui;

import com.robertkoszewski.wui.core.ViewInstance;
import com.robertkoszewski.wui.core.ViewInterface;
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.template.ContentData;
import com.robertkoszewski.wui.template.WindowTemplate;

/**
 * View Definition
 * @author Robert Koszewski
 */
public abstract class View implements ViewInterface{

	private final Type type;
	
	public View(Type type) {
		this.type = type;
	}
	
	/**
	 * On Destroy Callback
	 */
	public void onDestroy() {}
	
	/**
	 * Get View Instance
	 * @param template
	 * @return
	 */
	public final ViewInstance getViewInstance(WindowTemplate template) {
		ContentData content = template.getContentInstance();
		// TODO: Change behavior based on Type
		switch(type) {
			default:
		}
		ViewInstance instance = new ViewInstance(content);
		createView(content); // Populate View
		return instance;
	}
	
	/**
	 * View Visibility Type
	 * @author Robert Koszewski
	 */
	public enum Type{
		GLOBAL,
		PRIVATE
	}
}
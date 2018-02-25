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

package com.robertkoszewski.wui.element.feature;

import com.robertkoszewski.wui.element.Element;
import com.robertkoszewski.wui.utils.Utils;

/**
 * Abstract Actionable Element with Simple Dynamic Data
 * @author Robert Koszewski
 */
public abstract class AbstractElementWithSimpleDynamicData<T> extends Element implements ElementWithDynamicData {
	
	// Variables
	private T data; // Element Data
	private long data_timestamp = Utils.getChangeTimestamp(); // Element Data Timestamp
	
	/**
	 * Set Text Input Value
	 * @param value
	 */
	protected void setData(T value) {
		data = value;
		updateDataTimestamp();
	}
	
	/**
	 * Get Text Input Value
	 * @return
	 */
	protected T getData() {
		return data;
	}
	
	// HTML Element Methods

	@Override
	public Object getElementData() {
		return data;
	}
	
	// Time Methods
	
	@Override
	public long getDataTimestamp() {
		return data_timestamp;
	}

	@Override
	public void updateDataTimestamp() {
		this.data_timestamp = Utils.getChangeTimestamp();
		triggerElementUpdate();
	}
}

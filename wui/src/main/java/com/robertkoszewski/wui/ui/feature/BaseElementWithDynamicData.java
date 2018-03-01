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

package com.robertkoszewski.wui.ui.feature;

import java.util.EnumMap;

import com.robertkoszewski.wui.utils.Utils;

/**
 * Abstract Actionable Element with Dynamic Data
 * @author Robert Koszewski
 */
public abstract class BaseElementWithDynamicData<T extends Enum<T>, E> extends BaseElement implements ElementWithDynamicData {
	
	// Variables
	protected EnumMap<T, E> data; // Element Data
	private long data_timestamp = Utils.getChangeTimestamp(); // Element Data Timestamp
	
	/**
	 * Constructor
	 * @param clazz
	 */
	public BaseElementWithDynamicData(Class<T> clazz) {
		data = new EnumMap<T, E>(clazz); 
	}
	
	/**
	 * Set Text Input Value
	 * @param value
	 */
	protected void setData(T key, E value) {
		data.put(key, value);
		updateDataTimestamp();
	}
	
	/**
	 * Get Text Input Value
	 * @return
	 */
	protected E getData(T key) {
		return data.get(key);
	}
	
	/**
	 * Remove Data
	 * @param key
	 */
	protected void removeData(T key) {
		data.remove(key);
		updateDataTimestamp();
	}
	
	/**
	 * Clear Data
	 */
	protected void clearData() {
		data.clear();
		updateDataTimestamp();
	}
	
	// TODO: Implement a ChangeNotificable HashMap
	
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

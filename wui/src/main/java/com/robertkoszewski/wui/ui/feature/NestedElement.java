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
 * Element With Nesting
 * @author Robert Koszewski
 */
public abstract class NestedElement<T extends Enum<T>, E> extends BaseElement implements ElementWithNestingTimestamp{

	protected long element_nesting_timestamp = Utils.getChangeTimestamp(); // Timestamp
	
	
	protected EnumMap<T, E> children; // Element Data
	
	/**
	 * Constructor
	 * @param clazz
	 */
	public NestedElement(Class<T> clazz) {
		children = new EnumMap<T, E>(clazz); 
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
	public void updateNestingTimestamp(){
		this.element_nesting_timestamp = Utils.getChangeTimestamp();
	}
	
}

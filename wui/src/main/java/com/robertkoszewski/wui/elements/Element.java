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

package com.robertkoszewski.wui.elements;

import java.util.UUID;

import com.robertkoszewski.wui.utils.Utils;

/**
 * Base Element
 * @author Robert Koszewski
 */
public abstract class Element implements ElementWithData, ElementWithTimestamp {

	protected long data_timestamp = Utils.getTimestamp(); // Element UID	
	protected String element_name = this.getClass().getSimpleName();//.getName();
	protected UUID element_uuid = UUID.randomUUID();

	/**
	 * Get Element Name
	 * @return
	 */
	public String getElementName() {
		return element_name;
	}
	
	/**
	 * Returns the Element Last Modified time stamp
	 * @return Time stamp
	 */
	public long getTimestamp(){
		return data_timestamp;
	}
	
	/**
	 * Update the Element time stamp
	 */
	public void updateTimestamp(){
		this.data_timestamp = Utils.getTimestamp();
	}

	
	
}

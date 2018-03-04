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

import java.util.UUID;

import com.robertkoszewski.wui.template.ElementTemplate;

/**
 * Root UI Element
 * @author Robert Koszewski
 */
public interface RootElement {
	
	/**
	 * Get Element Definition
	 * @return Element Definition
	 */
	public ElementTemplate getElementDefinition();
	
	/**
	 * Get Element Name
	 * @return
	 */
	public String getElementName();
	
	/**
	 * Get Element UUID
	 * @return
	 */
	public UUID getElementUUID();
	
	/**
	 * Get Element ID
	 * @return id
	 */
	public String getID();
	
	/**
	 * Set Element ID
	 * @param id
	 */
	public void setID(String id);
	
	/**
	 * Get Element Timestamp
	 * @return
	 */
	public long getElementTimestamp();
	
	/**
	 * Update Element Timestamp
	 */
	// public void updateElementTimestamp();
}

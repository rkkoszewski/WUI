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

import com.robertkoszewski.wui.template.ElementTemplate;
import com.robertkoszewski.wui.ui.feature.BaseElementWithDynamicData;

/**
 * Text Input Element
 * @author Robert Koszewski
 */
public class TextInput extends BaseElementWithDynamicData<TextInput.Data, String>{

	/**
	 * Constructor
	 */
	public TextInput() {
		super(Data.class);
	}
	
	/**
	 * Set Text Input Value
	 * @param value
	 */
	protected void setValue(String value) {
		data.put(Data.value, value);
	}
	
	/**
	 * Get Text Input Value
	 * @return
	 */
	protected String getValue() {
		String v = data.get(Data.value);
		return (v != null ? v : "");
	}

	@Override
	public ElementTemplate getElementDefinition() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * TextInput Data
	 */
	protected enum Data{
		value
	}
}

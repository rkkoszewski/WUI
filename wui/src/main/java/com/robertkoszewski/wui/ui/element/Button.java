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

import java.io.IOException;

import com.robertkoszewski.wui.Test;
import com.robertkoszewski.wui.template.ElementTemplate;
import com.robertkoszewski.wui.ui.element.feature.BaseElementWithDynamicData;

/**
 * Button Element
 * @author Robert Koszewski
 */
public class Button extends BaseElementWithDynamicData<Button.Data, String>{

	// Constructors

	public Button(String value) {
		super(Data.class);
		setValue(value);
	}
	
	/**
	 * Set Text Input Value
	 * @param value
	 */
	public void setValue(String value) {
		data.put(Data.value, value);
	}
	
	/**
	 * Get Text Input Value
	 * @return
	 */
	public String getValue() {
		String v = data.get(Data.value);
		return (v != null ? v : "");
	}

	@Override
	public ElementTemplate getElementDefinition() {
		try {
			return new ElementTemplate(Test.class.getResourceAsStream("/com/robertkoszewski/wui/resources/templates/base/elements/Button.html"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Button Data
	 */
	protected enum Data{
		value
	}
}

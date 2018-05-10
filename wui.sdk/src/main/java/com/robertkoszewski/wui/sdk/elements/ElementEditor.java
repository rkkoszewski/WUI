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

package com.robertkoszewski.wui.sdk.elements;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.io.IOUtils;

import com.robertkoszewski.wui.core.EventListener;
import com.robertkoszewski.wui.template.ElementTemplate;
import com.robertkoszewski.wui.ui.element.Node;
import com.robertkoszewski.wui.utils.FileUtils;

/**
 * WUI Element Editor
 * @author Robert Koszewski
 */
public class ElementEditor extends Node{
	
	public ElementEditor() {
		addEventListener(new EventListener() {
			public void run(String eventID, String data) {
				if(eventID.equals("editor-update")) {
					setElementData("definition", data); // TODO: Check Data
				}
			}
		});
	}
	
	/**
	 * Get Definition
	 * @return
	 */
	public String getDefinition() {
		return getElementData("definition");
	}
	
	/**
	 * Load Definition
	 * @param definition
	 */
	public void setDefinition(String definition) {
		setElementData("definition", definition); // TODO: Check Data
	}

	/**
	 * Load Definition from URI
	 * @param definitionURI
	 * @throws IOException
	 */
	public void loadDefinition(URI definitionURI) throws IOException {
		setElementData("definition", IOUtils.toString(definitionURI, "UTF-8")); // TODO: Check Data
	}
	
	/**
	 * Save Element Definition to File
	 * @param file
	 */
	public void saveDefinition(File file) {
		try {
			FileUtils.writeStringToFile(file, getDefinition());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ElementTemplate getElementDefinition() {
		try {
			return new ElementTemplate(ElementEditor.class.getResourceAsStream("ElementEditor.def.json"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}

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

package com.robertkoszewski.wui.template;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.google.gson.annotations.SerializedName;

public class ElementTemplate {
	
	/*
	@SerializedName("html")
	private String html;
	
	@SerializedName("js-initialize")
	private String initialize_JS;
	
	@SerializedName("js-set-data")
	private String set_data_JS;
	*/
	
	private String element_definition;
	
	


	public ElementTemplate(InputStream is) throws IOException {
		this(IOUtils.toString(is, "UTF-8"));
	}
	
	public ElementTemplate(String element_definition) {
		this.element_definition = element_definition;
		
		return;
		
		/*
		Document doc = Jsoup.parse(element_definition, "", Parser.xmlParser());
		//Document doc = Jsoup.parse(element_definition, "");
		doc.outputSettings().indentAmount(0).prettyPrint(true); // Output Settings
		Element e;
		
		
		// Get HTML		
		if((e = doc.select("element > html > *").first()) != null) {
			this.html = e.outerHtml().replaceAll("\n","");
		}

		// Get Initialize JS		
		if((e = doc.select("element > initialize > script").first()) != null) {
			this.initialize_JS = e.wholeText();
		}

		
		// Get Set Data JS		
		if((e = doc.select("element > set-data > script").first()) != null) {
			this.set_data_JS = e.wholeText();
		}
		*/

	}
	
	/*
	public String getHTML() {
		return html;
	}
	
	public String getInitializeJS() {
		return initialize_JS;
	}
	
	public String getSetDataJS() {
		return set_data_JS;
	}
	
	*/
	
	public String getElementDefinition() {
		return element_definition;
	}
	
	/*
	public String toString() {
		return "HTML: " + html + " | INITIALIZE JS: " + initialize_JS + " | SET-DATA JS: " +set_data_JS; 
	}
	*/
}

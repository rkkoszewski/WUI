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

package com.robertkoszewski.wui.template.materialdesign;

import com.robertkoszewski.wui.core.ViewInstance;
import com.robertkoszewski.wui.server.ResourceManager;
import com.robertkoszewski.wui.template.BaseContent;
import com.robertkoszewski.wui.template.BasicContent;
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.template.WindowTemplate;

/**
 * Material Design Template
 * @author Robert Koszewski
 */
public class MaterialDesignTemplate implements WindowTemplate{
	
	private String title = "";

	public String getTemplateHTML(ResourceManager resources) {
		String html = resources.getResourceAsString(MaterialDesignTemplate.class.getResourceAsStream("template.html"));
		return (html != null ? html : "<html><body>ERROR: Unable to find Template</body></html>");
	}

	public String generateResponse(Content content, /*UIControl uicontrol,*/ long timestamp) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAppName(String title) {
		this.title = title;
	}

	public String getAppName() {
		return this.title;
	}

	public BaseContent<?, ?, ?> getContentInstance(ViewInstance viewInstance) {
		return new BasicContent(viewInstance);
	}

}

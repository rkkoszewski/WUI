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

package com.robertkoszewski.wui.templates;

import com.robertkoszewski.wui.server.ResourceManager;

/**
 * Window Template
 * @author Robert Koszewski
 */
public interface WindowTemplate {
	
	/**
	 * Returns the base HTML template
	 * @param resources
	 * @return
	 */
	String getTemplateHTML(ResourceManager resources);
	
	/**
	 * Renders a Content Response
	 * @param res 
	 * @param req 
	 * @param content
	 * @param uicontrol 
	 * @return
	 */
	public String generateResponse(Content content, /*UIControl uicontrol,*/ long timestamp);
	
	/**
	 * Set Window Title
	 * @param title
	 */
	public void setAppName(String title);
	
	/**
	 * Get Window Title
	 * @return
	 */
	public String getAppName();
	
	
	/**
	 * Get Content Instance
	 * @return 
	 */
	public Content getContentInstance();

}

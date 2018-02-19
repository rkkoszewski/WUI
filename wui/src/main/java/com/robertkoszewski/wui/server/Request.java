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

package com.robertkoszewski.wui.server;

import java.util.List;
import java.util.Map;

/**
 * Request Interface
 * @author Robert Koszewski
 */
public interface Request {
	// URL
	/**
	 * Get Request URL
	 * @return URL
	 */
	public String getURL();
	
	// Headers
	/**
	 * Get Headers
	 * @return
	 */
	public Map<String, String> getHeaders();
	
	/**
	 * Get Header
	 * @param id
	 * @return
	 */
	public String getHeader(String id);
	
	// Get Cookies
	
	/**
	 * Get all Cookies
	 * @return
	 */
	public Map<String, String> getCookies();
	
	/**
	 * Get Cookie
	 * @param id
	 * @return
	 */
	public String getCookie(String id);
	
	// POST Data
	public void getPostData();
	public void getPostBody();

	// GET Parameters
	/**
	 * Get all GET Parameters
	 */
	public Map<String, List<String>> getParameters();
	
	/**
	 * Get GET Parameter with all variants
	 */
	public List<String> getParameter(String id);
	
	/**
	 * Get first GET Parameter
	 */
	public String getFirstParameter(String id);
	
	// Remote Data
	public String getRemoteIpAddress();
}

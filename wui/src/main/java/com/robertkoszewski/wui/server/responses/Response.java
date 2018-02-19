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

package com.robertkoszewski.wui.server.responses;

import java.util.Map;

import com.robertkoszewski.wui.server.Cookie;

/**
 * Basic Response Type
 * @author Robert Koszewski
 */
public interface Response {
	
	/**
	 * Get Response Content Mime Type (Example: text/html, text/json, image/jpeg, image/png)
	 * @return MimeType
	 */
	public String getMimeType();
	
	/**
	 * Return Custom Headers
	 * @return Headers
	 */
	public Map<String, String> getHeaders();
	
	
	/**
	 * Return Cookies
	 * @return Cookies
	 */
	public Map<String, Cookie> getCookies();
	
	/**
	 * Set A Cookie
	 * @param id
	 * @param value
	 */
	public void setCookie(String key, Cookie cookie);

	/**
	 * Get RAW Response. This is called when the server doesn't support any other super-types.
	 * @return Raw Response
	 */
	public byte[] getResponse();
}

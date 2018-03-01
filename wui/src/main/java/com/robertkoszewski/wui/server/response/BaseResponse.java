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

package com.robertkoszewski.wui.server.response;

import java.util.HashMap;
import java.util.Map;

import com.robertkoszewski.wui.server.Cookie;

/**
 * Base Response Class
 * @author Robert Koszewski
 *
 */
public abstract class BaseResponse implements Response{
	
	private String mimeType = "text/html";
	private Map<String, Cookie> cookies = new HashMap<String, Cookie>();
	private Map<String, String> headers = new HashMap<String, String>();
	
	public BaseResponse(String mimeType) {
		this.mimeType = mimeType;
	}
	
	@Override
	public String getMimeType() {
		return this.mimeType;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public Map<String, Cookie> getCookies() {
		return cookies;
	}

	@Override
	public void setCookie(String key, Cookie cookie) {
		cookies.put(key, cookie);
	}
}

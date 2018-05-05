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

package com.robertkoszewski.wui.server.nanohttpd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.robertkoszewski.wui.server.Request;

import fi.iki.elonen.NanoHTTPD.CookieHandler;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;

/**
 * NanoHTTPD Compatible Request
 * @author Robert Koszewski
 */
public class NanoHTTPDDebugRequest implements Request{
	
	private final IHTTPSession session;

	public NanoHTTPDDebugRequest(IHTTPSession session) {
		this.session = session;
	}

	public String getURL() {
		return "/data";
	}

	public Map<String, String> getHeaders() {
		Map<String, String> m = new HashMap<String, String>();
		m.put("x-wui-request", "content");
		return m;
	}

	public String getHeader(String id) {
		return getHeaders().get(id);
	}
	
	// Cookies

	public Map<String, String> getCookies() {
		HashMap<String, String> cookies = new HashMap<String, String>();
		cookies.put("WUISESSIONID", "7370e284-5393-44c7-afac-7795ad8b32a1");
		return cookies;
	}

	public String getCookie(String id) {
		return session.getCookies().read(id);
	}
	
	// POST Parameters
	
	public void getPostData() {
		
		// TODO Auto-generated method stub
		
	}

	public String getPostBody() {
		return null;
	}
	
	// GET Parameters

	/**
	 * Get all GET Parameters
	 */
	public Map<String, List<String>> getParameters() {
		return session.getParameters();
	}
	
	/**
	 * Get GET Parameter with all variants
	 */
	public List<String> getParameter(String id) {
		return session.getParameters().get(id);
	}

	/**
	 * Get GET Parameter
	 */
	public String getFirstParameter(String id) {
		List<String> p = getParameter(id);
		return (p.size() == 0 ? null : p.get(0));
	}
	
	// Client Data
	
	public String getRemoteIpAddress() {
		return session.getRemoteIpAddress();
	}

}

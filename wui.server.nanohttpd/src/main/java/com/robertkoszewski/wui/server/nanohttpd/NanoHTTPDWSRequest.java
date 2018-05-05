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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.robertkoszewski.wui.server.Request;

import fi.iki.elonen.NanoHTTPD.CookieHandler;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.ResponseException;

/**
 * NanoHTTPD Compatible Request
 * @author Robert Koszewski
 */
public class NanoHTTPDWSRequest implements Request{
	
	private final IHTTPSession session;

	public NanoHTTPDWSRequest(IHTTPSession session) {
		this.session = session;
	}

	public String getURL() {
		return session.getUri();
	}

	public Map<String, String> getHeaders() {
		return Collections.unmodifiableMap(session.getHeaders());
	}

	public String getHeader(String id) {
		return session.getHeaders().get(id);
	}
	
	// Cookies

	public Map<String, String> getCookies() {
		// This is actually already internally a HashMap in NanoHTTPD but 
		// for whatever reason it's not accessible. Maybe that's their way
		// to make it read-only? We will never know.
		HashMap<String, String> cookies = new HashMap<String, String>();
		CookieHandler cookie_handler = session.getCookies();
		Iterator<String> cit = cookie_handler.iterator();
		while(cit.hasNext()) {
			String cookie_key = cit.next();
			cookies.put(cookie_key, cookie_handler.read(cookie_key)); 
		}
		return Collections.unmodifiableMap(cookies);
	}

	public String getCookie(String id) {
		return session.getCookies().read(id);
	}
	
	// POST Parameters
	
	public void getPostData() {
		
		// TODO Auto-generated method stub
		
	}

	public String getPostBody() {
		final HashMap<String, String> map = new HashMap<String, String>();
        try {
			session.parseBody(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return map.get("postData");
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

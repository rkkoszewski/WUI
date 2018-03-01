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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

/**
 * WebSocket Request
 * @author Robert Koszewski
 */
public class WSRequest implements Request{
	
	// Request Properties
	@Expose
	private String url;
	@Expose
	private Map<String, String> headers;
	@Expose
	private Map<String, List<String>> parameters;

	// Private Variables
	private Request handshake_request;
	
	// Constructor
	public WSRequest(Request request) {
		this();
		this.handshake_request = request;
	}
	
	public WSRequest() {
		headers = new HashMap<String, String>();
		parameters = new HashMap<String, List<String>>();
	}
	
	// WS Request Only Methods
	public void cloneRequest(Request request) {
		this.url = request.getURL();
		this.headers = request.getHeaders();
		this.parameters = request.getParameters();
	}
	
	public void setParameter(String id, String value) {
		List<String> values = new ArrayList<String>();
		values.add(value);
		parameters.put(id, values);
	}
	
	public void setParameter(String id, List<String> value) {
		parameters.put(id, value);
	}
	
	/**
	 * Set Original Request during Handshake
	 * @param handshake_request
	 */
	public void setHandshakeRequest(Request handshake_request) {
		this.handshake_request = handshake_request;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public String getHeader(String id) {
		return headers.get(id);
	}

	@Override
	public Map<String, String> getCookies() {
		//return request.getCookies();
		HashMap<String, String> cookies = new HashMap<String, String>();
		cookies.put("WUISESSIONID", "7370e284-5393-44c7-afac-7795ad8b32a1");
		return cookies;
	}

	@Override
	public String getCookie(String id) {
		return getCookies().get(id);
	}

	@Override
	public void getPostData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getPostBody() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, List<String>> getParameters() {
		return parameters;
	}

	@Override
	public List<String> getParameter(String id) {
		return parameters.get(id);
	}

	@Override
	public String getFirstParameter(String id) {
		return parameters.get(id).get(0);
	}

	@Override
	public String getRemoteIpAddress() {
		// TODO Auto-generated method stub
		return null;
	}
}

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

import java.util.Map;
import java.util.Vector;

import com.robertkoszewski.wui.server.Request;

import fi.iki.elonen.NanoHTTPD.IHTTPSession;

/**
 * NanoHTTPD Compatible Request
 * @author Robert Koszewski
 */
public class NanoHTTPDRequest implements Request{
	
	private final IHTTPSession session;

	public NanoHTTPDRequest(IHTTPSession session) {
		this.session = session;
	}

	public String getURL() {
		return session.getUri();
	}

	public Map<String, String> getHeaders() {
		return session.getHeaders();
	}

	public String getHeader(String id) {
		return session.getHeaders().get(id);
	}

	public void getPostData() {
		// TODO Auto-generated method stub
		
	}

	public void getPostBody() {
		// TODO Auto-generated method stub
		
	}

	public void getGetData() {
		// TODO Auto-generated method stub
		
	}

}

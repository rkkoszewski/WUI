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

/**
 * Cookie
 * @author Robert Koszewski
 */
public class Cookie {
	
	private String value;
	private int expiration;
	private boolean isSession = false;
	
	public Cookie(String value) {
		this.value = value;
		isSession = true;
	}
	
	public Cookie (String value, int expiration_in_days) {
		this(value);
		this.expiration = expiration_in_days;
	}
	
	public String getValue() {
		return value;
	}

	public int getExpiration() {
		return expiration;
	}
	
	public boolean isSessionCookie() {
		return isSession;
	}
	
	@Override
	public String toString() {
		return "VALUE: "+ value + " | EXPIRATION: " + expiration + " | SESSION: " + isSession;
	}
	
}

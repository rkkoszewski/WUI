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

package com.robertkoszewski.wui.core;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.robertkoszewski.wui.server.Cookie;
import com.robertkoszewski.wui.server.Request;
import com.robertkoszewski.wui.templates.WindowTemplate;

/**
 * Session Manager Class
 * @author Robert Koszewski
 */
public class SessionManager {

	// Static Variables
	private static final String WUI_SESSION_ID = "WUISESSIONID";
	
	protected final ContentManager content_manager;
	protected final WindowTemplate template;
	
	private Map<String, Session> sessions = new HashMap<String, Session>(); // TODO: Implement Session Cleanup Mechanism
	
	// Constructors
	public SessionManager(ContentManager content_manager, WindowTemplate template) {
		this.content_manager = content_manager;
		this.template = template;
	}
	
	// Methods
	
	public Session getSession(Request request, Map<String, Cookie> response_cookies) {
		
		// Has Session?
		String sessionID = request.getCookie(WUI_SESSION_ID);
		// @DEBUG System.out.println("CURRENT SESSION ID: " + sessionID);
		if(sessionID == null) {
			// Generate new Session ID
			sessionID = newSessionID();
			response_cookies.put(WUI_SESSION_ID, new Cookie(sessionID)); // Update Session ID Cookie
		}
		
		// Return Session
		
		Session session = sessions.get(sessionID);
		if(session == null) {
			session = new Session(this);
			sessions.put(sessionID, session);
		}
		
		return session;
	}
	
	// Methods
	
	/**
	 * Generate Session ID (TODO: Improve this and make it more secure)
	 * @return Session ID
	 */
	private String newSessionID() {
		return UUID.randomUUID().toString();
	}
}

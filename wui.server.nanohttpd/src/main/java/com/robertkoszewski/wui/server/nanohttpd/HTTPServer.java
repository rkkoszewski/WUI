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
import java.util.Map;

import com.robertkoszewski.wui.WUIController;

import fi.iki.elonen.NanoHTTPD;

/**
 * NanoHTTPD Based Web Server Implementation for WUI
 * @author Robert Koszewski
 */
public class HTTPServer extends NanoHTTPD{
	
	private Map<String, WUIController> pages;
	
	/*
	public App()  {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning! Point your browsers to http://localhost:8080/ \n");
    }
    */
	
	public HTTPServer(int port, Map<String, WUIController> pages) throws IOException {
		super(port);
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		this.pages = pages;
		System.out.println("SERVER STARTED AT PORT: " + port);
	}
	
	 @Override
     public Response serve(IHTTPSession session) {
		 System.out.println(session.getUri());
		 
		 WUIController controller = pages.get(session.getUri());
		 String response = "";;
		 
		 if(controller == null) response = "404 Not Found";
		 else response = controller.viewUpdate().getHTML();

		 return newFixedLengthResponse(response);
		 
		 /*
         String msg = "<html><body><h1>Hello server</h1>\n";
         Map<String, String> parms = session.getParms();
         if (parms.get("username") == null) {
             msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
         } else {
             msg += "<p>Hello, " + parms.get("username") + "!</p>";
         }
         
         */
         //return newFixedLengthResponse(msg + "</body></html>\n");
     }
	
}

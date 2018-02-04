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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.robertkoszewski.wui.server.ResponseManager;
import com.robertkoszewski.wui.server.responses.FileResponse;
import com.robertkoszewski.wui.server.responses.StringResponse;

import fi.iki.elonen.NanoHTTPD;

/**
 * NanoHTTPD Based Web Server Implementation for WUI
 * @author Robert Koszewski
 */
public class HTTPServer extends NanoHTTPD{
	
	private final ResponseManager responseManager;

	public HTTPServer(int port, ResponseManager responseManager) throws IOException {
		super(port);
		this.responseManager = responseManager;
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		System.out.println("SERVER STARTED AT PORT: " + port);
	}
	
	 @Override
     public Response serve(IHTTPSession session) {

		 // Get Response Object from the WUI Response Manager
		 com.robertkoszewski.wui.server.responses.Response responseObject = responseManager.getResponse(new NanoHTTPDRequest(session));
		 
		 // Response Variable
		 Response response;
		 
		 // Check for Response Types
		 if(responseObject == null) {
			 response = newFixedLengthResponse("ERROR: An unexpected server error occured. CAUSE: Unexpected response. Response is NULL.");
			 
		 }else if(responseObject instanceof StringResponse) { // String Response
			 response = newFixedLengthResponse(Response.Status.OK, responseObject.getContentType(), ((StringResponse) responseObject).getStringResponse());

		 }else if(responseObject instanceof FileResponse){ // File Response
			 response = newChunkedResponse(Response.Status.OK, responseObject.getContentType(), ((FileResponse) responseObject).getInputStream());
			 
		 }else { // Generic or Unknown Response Type
			 response = newChunkedResponse(Response.Status.OK, responseObject.getContentType(), new ByteArrayInputStream(responseObject.getResponse()));
			 
		 }
		 
		 return response; // Serve Response
		 
		 
		 
		 /*
		 boolean content_request = session.getHeaders().get("x-wui-content-request").length() != 0;
		 String response = template.getTemplateHTML(resources);
		 */
		 
		 
		 /*
		 return new Response(Response.Status.OK, "image/jpeg", fis, -1);
		 
		 return newFixedLengthResponse(response);
		 */
		 
		 /*
		 
		 WUIController controller = pages.get(session.getUri());
		 String response = "";;
		 
		 if(controller == null) response = "404 Not Found";
		 else response = controller.viewUpdate().getHTML();

		 return newFixedLengthResponse(response);
		 
		 */
		 
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

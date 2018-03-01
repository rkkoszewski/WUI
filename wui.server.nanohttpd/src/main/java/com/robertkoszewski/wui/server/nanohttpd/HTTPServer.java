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
import java.nio.charset.CharacterCodingException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.robertkoszewski.wui.server.Request;
import com.robertkoszewski.wui.server.RequestResponse;
import com.robertkoszewski.wui.server.ResponseManager;
import com.robertkoszewski.wui.server.response.FileResponse;
import com.robertkoszewski.wui.server.response.StringResponse;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoWSD;
import fi.iki.elonen.NanoWSD.WebSocketFrame.CloseCode;

/**
 * NanoHTTPD Based Web Server Implementation for WUI
 * @author Robert Koszewski
 */
public class HTTPServer extends NanoWSD{
	
	private final ResponseManager responseManager;
	
	public HTTPServer(int port, ResponseManager responseManager) throws IOException {
		super(port);
		this.responseManager = responseManager;
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		System.out.println("SERVER STARTED AT PORT: " + port);
	}
	
	 @Override
	 public Response serveHttp(IHTTPSession session) {
	
		 // Get Response Object from the WUI Response Manager
		 com.robertkoszewski.wui.server.response.Response responseObject = responseManager.getResponse(new NanoHTTPDRequest(session));
		 
		 // Response Variable
		 Response response;
		 
		 // Check for Response Types
		 if(responseObject == null) {
			 response = newFixedLengthResponse("ERROR: An unexpected server error occured. CAUSE: Unexpected response. Response is NULL.");
			 
		 }else if(responseObject instanceof StringResponse) { // String Response
			 response = newFixedLengthResponse(Response.Status.OK, responseObject.getMimeType(), ((StringResponse) responseObject).getStringResponse());
	
		 }else if(responseObject instanceof FileResponse){ // File Response
			 response = newChunkedResponse(Response.Status.OK, responseObject.getMimeType(), ((FileResponse) responseObject).getInputStream());
			 
		 }else { // Generic or Unknown Response Type
			 response = newChunkedResponse(Response.Status.OK, responseObject.getMimeType(), new ByteArrayInputStream(responseObject.getResponse()));
			 
		 }
		 
		 // Serve Cookies
		 Iterator<Entry<String, com.robertkoszewski.wui.server.Cookie>> roit = responseObject.getCookies().entrySet().iterator();
		 while(roit.hasNext()) {
			 Entry<String, com.robertkoszewski.wui.server.Cookie> cookie_entry = roit.next();
			 com.robertkoszewski.wui.server.Cookie cookie = cookie_entry.getValue();
			 session.getCookies().set(cookie_entry.getKey(), cookie.getValue(), (cookie.isSessionCookie() ? 9999999 : cookie.getExpiration()));
		 }
		 
		 return response; // Serve Response
	}
	
	 
	 
	@Override
	protected WebSocket openWebSocket(IHTTPSession handshake) {
		return new DebugWebSocket(responseManager, handshake);
	}
	
	private static class DebugWebSocket extends WebSocket {

		private final ResponseManager responseManager;
		private final IHTTPSession handshakeRequest;

        public DebugWebSocket(ResponseManager responseManager, IHTTPSession handshakeRequest) {
            super(handshakeRequest);
            this.handshakeRequest = handshakeRequest;
            this.responseManager = responseManager;
        }

        @Override
        protected void onOpen() {
        	System.out.println("WS OPENED");
        }

        @Override
        protected void onClose(CloseCode code, String reason, boolean initiatedByRemote) {
                System.out.println("C [" + (initiatedByRemote ? "Remote" : "Self") + "] " + (code != null ? code : "UnknownCloseCode[" + code + "]")
                        + (reason != null && !reason.isEmpty() ? ": " + reason : ""));
        }
        
        private Thread contentThread = null;

        @Override
        protected void onMessage(final WebSocketFrame message) {
        	System.out.println("GOT IT!");

        	String message_content = message.getTextPayload();
        	
        	if(message_content.equals("")) return; // IT's just a ping
        	
        	final RequestResponse requestResponse = responseManager.getWebSocketResponse(message_content);
        	
        	// Content Request
        	if(requestResponse.isContentRequest()) {
        		
        		if(contentThread != null) {
            		contentThread.interrupt();
            	}
        		
        		// Content Thread
            	contentThread = new Thread() {
            		
        			@Override
        			public void run() {
        				int i = 1;
        				String payload = message.getTextPayload();
        				Request request = requestResponse.getRequest();
        				try {
    	    				while(!this.isInterrupted()) {
    	    					System.out.println("ROUND: " + i++);

    	    	                // Get Response
    	    	                com.robertkoszewski.wui.server.response.Response responseObject;
    	    	                
    	    	                if(request == null) {
    	    	                	RequestResponse requestResponse = responseManager.getWebSocketResponse(payload);
    	    	                	responseObject = requestResponse.getResponse();
    	    	                	request = requestResponse.getRequest();
    	    	                }else {
    	    	                	RequestResponse requestResponse = responseManager.getWebSocketResponse(request);
    	    	                	responseObject = requestResponse.getResponse();
    	    	                	request = requestResponse.getRequest();
    	    	                }

    	    	                if(!this.isInterrupted())
    	    	                	sendResponse(responseObject, message);
    	    				}
    	    				
        				} catch (Exception e) {
        	            	System.err.println("RUNTIME EXCEPTION");
        	                throw new RuntimeException(e);
        	            }
        				System.err.println("THREAD JUST FINISHED");
        			}
                	
                };
                
                // Send Response
                try {
					sendResponse(requestResponse.getResponse(), message);
					contentThread.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
                
        	}else {
        		// Any other request
        		System.out.println("SENDING OTHER REQUEST");
        		try {
					sendResponse(requestResponse.getResponse(), message);
				} catch (Exception e) {
					e.printStackTrace();
				}

        	}
        	
        	
        	
        	
        	
        	
        	
        	/*
            try {
                message.setUnmasked();
                com.robertkoszewski.wui.server.response.Response responseObject = responseManager.getWebSocketResponse(message.getTextPayload());
                
            	if(responseObject == null) {
            		message.setTextPayload("ERROR: An unexpected server error occured. CAUSE: Unexpected response. Response is NULL.");

	       		 }else if(responseObject instanceof StringResponse) { // String Response
	       			message.setTextPayload(((StringResponse) responseObject).getStringResponse());
	       			// }else if(responseObject instanceof FileResponse){ // File Response
	       			// response = newChunkedResponse(Response.Status.OK, responseObject.getMimeType(), ((FileResponse) responseObject).getInputStream());
	       			 
	       		 }else { // Generic or Unknown Response Type
	       			message.setBinaryPayload(responseObject.getResponse());
	       		 }
               
            	sendFrame(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            */
        }
        
        /**
         * Send Response
         * @param responseObject
         * @param message
         * @throws Exception 
         */
        protected void sendResponse(com.robertkoszewski.wui.server.response.Response responseObject, WebSocketFrame message) throws Exception {
        	message.setUnmasked();
        	
        	if(responseObject == null) {
        		message.setTextPayload("ERROR: An unexpected server error occured. CAUSE: Unexpected response. Response is NULL.");
        		sendFrame(message);
        		throw new Exception("ERROR: An unexpected server error occured. CAUSE: Unexpected response. Response is NULL.");

       		}else if(responseObject instanceof StringResponse) { // String Response
       			message.setTextPayload(((StringResponse) responseObject).getStringResponse());
       			
   			// }else if(responseObject instanceof FileResponse){ // File Response
   			// response = newChunkedResponse(Response.Status.OK, responseObject.getMimeType(), ((FileResponse) responseObject).getInputStream());
   			 
       		}else { // Generic or Unknown Response Type
       			message.setBinaryPayload(responseObject.getResponse());
       		}

        	sendFrame(message);
        }
        
        @Override
        protected void onException(IOException exception) {
        	System.err.println("exception occured" + exception.getMessage());
        }
        
        // Non Used Methods

        @Override
        protected void onPong(WebSocketFrame pong) {
        	// Do nothing
        }

        @Override
        protected void debugFrameReceived(WebSocketFrame frame) {
        	// Do nothing
        }

        @Override
        protected void debugFrameSent(WebSocketFrame frame) {
        	// Do nothing
        }
    }
	
	
}

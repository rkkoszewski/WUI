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

package com.robertkoszewski.wui.renderer.javafx;

import java.awt.Dimension;

import com.robertkoszewski.wui.renderer.Renderer;
import com.robertkoszewski.wui.renderer.Window;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * JavaFX Renderer
 * @author Robert Koszewski
 */
public class JavaFXRenderer implements Renderer{

	public Window open(String url, String title, Dimension window_size, boolean window_maximized, String icon_path, Runnable onCloseAction) {
			// Start JavaFX Subsystem
			JavaFXSubsystem.initialize();

			// Start JavaFX WebView Browser
			Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			    	
			    	// Start JavaFX Window
			    	final Stage stage = new Stage(); // Initialize Stage
			    	stage.setTitle(title); // Set Title
			        //if(icon_path!=null) stage.getIcons().add(Utils.getFXImage(icon_path)); // Set Icon	
			        
			        // ## Use JavaFX WebView ## (Seems to be a RAM Hog and is very Buggy) 
			        WebView webview = new WebView();
			        
			        // OPTIMIZATIONS +++ START +++
			        webview.getEngine().getHistory().setMaxSize(0); // OPTIMIZATION: Should cut down RAM usage
			        webview.setCache(false);
			        // OPTIMIZATIONS +++ END +++
			        
			        webview.getEngine().load(url);
			        
			        // Set Window Size
			        if(window_size==null)
			        	stage.setScene(new Scene(webview, 650, 450)); // Start Browser Frame
		            else
		            	stage.setScene(new Scene(webview, window_size.getWidth(), window_size.getHeight())); // Start Browser Frame

		            // Set Maximized State
		    		if(window_maximized)
		    			stage.setMaximized(true);
	  
			    	// Set On Close Action
			        if(onCloseAction!=null){ // Don't add if not used
				        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				            public void handle(WindowEvent we) {
				            	onCloseAction.run();
				            	
				            }
				        });   
			        }
			        
			        // Shutdown System when Closing App
			        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			            public void handle(WindowEvent we) {
			            	System.exit(0);
			            }
			        });   
			        
			        // Show Window
			    	stage.show();
			    	stage.toFront();
			    }
			});
			
			return new Window() {

				@Override
				public void close() {
					//stage.close();
					JavaFXSubsystem.shutdown(false);
				}
				
			};
	}

}

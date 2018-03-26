/*******************************************************************************
 * Copyright (c) 2018 Robert Koszewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.robertkoszewski.wui.renderer.javafx;

import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Initialize JavaFX Subsystem
 * @author Robert Koszewski
 */
public class JavaFXSubsystem extends Application {
	
	private static JavaFXSubsystem instance = null;
	private static boolean isInitialized = false;
	private static CountDownLatch latch = null;
	
	/**
	 * Start JavaFX Application Thread
	 */
	synchronized public static boolean initialize(){

		// Single Instance
		if(instance == null){ 
			
			latch = new CountDownLatch(1);
			isInitialized = true;

			new Thread() {
	            @Override
	            public void run() {
	            	try {
	            		Application.launch(JavaFXSubsystem.class);
	            		// System.out.println("JavaFX Subsystem is Closing");
	            		
	            	} catch(Exception e){
	            		e.printStackTrace();
	            		isInitialized = false;
	            		latch.countDown();
	            		
	            	}
	            }
	        }.start();
	        
	        try {
				latch.await(); // Wait for the JavaFX Toolkit to Start
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				
			}
	        
	        return isInitialized;
			
		}else{
			// JavaFX Application is already initialized (Do nothing)
			return true;
			
		}

	}
	
	/**
	 * Shutdown JavaFX Application (PLEASE NOTE: You won't be able to run the Application again)
	 */
	public static void shutdown(boolean applicationOnly){

		if(applicationOnly){
			try {
				instance.stop(); // Stop Application Instance
				latch = null;
				instance = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}else{
			Platform.exit(); // Shutdown all JavaFX Instances
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		instance = this;
		latch.countDown();
	}

}

/*******************************************************************************
 * Copyright (c) 2016 Robert Koszewski
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
package com.robertkoszewski.wui.renderer;

import java.awt.Desktop;
import java.awt.Dimension;
import java.net.URI;

import javax.swing.JOptionPane;

/**
 * Open Native Browser
 * @author Robert Koszewski
 */
public class RendererNative implements Renderer{
	
	private static boolean isNotified = false;

	public Window open(String url, String title, Dimension window_size, boolean window_maximized, String icon_path, Runnable onCloseAction) {
		boolean isOpen = true;
		
		if (Desktop.isDesktopSupported()) {
            // Windows
            try {
            	if(onCloseAction!=null){		      	
	            	if(!isNotified)
	            		JOptionPane.showMessageDialog(null, "Your default browser will be used as UI. The server has to be shut down manually.", "Server need manual shutdown", JOptionPane.INFORMATION_MESSAGE);
	            	isNotified = true; // Only show this message once
            	}
            	Desktop.getDesktop().browse(new URI(url));
			} catch (Exception e) {
				isOpen = false;
				e.printStackTrace();
			}
            
        }else{
        	isOpen = false;
        }
		
		if(!isOpen){
			JOptionPane.showMessageDialog(null, "Could not open browser interface. Please go manually to the URL: "+url, "Error Opening Browser Interface", JOptionPane.ERROR_MESSAGE);
		}
		
		
		return new WindowNative();
	}
	
	/**
	 * Window Implementation
	 * @author Robert Koszewski
	 */
	public class WindowNative implements Window{
		public void close() {
			JOptionPane.showMessageDialog(null, "Trying to close browser window. This has to be performed manually by the user.", "Close Browser Manually", JOptionPane.ERROR_MESSAGE);
		}
	}

}

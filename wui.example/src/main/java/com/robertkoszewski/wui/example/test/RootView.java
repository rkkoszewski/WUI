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

package com.robertkoszewski.wui.example.test;

import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.element.Button;
import com.robertkoszewski.wui.element.Label;
import com.robertkoszewski.wui.templates.Content;
import com.robertkoszewski.wui.utils.StringUtils;
import com.robertkoszewski.wui.utils.SystemInfo;

/**
 * Demo Root View
 * @author Robert Koszewski
 */
public class RootView extends View{

	public RootView() {
		super(Type.GLOBAL);
	}

	public void createView(final Content content) {
		// Set Data (Pretty much like variables)
		content.setData("press", 0);
		content.setData("i", 0);
		
		final Label label = new Label();
		final Label label_press = new Label("BUTTON NEVER PRESSED");
		
		final Label cpu_label = new Label();
		final Label ram_label = new Label();
		
		content.addElement(label);
		content.addElement(label_press);
		content.addElement(cpu_label);
		content.addElement(ram_label);
		
		/*
		content.createSharedElement("cpu_label", Label.class, 1, 2);
		
		content.createSharedElement("cpu_label", Label.class, new ElementCreated{
			
		} 1, 2);
		*/
		
		// Button Test
		Button b = new Button("Press Me!!");
		b.addActionListener(new Runnable() {
			public void run() {
				int press = content.getData("press", Integer.class);
				label_press.setText("COUNTING PRESSES: " + press++ );
				content.setData("press", press);
			}
		});
		
		// Controller Logic
		
		// System Info Thread
		final SystemInfo info = new SystemInfo();
		
		new Thread() {
			public void run() {
				while(true) {
					System.out.println("# Updating System Info");
					cpu_label.setText("Total RAM: "+StringUtils.readableFileSize(info.totalMem()));
					ram_label.setText("Used RAM: "+StringUtils.readableFileSize(info.usedMem()));
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Wait 3 Seconds
					
					int i = content.getData("i", Integer.class);
					label.setText("IT WORKS. COUNTING: " + i++); // And a counter
					content.setData("i", i);
				}
				
			};
		}.start();
		
		content.addElement(b);
	}
	
	
}

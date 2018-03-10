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
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.ui.element.Button;
import com.robertkoszewski.wui.ui.element.Label;
import com.robertkoszewski.wui.ui.layout.BorderLayout;
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
		
		// Add BorderLayout
		final BorderLayout layout = new BorderLayout();
		content.addElement(layout);
		
		
		// Set Data (Pretty much like variables)
		content.setSharedData("press", 0);
		content.setSharedData("i", 0);
		content.setSharedData("x", 0);
		
		final Label label = new Label();
		final Label label_press = new Label("BUTTON NEVER PRESSED");
		
		final Label cpu_label = new Label();
		final Label ram_label = new Label();

		layout.addElement(cpu_label, BorderLayout.Position.west);
		layout.addElement(ram_label, BorderLayout.Position.west);

		/*
		content.createSharedElement("cpu_label", Label.class, 1, 2);
		
		content.createSharedElement("cpu_label", Label.class, new ElementCreated{
			
		} 1, 2);
		*/
		
		// Button Test
		layout.addElement(label_press, BorderLayout.Position.east);
		Button b = new Button("Press Me!! (Counter)");
		b.addActionListener(new Runnable() {
			public void run() {
				int press = content.getSharedData("press", Integer.class);
				label_press.setText("COUNTING PRESSES: " + press++ );
				content.setSharedData("press", press);
			}
		});
		layout.addElement(b, BorderLayout.Position.east);

		
		
		// Controller Logic
		
		// System Info Thread & Realtime Counter
		final SystemInfo info = new SystemInfo();
		
		final Thread cthread = new Thread() {
			public void run() {
				while(!interrupted()) {
					System.out.println("# Updating System Info");
					cpu_label.setText("Total RAM: "+StringUtils.readableFileSize(info.totalMem()));
					ram_label.setText("Used RAM: "+StringUtils.readableFileSize(info.usedMem()));
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return;
					} // Wait 3 Seconds
					
					int i = content.getSharedData("i", Integer.class);
					label.setText("IT WORKS. COUNTING: " + i++); // And a counter
					content.setSharedData("i", i);
				}
				
			};
		};
		
		layout.addElement(label, BorderLayout.Position.center);
		
		// Realtime Counter
		Button bstart = new Button("Start Realtime Counter");
		bstart.addActionListener(new Runnable() {
			public void run() {
				if(!cthread.isAlive()) {
					cthread.start();
					System.out.println("Counter Started");
				}else{
					cthread.interrupt();
					System.out.println("Counter Stopped");
				}
			}
		});
		layout.addElement(bstart, BorderLayout.Position.center);

		
		// Button Test
		layout.addElement(new Label("--- ADD A NEW ELEMENT DYNAMICALLY ---"), BorderLayout.Position.center);
		Button addbtn = new Button("Add new Node");
		addbtn.addActionListener(new Runnable() {
			public void run() {
				int x = content.getSharedData("x", Integer.class);
				layout.addElement(new Label("Adding new dynamic element " + x), BorderLayout.Position.east);
				content.setSharedData("x", x);
			}
		});
		layout.addElement(addbtn, BorderLayout.Position.center);
	}
	
	
}

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

import com.robertkoszewski.wui.WUIController;
import com.robertkoszewski.wui.WUIView;
import com.robertkoszewski.wui.elements.Button;
import com.robertkoszewski.wui.elements.Label;
import com.robertkoszewski.wui.templates.Content;
import com.robertkoszewski.wui.templates.WindowTemplate;
import com.robertkoszewski.wui.utils.StringUtils;
import com.robertkoszewski.wui.utils.SystemInfo;

public class RootController implements WUIController{

	private RootView view;
	int i = 1;
	
	public void initialize(WindowTemplate template) {
		view = new RootView(template.getContentInstance());
		
		final SystemInfo info = new SystemInfo();
		
		new Thread() {
			public void run() {
				while(true) {
					System.out.println("# Updating System Info");
					view.setCPU("Total RAM: "+ StringUtils.readableFileSize(info.totalMem()));
					view.setRAM("Used RAM: "+ StringUtils.readableFileSize(info.usedMem()));
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Wait 3 Seconds
				}
				
			};
			
			
		}.start();
	}

	public Content viewUpdate() {
		view.setText("IT WORKS. COUNTING: " + i++);
		
		return view.getContent();
	}
	
	public class RootView implements WUIView{

		private Content content;
		private Label label;
		private Label cpu_label;
		private Label ram_label;
		private int press = 0;
		
		public RootView(Content content) {
			this.content = content;
			label = new Label();
			final Label label_press = new Label("BUTTON NEVER PRESSED");
			cpu_label = new Label();
			ram_label = new Label();
			content.addElement(label);
			content.addElement(label_press);
			content.addElement(cpu_label);
			content.addElement(ram_label);
			
			// Button Test
			Button b = new Button("Press Me!!");
			b.addActionListener(new Runnable() {
				public void run() {
					label_press.setText("COUNTING PRESSES: " + press++);
				}
			});
			
			content.addElement(b);
		}
		
		public void setText(String text) {
			label.setText(text);
		};
		
		public void setCPU(String text) {
			cpu_label.setText(text);
		};
		
		public void setRAM(String text) {
			ram_label.setText(text);
		};

		public Content getContent() {
			return content;
		}
		
	}

}

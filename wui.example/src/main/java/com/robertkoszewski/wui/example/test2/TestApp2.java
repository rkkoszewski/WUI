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

package com.robertkoszewski.wui.example.test2;

import com.robertkoszewski.wui.Preferences;
import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.WUIEngine;
import com.robertkoszewski.wui.core.EventListener;
import com.robertkoszewski.wui.server.ServerNotFoundException;
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.template.materialdesign.MaterialDesignTemplate;
import com.robertkoszewski.wui.ui.element.Button;
import com.robertkoszewski.wui.ui.element.Label;
import com.robertkoszewski.wui.ui.element.TextInput;
import com.robertkoszewski.wui.ui.layout.BorderLayout;

public class TestApp2 {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ServerNotFoundException {
		
		System.setProperty("wui.renderer", "javafx");
		
		WUIEngine window = new WUIEngine(new MaterialDesignTemplate());
		
		// Root View
		window.addView("/", new View(View.Scope.SHARED) {

			public void createView(Content content) {
				
				// Container
				final BorderLayout container = new BorderLayout();
				content.addElement(container);
				
				// Left Menu
				container.addElement(new Label("Add Note Here:"), BorderLayout.Position.west);
				
				final TextInput input = new TextInput();
				container.addElement(input, BorderLayout.Position.west);
				
				Button btn = new Button("Add Note");
				container.addElement(btn, BorderLayout.Position.west);
				
				btn.addEventListener(new EventListener() {
					public void run(String eventID, String data) {

						container.addElement(new Label(input.getValue()), BorderLayout.Position.center);
						input.setValue("");
						
					}
				});
	
			}
			
		});
		
		// Open Window
		window.showView();

	}
	
}

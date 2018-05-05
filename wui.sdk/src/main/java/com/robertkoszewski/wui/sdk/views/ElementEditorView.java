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

package com.robertkoszewski.wui.sdk.views;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.Vector;

import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.core.EventListener;
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.ui.element.Button;
import com.robertkoszewski.wui.ui.element.Label;
import com.robertkoszewski.wui.ui.element.Node;
import com.robertkoszewski.wui.ui.layout.BorderLayout;

import elements.ElementEditor;

public class ElementEditorView extends View {

	public ElementEditorView(Type type) {
		super(type);
	}
	
	static int i = 0;
	static int e = 1;
	static boolean left = true;

	public void createView(Content content) {
		final BorderLayout layout = new BorderLayout();
		content.addElement(layout);
		
		
		// Element Editor
		
		ElementEditor editor = new ElementEditor();
		
		layout.addElement(editor, BorderLayout.Position.center);
		
		/*
		
		final Label label = new Label("IT WORKS ");
		layout.addElement(label, BorderLayout.Position.north); // Label Button
		
		Button button = new Button("PRESS ME");
		button.addEventListener(new EventListener() {
			public void run(String eventID, String data) {
				label.setText("BUTTON PRESSED: "+ i++);
				
				if(left) {
					//layout.removeElement(label);
					layout.addElement(label, BorderLayout.Position.east); // Label Button
					left = false;
				}else {
					//layout.removeElement(label);
					layout.addElement(label, BorderLayout.Position.north); // Label Button
					left = true;
				}
			}
		});
		layout.addElement(button, BorderLayout.Position.south);
		
		
		// Dynamic Add Remove
		
		final Queue<Node> den = new ArrayDeque<Node>();
				
		Button button2 = new Button("CREATE NEW ELEMENT");
		button2.addEventListener(new EventListener() {
			public void run(String eventID, String data) {
				Label nl = new Label("NEW ELEMENT " + e++);
				den.add(nl);
				layout.addElement(nl, BorderLayout.Position.center);;
			}
		});
		layout.addElement(button2, BorderLayout.Position.south);
		
		Button button3 = new Button("REMOVE ELEMENT");
		button3.addEventListener(new EventListener() {
			public void run(String eventID, String data) {
				
				if(!den.isEmpty()) {
					Node ele = den.poll();
					layout.removeElement(ele);
				}
			}
		});
		layout.addElement(button3, BorderLayout.Position.south);
		*/
	}
}

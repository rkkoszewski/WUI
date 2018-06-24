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

package com.robertkoszewski.wui.example.collaboration;

import java.io.File;

import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.core.EventListener;
import com.robertkoszewski.wui.example.pictureviewer.elements.FolderElement;
import com.robertkoszewski.wui.example.pictureviewer.elements.PictureElement;
import com.robertkoszewski.wui.example.pictureviewer.elements.PictureViewer;
import com.robertkoszewski.wui.example.pictureviewer.elements.ReturnElement;
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.template.ElementTemplate;
import com.robertkoszewski.wui.ui.element.Button;
import com.robertkoszewski.wui.ui.element.Container;
import com.robertkoszewski.wui.ui.element.Label;
import com.robertkoszewski.wui.ui.element.Node;
import com.robertkoszewski.wui.ui.layout.BorderLayout;

/**
 * An Example Picture Viewer
 * @author Robert Koszewski
 *
 */
public class CollaborationView extends View{

	public CollaborationView(Scope scope) {
		super(scope);
	}

	public void createView(Content content) {
		
		// Base Layout
		BorderLayout layout = new BorderLayout();
		content.addElement(layout);
		
		// Picture Viewer
		final Editor editor = new Editor();
		layout.addElement(editor, BorderLayout.Position.center);
		
		// Control Buttons
		Container controls = new Container();
		layout.addElement(controls, BorderLayout.Position.west);
		
		controls.addElement(new Label("Save it now"));
		
		Button save_btn = new Button("Save");
		controls.addElement(save_btn);
		
		save_btn.addEventListener(new EventListener() {
			
			public void run(String eventID, String data) {
				if(eventID.equals("click")) {
					editor.save();
				}
			}
		});
		
		Button cancel_btn = new Button("Cancel");
		controls.addElement(cancel_btn);
		
		cancel_btn.addEventListener(new EventListener() {
			
			public void run(String eventID, String data) {
				if(eventID.equals("click")) {
					editor.reset();
					
				}
			}
		});

	}
	
	
	
	class Editor extends Node{
		
		public void save(){}
		
		public void reset(){}

		public ElementTemplate getElementDefinition() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	}
}

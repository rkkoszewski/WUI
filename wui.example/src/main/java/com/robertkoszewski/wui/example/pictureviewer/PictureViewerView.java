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

package com.robertkoszewski.wui.example.pictureviewer;

import java.io.File;

import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.core.EventListener;
import com.robertkoszewski.wui.example.pictureviewer.elements.FolderElement;
import com.robertkoszewski.wui.example.pictureviewer.elements.PictureElement;
import com.robertkoszewski.wui.example.pictureviewer.elements.PictureViewer;
import com.robertkoszewski.wui.example.pictureviewer.elements.ReturnElement;
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.ui.layout.BorderLayout;

/**
 * An Example Picture Viewer
 * @author Robert Koszewski
 *
 */
public class PictureViewerView extends View{

	public PictureViewerView(Scope scope) {
		super(scope);
	}

	public void createView(Content content) {
		// Base Layout
		BorderLayout layout = new BorderLayout();
		content.addElement(layout);
		
		// Picture Viewer
		PictureViewer viewer = new PictureViewer();
		layout.addElement(viewer, BorderLayout.Position.center);
		
		// Show Pictures
		ShowPictures(viewer, new File("D:\\Users\\rkkos\\Pictures"));
		
	}
	
	public void ShowPictures(final PictureViewer layout, final File folder) {
		layout.clearElements(); // Clear Elements
		
		// Back Button
		ReturnElement backButton = new ReturnElement();
		layout.addElement(backButton);
		
		// Button Action
		backButton.addEventListener(new EventListener() {
			public void run(String eventID, String data) {
				System.out.println("Clicked on: GO BACK");
				File parent = folder.getAbsoluteFile().getParentFile();
				if(parent == null) {
					System.out.println("CANNOT GO BACK!!! " + folder.getAbsolutePath());
				}else {
					System.out.println("SWITCHING TO PARENT FOLDER: " + parent.getAbsolutePath());
					ShowPictures(layout, parent);
				}
			}
		});
		
		// List Folders
		File[] listOfFiles = folder.listFiles();
		for(final File file: listOfFiles) {
			
			System.out.println("SCANNING FILE: " + file.getName());
			if(!file.isDirectory() && !file.getName().toLowerCase().endsWith(".jpg")) continue; // Ignore Unsupproted Files

			if(file.isDirectory()) {
				// Directory
				FolderElement pictureElement = new FolderElement(file.getName());
				layout.addElement(pictureElement);
				// Button Action
				pictureElement.addEventListener(new EventListener() {
					public void run(String eventID, String data) {
						System.out.println("Clicked on: " + file.getName());
						ShowPictures(layout, file);
					}
				});
				
			}else {
				// Picture
				PictureElement pictureElement = new PictureElement(file.getName());
				pictureElement.setImage(file);
				layout.addElement(pictureElement);
				
			}
		}
		
	}

}

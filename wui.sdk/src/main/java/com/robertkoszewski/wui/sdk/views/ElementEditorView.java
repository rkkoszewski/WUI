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

import java.io.File;
import java.io.IOException;

import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.core.EventListener;
import com.robertkoszewski.wui.sdk.elements.ElementEditor;
import com.robertkoszewski.wui.sdk.elements.FileButton;
import com.robertkoszewski.wui.sdk.elements.FileListContainer;
import com.robertkoszewski.wui.sdk.elements.FolderButton;
import com.robertkoszewski.wui.sdk.elements.SimpleFileButton;
import com.robertkoszewski.wui.structs.HardReference;
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.ui.element.Button;
import com.robertkoszewski.wui.ui.element.Label;
import com.robertkoszewski.wui.ui.layout.BorderLayout;

public class ElementEditorView extends View {

	public ElementEditorView(Scope type) {
		super(type);
	}

	/**
	 * Create Editor View
	 */
	public void createView(Content content) {
		// Current File Reference
		final HardReference<File> currentFile = new HardReference<File>(null);
		
		// Build UI
		final BorderLayout layout = new BorderLayout();
		content.addElement(layout);

		
		// Element Editor
		final ElementEditor editor = new ElementEditor();
		layout.addElement(editor, BorderLayout.Position.center);

		// Left Sidebar
		BorderLayout sidebar = new BorderLayout();
		layout.addElement(sidebar, BorderLayout.Position.west);

		// Save Button
		Button save = new Button("Save");
		sidebar.addElement(save, BorderLayout.Position.north);
		
		save.addEventListener(new EventListener() {
			public void run(String eventID, String data) {
				File file = currentFile.getValue();
				if(file == null)
					System.out.println("Cannot save file. No file is loaded");
				else
					editor.saveDefinition(file);
			}
		});

		// File Browser
		FileListContainer fileList = new FileListContainer();
		sidebar.addElement(fileList, BorderLayout.Position.center);
		
		ListFiles(new File("."), fileList, editor, currentFile);
	}

	/**
	 * List Files
	 * @param folder
	 * @param sidebar
	 * @param editor 
	 * @param currentFile 
	 */
	public void ListFiles(final File folder, final FileListContainer list, final ElementEditor editor, final HardReference<File> currentFile) {
		// Clear Previous List
		list.clearElements();

		// Label
		list.addElement(new Label("Files"));
		
		// Back Button
		Button backButton = new SimpleFileButton("...");
		list.addElement(backButton);
		
		// Button Action
		backButton.addEventListener(new EventListener() {
			public void run(String eventID, String data) {
				System.out.println("Clicked on: GO BACK");
				File parent = folder.getAbsoluteFile().getParentFile();
				if(parent == null) {
					System.out.println("CANNOT GO BACK!!! " + folder.getAbsolutePath());
				}else {
					System.out.println("SWITCHING TO PARENT FOLDER: " + parent.getAbsolutePath());
					ListFiles(parent, list, editor, currentFile);
				}
			}
		});
		
		// List Folders
		File[] listOfFiles = folder.listFiles();
		for(final File file: listOfFiles) {
			if(!file.isDirectory() && !file.getName().endsWith(".def.json")) continue; // Ignore Unsupproted Files

			Button fileLabel = file.isDirectory() ? new FolderButton(file.getName()) : new FileButton(file.getName());
			//Button fileLabel = new Button(file.getName());
			list.addElement(fileLabel);
			
			// Button Action
			fileLabel.addEventListener(new EventListener() {

				public void run(String eventID, String data) {
					System.out.println("Clicked on: " + file.getName());
					
					if(file.isDirectory()) {
						// Open Directory
						ListFiles(file, list, editor, currentFile);
						
					}else {
						// Load File
						try {
							editor.loadDefinition(file.toURI());
							currentFile.setValue(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}
}

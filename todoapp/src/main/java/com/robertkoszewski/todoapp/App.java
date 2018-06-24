package com.robertkoszewski.todoapp;

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

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InstantiationException, IllegalAccessException, ServerNotFoundException
    {

    	System.setProperty("wui.renderer", "javafx");
    	
    	WUIEngine window = new WUIEngine(new MaterialDesignTemplate());
    	
    	// View
    	window.addView("/", new View(View.Scope.SHARED) {

			public void createView(Content content) {

				final BorderLayout layout = new BorderLayout();
				content.addElement(layout);
				
				// Left Sidebar
				layout.addElement(new Label("Add Note Here: "), BorderLayout.Position.west);
				
				final TextInput input = new TextInput();
				layout.addElement(input, BorderLayout.Position.west);
				
				Button button = new Button("Add Note");
				layout.addElement(button, BorderLayout.Position.west);
				
				button.addEventListener(new EventListener() {
					public void run(String eventID, String data) {
						layout.addElement(new Note(input.getValue()), BorderLayout.Position.center);
						input.setValue("");
					}
				});
				
				
			}
    		
    	});
    	
    	// Open
    	
    	window.showView();
    	
    }
}

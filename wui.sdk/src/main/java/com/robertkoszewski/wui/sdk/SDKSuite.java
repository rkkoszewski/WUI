package com.robertkoszewski.wui.sdk;

import com.robertkoszewski.wui.Preferences;
import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.WUIEngine;
import com.robertkoszewski.wui.core.CSSDependency;
import com.robertkoszewski.wui.core.JSDependency;
import com.robertkoszewski.wui.sdk.views.ElementEditorView;
import com.robertkoszewski.wui.server.ServerNotFoundException;
import com.robertkoszewski.wui.template.materialdesign.MaterialDesignTemplate;

/**
 * Hello world!
 *
 */
public class SDKSuite 
{
    public static void main( String[] args )
    {
        try {
        	
        	// TEMPORARY: Set Preferred Renderer
	        // System.setProperty("wui.renderer", "javafx");
        	
	        // Instantiate WUI Window
			WUIEngine wui = new WUIEngine(new Preferences("port=8080"), new MaterialDesignTemplate());

			// Add Views
			wui.addView("/", new ElementEditorView(View.Scope.SHARED));
			
			// Add JS Dependencies
			wui.addDependency(new JSDependency("codemirror", "5.36.0", SDKSuite.class.getResource("/codemirror.js")));
			wui.addDependency(new JSDependency("codemirror.mode.xml", "5.36.0", SDKSuite.class.getResource("/codemirror-5.36.0/mode/xml/xml.js")));
			wui.addDependency(new JSDependency("codemirror.mode.javascript", "5.36.0", SDKSuite.class.getResource("/codemirror-5.36.0/mode/javascript/javascript.js")));
			wui.addDependency(new JSDependency("codemirror.mode.css", "5.36.0", SDKSuite.class.getResource("/codemirror-5.36.0/mode/css/css.js")));
			wui.addDependency(new JSDependency("codemirror.mode.htmlmixed", "5.36.0", SDKSuite.class.getResource("/codemirror-5.36.0/mode/htmlmixed/htmlmixed.js")));
			wui.addDependency(new JSDependency("codemirror.addon.edit.matchbrackets", "5.36.0", SDKSuite.class.getResource("/codemirror-5.36.0/edit/matchbrackets.js")));
			
			// Add CSS Dependencies
			wui.addDependency(new CSSDependency("codemirror", "5.36.0", SDKSuite.class.getResource("/codemirror.css")));
			
			// Open Window
			wui.showView();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ServerNotFoundException e) {
			e.printStackTrace();
		}
    }
}

package com.robertkoszewski.wui.sdk;

import com.robertkoszewski.wui.Preferences;
import com.robertkoszewski.wui.WUIWindow;
import com.robertkoszewski.wui.sdk.views.ElementEditorView;
import com.robertkoszewski.wui.server.ServerNotFoundException;

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
			WUIWindow window = new WUIWindow(new Preferences(new String[] {"prefered"}));

			// Add Views
			window.addView("/", new ElementEditorView(null));
			
			// Open Window
			window.open();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ServerNotFoundException e) {
			e.printStackTrace();
		}
    }
}

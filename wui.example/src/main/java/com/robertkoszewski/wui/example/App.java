package com.robertkoszewski.wui.example;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import com.google.devtools.common.options.OptionsParser;
import com.robertkoszewski.wui.Preferences;
import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.WUIEngine;
import com.robertkoszewski.wui.core.CSSDependency;
import com.robertkoszewski.wui.core.EventListener;
import com.robertkoszewski.wui.example.pictureviewer.PictureViewerView;
import com.robertkoszewski.wui.example.test.RootView;
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.template.materialdesign.MaterialDesignTemplate;
import com.robertkoszewski.wui.ui.element.Button;
import com.robertkoszewski.wui.ui.element.Label;

/**
 * Hello world!
 *
 */
public class App 
{
	
	/*
	static {
	    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
	    System.setProperty("org.slf4j.simpleLogger.showShortLogName", "true");

	}
	*/

    public static void main( String[] args )
    {

        // Running the JAR with this command seems to have a pretty decent RAM usage with JavaFX
        // java -jar WUITest.JavaFX.jar -XX:GCTimeRatio=19 -XX:MinHeapFreeRatio=20 -XX:MaxHeapFreeRatio=30
    	
        
        // Parse Arguments
        OptionsParser parser = OptionsParser.newOptionsParser(Options.class);
        parser.parseAndExitUponError(args);
        Options options = parser.getOptions(Options.class);
        
        // Show Help
        if(options.help) {
        	 System.out.println("Usage: java -jar server.jar OPTIONS");
        	 System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(), OptionsParser.HelpVerbosity.LONG));
        	 return;
        }

        
        // Setup Renderer
        // System.setProperty("wui.renderer", "javafx");
        
        WUIEngine w = null;
		try {
			
			Preferences settings = new Preferences();
			if(options.port > 0) settings.setSetting("Port", options.port + "");
			settings.setSetting("Headless", options.headless + "");
			
			w = new WUIEngine(settings, new MaterialDesignTemplate());
		} catch (Exception e) {
			System.err.println("ERROR: An exception found during start of the Server system.");
			e.printStackTrace();
			System.exit(1);
		} 
        
        w.setIcon(RootView.class.getResource("icon.png")); // Set Icon

        // Class Based View
        w.addView("/", new RootView()); // Define Root View
        //w.addView("/", new PictureViewerView(View.Scope.SHARED)); // Define Root View
        

        // Add View
        final EventListener callback = new EventListener() {
			public void run(String eventID, String data) {
				System.exit(0);
			}
        };
        
        w.addView("/data", new View(View.Scope.SHARED) { // Default is global view
			public void createView(Content content) {
				
				content.addElement(new Label("ASD"));
				Button button = new Button("Shutdown");
				button.addEventListener(callback);
				content.addElement(button);

				Button button2 = new Button("GO BACK TO ROOT VIEW");
				button2.setLink("/");
				content.addElement(button2);
			}
        });

        
        w.showView();

        /*
        WUIWindow w = WUI.newWindow("Title", "Icon");
        
        
        //new WUIApp();
        
        
        Renderer r = new RendererNative();
        r.open("http://www.google.es", "", null, false, "", null);
        
        
        */
        
    }
    
    private void showSysUsage() {
	  OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
	  for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
	    method.setAccessible(true);
	    if (method.getName().startsWith("get")
	        && Modifier.isPublic(method.getModifiers())) {
	            Object value;
	        try {
	            value = method.invoke(operatingSystemMXBean);
	        } catch (Exception e) {
	            value = e;
	        } // try
	        System.out.println(method.getName() + " = " + value);
	    } // if
	  } // for
    }
}

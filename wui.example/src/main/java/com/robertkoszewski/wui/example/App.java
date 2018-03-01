package com.robertkoszewski.wui.example;

import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.WUIWindow;
import com.robertkoszewski.wui.example.test.RootView;
import com.robertkoszewski.wui.template.Content;
import com.robertkoszewski.wui.ui.element.Button;
import com.robertkoszewski.wui.ui.element.Label;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        /*
        Server s = ServerFactory.getServerInstance();
        s.startServer(8080);
        
        s.addPage("/", "IT UTTERLY WORKS!");
        */
        
        
        WUIWindow w = new WUIWindow(); // TODO: Add configuration options to this
        
        w.setIcon(RootView.class.getResource("icon.png")); // Set Icon
        
        // Class Based View
        w.addView("/data", new RootView()); // Define Root View
        
        
        // Add View
        final Runnable callback = new Runnable() {
			public void run() {
				System.exit(0);
			}
        };
        
        w.addView("/", new View(View.Type.PRIVATE) { // Default is global view
			public void createView(Content content) {
				content.addElement(new Label("ASD"));
				Button button = new Button("Shutdown");
				button.addActionListener(callback);
				content.addElement(button);
			}
        });

        
        w.open();

        /*
        WUIWindow w = WUI.newWindow("Title", "Icon");
        
        
        //new WUIApp();
        
        
        Renderer r = new RendererNative();
        r.open("http://www.google.es", "", null, false, "", null);
        
        
        */
        
    }
}

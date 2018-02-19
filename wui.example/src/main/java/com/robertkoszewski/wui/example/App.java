package com.robertkoszewski.wui.example;

import com.robertkoszewski.wui.WUIWindow;
import com.robertkoszewski.wui.core.View;
import com.robertkoszewski.wui.core.ViewInterface;
import com.robertkoszewski.wui.element.Label;
import com.robertkoszewski.wui.example.test.RootController;
import com.robertkoszewski.wui.templates.Content;

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
        
        
        WUIWindow w = new WUIWindow();
        w.setIcon(RootController.class.getResource("icon.png"));
        
        w.addController("/", new RootController());
        
        w.open();
        
        // Define View
        new View(View.Type.PRIVATE) { // Default is global view
			public Content createView(Content content) {
				content.addElement(new Label("ASD"));
				return null;
			}
        };
        
        /*
        WUIWindow w = WUI.newWindow("Title", "Icon");
        
        
        //new WUIApp();
        
        
        Renderer r = new RendererNative();
        r.open("http://www.google.es", "", null, false, "", null);
        
        
        */
        
    }
}

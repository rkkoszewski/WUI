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

package com.robertkoszewski.wui.renderer;

import java.util.Iterator;
import java.util.Set;

import org.reflections.Reflections;

/**
 * Renderer Factory
 * @author Robert Koszewski
 */
public class RendererFactory {
	
	public static Renderer getRendererInstance() throws RendererNotFoundException, InstantiationException, IllegalAccessException {
		Reflections reflections = new Reflections("com.robertkoszewski.wui.renderer");
		Set<Class<? extends Renderer>> modules = reflections.getSubTypesOf(Renderer.class);
		
		if(modules.size() == 0) {
			throw new RendererNotFoundException();
		}
		
		System.out.println("FOUND " + modules.size() + " RENDERER IMPLEMENTATIONS");
		
		String prefered_renderer = System.getProperty("wui.renderer");
		prefered_renderer = prefered_renderer.toLowerCase(); // Make it Lower Case
		
		if(prefered_renderer != null) { 
			
			Iterator<Class<? extends Renderer>> rit = modules.iterator();
			while(rit.hasNext()) {
				Class<? extends Renderer> rendererClass = rit.next();
				
				if(rendererClass.getName().toLowerCase().contains(prefered_renderer)) { // TODO: This is very spartan. Maybe do a full class path check first, then compare most similar name.
					System.out.println("FOUND RENDERER WITH NAME '" + rendererClass.getName() + "' similar to prefered renderer's name '" + prefered_renderer + "'");
					try {
						return rendererClass.newInstance();
					}catch(Exception e) {
						System.err.println("ERROR: Could not instantiate the Renderer. Trying another one. Stack Trace will follow:");
						e.printStackTrace();
						
					}
				}
			}

		}

		return modules.iterator().next().newInstance(); // No Preferred Renderer - Use first renderer found.
	}
	
}

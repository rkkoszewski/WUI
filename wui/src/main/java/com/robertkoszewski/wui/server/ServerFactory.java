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

package com.robertkoszewski.wui.server;

import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server Factory
 * @author Robert Koszewski
 */
public class ServerFactory {
	
	protected final static Logger log = LoggerFactory.getLogger(ServerFactory.class);
	
	/**
	 * Get Server Instance
	 * @return
	 * @throws ServerNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static Server getServerInstance() throws ServerNotFoundException, InstantiationException, IllegalAccessException {
		Reflections reflections = new Reflections("com.robertkoszewski.wui.server");
		Set<Class<? extends Server>> modules = reflections.getSubTypesOf(Server.class);
		
		if(modules.size() == 0) {
			log.error("Could not find any server implementations. WUI won't be able to start.");
			throw new ServerNotFoundException();
		}
		
		// Log Server Count
		log.info("FOUND " + modules.size() + " SERVER IMPLEMENTATIONS");
		
		return modules.iterator().next().newInstance();
	}
	
}

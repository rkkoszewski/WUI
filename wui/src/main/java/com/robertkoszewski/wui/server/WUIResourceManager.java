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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.robertkoszewski.wui.utils.StringUtils;

/**
 * WUI Resource Provider
 * @author Robert Koszewski
 */
public class WUIResourceManager implements ResourceManager {
	
	private static String BasePath = "/com/robertkoszewski/wui/resources/";

	@Override
	public boolean resourceExists(String path) {
		return WUIResourceManager.class.getResourceAsStream(processPath(path, true)) != null;
	}

	@Override
	public InputStream getResource(String path) {
		return WUIResourceManager.class.getResourceAsStream(processPath(path, true));
	}

	@Override
	public String getResourceAsString(String path) {
		return getResourceAsString(WUIResourceManager.class.getResourceAsStream(processPath(path, true))); 
	}
	
	@Override
	public String getResourceAsString(InputStream is) {
		if(is == null) return null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String out = "";
		try {
			String line;
			while ((line = reader.readLine()) != null)
				out += line;
		} catch (IOException e) {
			return null;
		}
		return out;
	}
	
	// Helper Methods
	private String processPath(String path, boolean prependBasePath) {
		return BasePath + StringUtils.removeStringFromStringStart(path, "/");
	}
}

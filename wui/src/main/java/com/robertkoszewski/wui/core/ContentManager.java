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

package com.robertkoszewski.wui.core;

import java.net.URL;

import com.robertkoszewski.wui.View;
import com.robertkoszewski.wui.server.ResponseManager;

/**
 * Content Manager Interface
 * @author Robert Koszewski
 */
public interface ContentManager extends ResponseManager {

	/**
	 * Set Application Icon
	 * @param resource
	 */
	public void setIcon(URL resource);

	/**
	 * Add View
	 * @param url
	 * @param view
	 */
	public void addView(String url, View view);
	
	/**
	 * Get View
	 * @param url
	 * @return
	 */
	public View getView(String url);
	
	/**
	 * Remove View by URL
	 * @param url
	 */
	public void removeView(String url);

	/**
	 * Add Web Dependency
	 * @param dependency
	 */
	public void addDependency(WebDependency dependency);
}

/*******************************************************************************
 * Copyright (c) 2016 Robert Koszewski
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package com.robertkoszewski.wui.utils;

import java.text.DecimalFormat;

/**
 * String Related Utils
 * @author Robert Koszewski
 */
public class StringUtils {

	/**
	 * Removes the extension of a filename
	 * @param filename
	 * @return
	 */
	public static String removeExtensionFromFilename(String filename){
		int index = filename.lastIndexOf('.');
		if(index == -1)
			return filename;
		else
			return filename.substring(0, index);
	}
	
	/**
	 * Readable File Size (Author: Mr Ed & Willem Van Onsem [http://stackoverflow.com/a/5599842/5284104])
	 * @param size
	 * @return
	 */
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0";
	    final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	/**
	 * Remove String from String Start
	 * @param text
	 * @param removeChar
	 * @return
	 */
	public static String removeStringFromStringStart(String text, String remove) {
		int len = remove.length();
		while(text.startsWith(remove))
			text = text.substring(len);
		return text;
	}
}

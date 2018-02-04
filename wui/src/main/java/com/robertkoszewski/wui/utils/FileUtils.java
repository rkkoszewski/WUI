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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;

/**
 * File related Utils
 * @author Robert Koszewski
 */
public class FileUtils {
	
	/**
	 * Read File from the current ClassPath to String
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readFileInJARToString(String path) throws Exception{
		InputStream in = FileUtils.class.getResourceAsStream(path); 
		if(in == null) return null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String out = "";
		String line;
		while ((line = reader.readLine()) != null)
			out += line;
		return out;
	}
	
	/**
	 * Write String to File
	 * @param file
	 * @param string
	 * @throws IOException
	 */
	public static void writeStringtoFile(File file, String string) throws IOException{
		try(PrintStream out = new PrintStream(new FileOutputStream(file))) {
		    out.print(string);
		}
	}
	
	
	/**
	 * Read String from File
	 * @param file
	 * @return
	 */
	public static String readFiletoString(File file){
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes/*,"UTF-8"*/);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}
}

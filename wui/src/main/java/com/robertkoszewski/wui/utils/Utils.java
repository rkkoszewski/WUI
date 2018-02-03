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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

//import javafx.scene.image.Image;

public class Utils {
	/**
	 * Retruns a JavaFX Image from a String URL
	 * @param url
	 * @return
	 */
	public static javafx.scene.image.Image getFXImage(String url){
		return new javafx.scene.image.Image(Utils.class.getResourceAsStream(url));
	}
	
	/**
	 * Returns a BufferedImage for Swing
	 * @param url
	 * @return Icon List
	 * @throws IOException
	 */
	public static List<Image> getSwingImage(String url) throws IOException{
		final int icn_min_scale = 16;	// Standard Swing Icons used are:
		final int icn_max_scale = 128; // 16x16, 32x32, 64x64x 128x128
		
		List<Image> icons = new ArrayList<Image>();
		BufferedImage icon = ImageIO.read(Utils.class.getResource(url));
		for(int scale = icn_min_scale; scale <= icn_max_scale; scale = scale*2 ){
			icons.add(icon.getScaledInstance(scale, scale, Image.SCALE_AREA_AVERAGING));
			// System.out.println("Generating icon for scale "+scale+"x"+scale);
		}
		
		return icons;
	}
	
	/**
	 * Return a Timestamp
	 * @return Timestamp
	 */
	public static long getTimestamp(){
		return System.currentTimeMillis();
		//return (int) (System.currentTimeMillis() / 1000L); // Return UNIX time stamp
	}
	
	private static long uid = 1;
	/**
	 * Returns a unique long number
	 * @return
	 */
	public static synchronized long getLongUID(){
		return uid++;
	}
	
	private static long euid = 1;
	/**
	 * 
	 * @return Element UID
	 */
	public static synchronized String getElementUID(){
		return "eid"+euid++;
	}
}

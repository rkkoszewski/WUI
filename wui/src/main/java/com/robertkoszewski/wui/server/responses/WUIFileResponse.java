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

package com.robertkoszewski.wui.server.responses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

/**
 * File Response
 * @author Robert Koszewski
 */
public class WUIFileResponse extends BaseResponse implements FileResponse{
	
	// Variables
	private final InputStream inputStream;
	private long size;
	private boolean forceDownload = false;

	// Constructors
	/**
	 * File Constructor
	 * @param mimeType
	 * @param file
	 * @throws FileNotFoundException
	 */
	public WUIFileResponse(String mimeType, File file) throws FileNotFoundException {
		super(mimeType);
		size = file.length();
		this.inputStream = new FileInputStream(file);
	}
	
	public WUIFileResponse(String mimeType, File file, boolean forceDownload) throws FileNotFoundException {
		this(mimeType, file);
		this.forceDownload = forceDownload;
	}
	
	/**
	 * InputStream Constructor
	 * @param mimeType
	 * @param inputStream
	 * @throws FileNotFoundException
	 */
	public WUIFileResponse(String mimeType, InputStream inputStream) throws FileNotFoundException {
		super(mimeType);
		try {
			size = inputStream.available();
		} catch (IOException e) {
			size = -1;
		}
		this.inputStream = inputStream;
	}
	
	public WUIFileResponse(String mimeType, InputStream inputStream, boolean forceDownload) throws FileNotFoundException {
		this(mimeType, inputStream);
		this.forceDownload = forceDownload;
	}
	
	// Methods

	@Override
	public byte[] getResponse() {
		try {
			return IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public long getFileSize() {
		return size;
	}

	@Override
	public boolean forceDownload() {
		return forceDownload;
	}
}

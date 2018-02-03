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

package com.robertkoszewski.wui.test;

import com.robertkoszewski.wui.WUIController;
import com.robertkoszewski.wui.WUIView;
import com.robertkoszewski.wui.templates.Content;
import com.robertkoszewski.wui.templates.WindowTemplate;

public class RootController implements WUIController{

	private RootView view;
	int i = 1;
	
	@Override
	public void initialize(WindowTemplate template) {
		view = new RootView(template.getContentInstance());
	}

	@Override
	public Content viewUpdate() {
		view.setText("IT WORKS. COUNTING: " + i++);
		
		return view.getContent();
	}
	
	public class RootView implements WUIView{

		private Content content;
		
		public RootView(Content content) {
			this.content = content;
		}
		
		public void setText(String text) {
			content.setHTML(text);
		};

		@Override
		public Content getContent() {
			return content;
		}
		
	}

}

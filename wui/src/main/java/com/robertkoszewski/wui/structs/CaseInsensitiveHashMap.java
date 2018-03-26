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

package com.robertkoszewski.wui.structs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Hash Map with Case Insensitive Keys
 * @author Robert Koszewski
 */
public class CaseInsensitiveHashMap<T> extends HashMap<String, T>{

	private static final long serialVersionUID = 1144995035716213368L;

	@Override
	public boolean containsKey(Object key) {
		if(key instanceof String)
			return super.containsKey(((String) key).toLowerCase());
		return super.containsKey(key);
	}

	@Override
	public T get(Object key) {
		if(key instanceof String)
			return super.get(((String) key).toLowerCase());
		return super.get(key);
	}

	@Override
	public T put(String key, T value) {
		return super.put(key.toLowerCase(), value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends T> values) {
		Map<String, T> map = new HashMap<String, T>();
		Iterator<?> vit = values.entrySet().iterator();
		while(vit.hasNext()) {
			@SuppressWarnings("unchecked")
			Entry<String, T> entry = (Entry<String, T>) vit.next();
			map.put(entry.getKey().toLowerCase(), entry.getValue());
		}
		super.putAll(map);
	}

	@Override
	public T remove(Object key) {
		if(key instanceof String)
			return super.remove(((String) key).toLowerCase());
		return super.remove(key);
	}
}

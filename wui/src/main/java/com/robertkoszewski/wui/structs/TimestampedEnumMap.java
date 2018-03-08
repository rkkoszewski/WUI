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

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

import com.robertkoszewski.wui.utils.Utils;

/**
 * Timestamped EnumMap
 * @author Robert Koszewski
 *
 * @param <K>
 * @param <V>
 */
public class TimestampedEnumMap<K extends Enum<K>, V> extends EnumMap<K, V> {

	// Serial Number
	private static final long serialVersionUID = -2470731693690775408L;
	
	// Variables
	private long timestamp = 0;

	public TimestampedEnumMap(Class<K> clazz) {
		super(clazz);
	}
	
	// Custom Methods
	
	/**
	 * Get Last Change Timestamp
	 * @return
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	// Private Methods
	protected void contentChanged() {
		timestamp = Utils.getChangeTimestamp();
	}
	
	/**
	 * Public Trigger Content Changed
	 */
	public void triggerContentChanged() {
		contentChanged();
	}

	// Overridden Methods
	
	@Override
	public V put(K key, V value) {
		V ret = super.put(key, value);
		contentChanged();
		return ret;
	}
	
	@Override
	public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
		V ret = super.merge(key, value, remappingFunction);
		contentChanged();
		return ret;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		super.putAll(m);
		contentChanged();
	}
	
	@Override
	public V putIfAbsent(K key, V value) {
		V ret = super.putIfAbsent(key, value);
		contentChanged();
		return ret;
	}
	
	@Override
	public V remove(Object key) {
		V ret = super.remove(key);
		contentChanged();
		return ret;
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		boolean ret = super.remove(key, value);
		contentChanged();
		return ret;
	}
	
	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		boolean ret = super.replace(key, oldValue, newValue);
		contentChanged();
		return ret;
	}
	
	@Override
	public V replace(K key, V value) {
		V ret = super.replace(key, value);
		contentChanged();
		return ret;
	}
	
	@Override
	public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
		super.replaceAll(function);
		contentChanged();
	}

	@Override
	public void clear() {
		super.clear();
		contentChanged();
	}
}

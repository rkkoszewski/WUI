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

/**
 * Timestamped Notifying EnumMap
 * @author Robert Koszewski
 *
 * @param <K>
 * @param <V>
 */
public class TimestampedNotifyingEnumMap<K extends Enum<K>, V> extends TimestampedEnumMap<K, V> {

	// Serial Number
	private static final long serialVersionUID = -2470731693690785408L;
	
	// Variables
	private final Object notifyTarget;

	public TimestampedNotifyingEnumMap(Class<K> clazz, Object notifyTarget) {
		super(clazz);
		if(notifyTarget == null) throw new NullPointerException(); // Null Objects not supported. Use TimestampedEnumMap instead.
		this.notifyTarget = notifyTarget;
	}

	// Private Methods
	
	protected void contentChanged() {
		super.contentChanged();
		// Notify Object
		synchronized(notifyTarget) {
			notifyTarget.notifyAll();
		}
	}
}

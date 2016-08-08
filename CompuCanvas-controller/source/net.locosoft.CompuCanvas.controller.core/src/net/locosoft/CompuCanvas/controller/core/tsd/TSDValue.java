/*****************************************************************************
 * Copyright (c) 2016 Chris J Daly (github user cjdaly)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   cjdaly - initial API and implementation
 ****************************************************************************/

package net.locosoft.CompuCanvas.controller.core.tsd;

import net.locosoft.CompuCanvas.controller.util.C3Util;

public class TSDValue {

	private long _timeMillis;
	private TSDType _type;
	private boolean _isArray;

	private String _stringValue = "";
	private String[] _stringValues = {};
	private long _longValue = 0;
	private long[] _longValues = {};
	private double _doubleValue = 0;
	private double[] _doubleValues = {};

	private TSDBuffer _buffer;

	TSDValue(long timeMillis, String value, TSDBuffer buffer) {
		_timeMillis = timeMillis;
		_type = TSDType.String;
		_isArray = false;
		_stringValue = value;
		_buffer = buffer;
	}

	TSDValue(long timeMillis, String[] values, TSDBuffer buffer) {
		_timeMillis = timeMillis;
		_type = TSDType.String;
		_isArray = true;
		_stringValues = values;
		_buffer = buffer;
	}

	TSDValue(long timeMillis, long value, TSDBuffer buffer) {
		_timeMillis = timeMillis;
		_type = TSDType.Long;
		_isArray = false;
		_longValue = value;
		_buffer = buffer;
	}

	TSDValue(long timeMillis, long[] values, TSDBuffer buffer) {
		_timeMillis = timeMillis;
		_type = TSDType.Long;
		_isArray = true;
		_longValues = values;
		_buffer = buffer;
	}

	TSDValue(long timeMillis, double value, TSDBuffer buffer) {
		_timeMillis = timeMillis;
		_type = TSDType.Double;
		_isArray = false;
		_doubleValue = value;
		_buffer = buffer;
	}

	TSDValue(long timeMillis, double[] values, TSDBuffer buffer) {
		_timeMillis = timeMillis;
		_type = TSDType.Double;
		_isArray = true;
		_doubleValues = values;
		_buffer = buffer;
	}

	TSDValue(long timeMillis, String value, TSDType type, TSDBuffer buffer) {
		_timeMillis = timeMillis;
		_type = type;
		_isArray = false;
		_stringValue = value;
		switch (type) {
		case Long:
			_longValue = C3Util.parseLong(value, 0);
			break;
		case Double:
			_doubleValue = C3Util.parseDouble(value, 0);
			break;
		default:
		}
		_buffer = buffer;
	}

	public long getTime() {
		return _timeMillis;
	}

	public TSDType getType() {
		return _type;
	}

	public boolean isArray() {
		return _isArray;
	}

	public int getSize() {
		if (!isArray())
			return 1;
		switch (_type) {
		case String:
			return _stringValues == null ? 0 : _stringValues.length;
		case Long:
			return _longValues == null ? 0 : _longValues.length;
		case Double:
			return _doubleValues == null ? 0 : _doubleValues.length;
		default:
			return 0;
		}
	}

	public TSDBuffer getBuffer() {
		return _buffer;
	}

	public String asString() {
		switch (_type) {
		case String:
			return _stringValue;
		case Long:
			return Long.toString(_longValue);
		case Double:
			return Double.toString(_doubleValue);
		default:
			return null;
		}
	}

	public String[] asStrings() {
		return _stringValues;
	}

	public long asLong() {
		switch (_type) {
		case String:
			return C3Util.parseLong(_stringValue, 0);
		case Long:
			return _longValue;
		case Double:
			return (long) _doubleValue;
		default:
			return 0;
		}
	}

	public long[] asLongs() {
		return _longValues;
	}

	public double asDouble() {
		switch (_type) {
		case String:
			return C3Util.parseDouble(_stringValue, 0);
		case Long:
			return (double) _longValue;
		case Double:
			return _doubleValue;
		default:
			return 0;
		}
	}

	public double[] asDoubles() {
		return _doubleValues;
	}

	public String toString() {
		return "TSD: " + getBuffer().getHashKey() + " = " + asString();
	}
}

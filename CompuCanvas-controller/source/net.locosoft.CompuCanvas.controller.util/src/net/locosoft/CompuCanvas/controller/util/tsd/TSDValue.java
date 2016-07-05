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

package net.locosoft.CompuCanvas.controller.util.tsd;

import net.locosoft.CompuCanvas.controller.util.C3Util;

public class TSDValue {

	private long _timeMillis;
	private TSDType _type;

	private String _stringValue = "";
	private long _longValue = 0;
	private double _doubleValue = 0;

	public TSDValue(String value) {
		this(System.currentTimeMillis(), value);
	}

	public TSDValue(long timeMillis, String value) {
		_timeMillis = timeMillis;
		_type = TSDType.String;
		_stringValue = value;
	}

	public TSDValue(long value) {
		this(System.currentTimeMillis(), value);
	}

	public TSDValue(long timeMillis, long value) {
		_timeMillis = timeMillis;
		_type = TSDType.Long;
		_longValue = value;
	}

	public TSDValue(double value) {
		this(System.currentTimeMillis(), value);
	}

	public TSDValue(long timeMillis, double value) {
		_timeMillis = timeMillis;
		_type = TSDType.Double;
		_doubleValue = value;
	}

	public TSDValue(String value, TSDType type) {
		this(System.currentTimeMillis(), value, type);
	}

	public TSDValue(long timeMillis, String value, TSDType type) {
		_timeMillis = timeMillis;
		_type = type;
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
	}

	public long getTime() {
		return _timeMillis;
	}

	public TSDType getType() {
		return _type;
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

	public String toString() {
		return asString();
	}
}

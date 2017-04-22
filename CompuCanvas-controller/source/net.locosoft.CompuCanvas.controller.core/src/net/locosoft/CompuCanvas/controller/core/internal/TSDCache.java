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

package net.locosoft.CompuCanvas.controller.core.internal;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.locosoft.CompuCanvas.controller.core.tsd.TSDValue;

public class TSDCache {

	private int _bucketCount = 1024;
	private TreeMap<Long, Bucket> _timeMillisToBucket = new TreeMap<Long, Bucket>();

	public synchronized void add(TSDValue value) {
		if (value == null)
			return;

		Bucket bucket = _timeMillisToBucket.get(value.getTime());
		if (bucket == null) {
			bucket = new Bucket(value);
			_timeMillisToBucket.put(bucket.getTime(), bucket);
			while (_timeMillisToBucket.size() > _bucketCount) {
				_timeMillisToBucket.remove(_timeMillisToBucket.firstKey());
			}
		} else {
			bucket.add(value);
		}
	}

	public synchronized TSDValue[] getLatest(int sizeHint) {
		ArrayList<TSDValue> values = new ArrayList<TSDValue>();

		for (Entry<Long, Bucket> entry : _timeMillisToBucket.descendingMap().entrySet()) {
			Bucket bucket = entry.getValue();
			bucket.fill(values);

			if (values.size() >= sizeHint)
				break;
		}

		return (TSDValue[]) values.toArray(new TSDValue[values.size()]);
	}

	class Bucket {
		final TSDValue _first;
		ArrayList<TSDValue> _rest;

		final long getTime() {
			return _first.getTime();
		}

		Bucket(TSDValue value) {
			_first = value;
		}

		void add(TSDValue value) {
			if ((value == null) || (value.getTime() != getTime()))
				return;

			if (_rest == null) {
				_rest = new ArrayList<TSDValue>();
			}
			_rest.add(value);
		}

		void fill(ArrayList<TSDValue> values) {
			values.add(_first);
			if (_rest != null) {
				values.addAll(_rest);
			}
		}
	}
}

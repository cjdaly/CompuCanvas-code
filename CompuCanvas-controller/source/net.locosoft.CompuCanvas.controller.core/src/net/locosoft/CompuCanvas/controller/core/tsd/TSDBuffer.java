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

import java.util.LinkedList;

import net.locosoft.CompuCanvas.controller.util.C3Util;

public class TSDBuffer {

	private String _id;
	private String _hashKey;
	private String _units;
	private TSDType _type;
	private int _size;
	private TSDGroup _group;

	private LinkedList<TSDValue> _values = new LinkedList<TSDValue>();

	TSDBuffer(String id, String units, TSDType type, int size, TSDGroup group) {
		_id = id;
		_units = units;
		_type = type;
		_size = size;
		_group = group;
		_hashKey = _group.getHashKey() + "/" + _id;
	}

	public String getHashKey() {
		return _hashKey;
	}

	public String getId() {
		return _id;
	}

	public String getUnits() {
		return _units;
	}

	public TSDType getType() {
		return _type;
	}

	public int getSize() {
		return _size;
	}

	public TSDGroup getGroup() {
		return _group;
	}

	public synchronized TSDValue getLatest() {
		if (_values.isEmpty())
			return null;
		else
			return _values.getFirst();
	}

	public synchronized void update(String value) {
		update(System.currentTimeMillis(), value);
	}

	public synchronized void update(long timeMillis, String value) {
		if (value == null)
			return;
		TSDValue tsd = new TSDValue(timeMillis, value, getType(), this);
		updateHelper(tsd);
	}

	public synchronized void update(String[] values) {
		update(System.currentTimeMillis(), values);
	}

	public synchronized void update(long timeMillis, String[] values) {
		if (values == null)
			return;
		TSDValue tsd = new TSDValue(timeMillis, values, this);
		updateHelper(tsd);
	}

	public synchronized void update(long value) {
		update(System.currentTimeMillis(), value);
	}

	public synchronized void update(long timeMillis, long value) {
		TSDValue tsd = new TSDValue(timeMillis, value, this);
		updateHelper(tsd);
	}

	public synchronized void update(double value) {
		update(System.currentTimeMillis(), value);
	}

	public synchronized void update(long timeMillis, double value) {
		TSDValue tsd = new TSDValue(timeMillis, value, this);
		updateHelper(tsd);
	}

	private void updateHelper(TSDValue tsd) {
		if (tsd == null)
			return;

		if (tsd.getType() != getType()) {
			C3Util.log("TSDValue type mismatch! " //
					+ "(buffer id: " + _id //
					+ ", tsd: " + tsd + "," //
					+ ", type: " + tsd.getType() + "," //
					+ ", expected: " + getType() + ")");
			return;
		}

		TSDValue tsdLatest = getLatest();
		if ((tsdLatest != null) && (tsdLatest.getTime() > tsd.getTime())) {
			C3Util.log("TSDValue older than previous latest! " //
					+ "(buffer id: " + _id //
					+ ", tsd: " + tsd + ")");
			return;
		}

		_values.addFirst(tsd);
		_group.propagate(tsd);
		while (_values.size() > _size) {
			_values.removeLast();
		}
	}

	public synchronized TSDAverage getAverage(long lookbackMillis) {
		// TODO
		return new TSDAverage();
	}

}

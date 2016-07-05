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

import java.util.LinkedList;

public class TSDBuffer {

	private String _id;
	private String _units;
	private TSDType _type;
	private int _size = 256;
	private LinkedList<TSDValue> _tsdValues = new LinkedList<TSDValue>();

	public TSDBuffer(String id, String units, TSDType type) {
		this(id, units, type, 64);
	}

	public TSDBuffer(String id, String units, TSDType type, int size) {
		_id = id;
		_units = units;
		_type = type;
		_size = size;
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

	public synchronized TSDValue getLatest() {
		if (_tsdValues.isEmpty())
			return null;
		else
			return _tsdValues.getFirst();
	}

	public synchronized void setLatest(TSDValue tsd) {
		if (tsd == null)
			return;

		TSDValue tsdLatest = getLatest();
		if (tsdLatest == null)
			return;

		if (tsdLatest.getTime() > tsd.getTime()) {
			System.out.println("TSDValue older than previous latest! " //
					+ "(buffer id: " + _id //
					+ ", tsd: " + tsd + ")");
			return;
		}

		if (tsdLatest.getType() != getType()) {
			System.out.println("TSDValue type mismatch! " //
					+ "(buffer id: " + _id //
					+ ", tsd: " + tsd + "," //
					+ ", type: " + tsd.getType() + "," //
					+ ", expected: " + getType() + ")");
			return;
		}

		_tsdValues.addFirst(tsd);
		while (_tsdValues.size() > _size) {
			_tsdValues.removeLast();
		}
	}

	public synchronized TSDAverage getAverage(long lookbackMillis) {
		// TODO
		return new TSDAverage();
	}

}

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

public enum TSDType {
	Long(1), //
	LongArray(2), //
	Double(3), //
	DoubleArray(4), //
	String(5), //
	StringArray(6);

	private int _ordinal;

	TSDType(int ordinal) {
		_ordinal = ordinal;
	}

	public int getOrdinal() {
		return _ordinal;
	}
}

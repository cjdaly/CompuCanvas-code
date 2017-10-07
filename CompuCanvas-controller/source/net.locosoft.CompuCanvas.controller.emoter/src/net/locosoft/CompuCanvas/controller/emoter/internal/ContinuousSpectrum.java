/*****************************************************************************
 * Copyright (c) 2017 Chris J Daly (github user cjdaly)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   cjdaly - initial API and implementation
 ****************************************************************************/

package net.locosoft.CompuCanvas.controller.emoter.internal;

public abstract class ContinuousSpectrum extends DiscreteSpectrum {

	private int _max;

	public ContinuousSpectrum(String id, String[] labels, int max) {
		super(id, labels);
		_max = max;
	}

	public int getMax() {
		return _max;
	}

}

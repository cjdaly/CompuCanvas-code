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

import net.locosoft.CompuCanvas.controller.emoter.ISpectrum;

public class DiscreteSpectrum implements ISpectrum {

	private String _id;
	private String[] _labels;

	public DiscreteSpectrum(String id, String[] labels) {
		_id = id;
		_labels = labels;
	}

	public String getId() {
		return _id;
	}

	public int getMin() {
		return 0;
	}

	public int getMax() {
		return _labels.length - 1;
	}

	public String[] getLabels() {
		return _labels;
	}

	public String getLabel(int index) {
		if (index < 0)
			index = 0;
		if (index >= _labels.length)
			index = _labels.length - 1;
		return _labels[index];
	}
}

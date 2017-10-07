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

public class Analog65535ContinuousSpectrum extends ContinuousSpectrum {

	public static final String[] _LABELS = new String[] //
	{ "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10" };

	public Analog65535ContinuousSpectrum() {
		super("analog65535", _LABELS, 65535);
	}

}

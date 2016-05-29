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

package net.locosoft.CompuCanvas.controller.BlinkStick;

import net.locosoft.CompuCanvas.controller.core.IC3Service;

public interface IBlinkStickService extends IC3Service {

	public int getBlinkStickCount();

	public IBlinkStick getBlinkStick(int index);

	// see here: https://www.w3.org/TR/css3-color/
	public static final String[] _BasicColors = new String[] { //
			"black", "silver", "gray", "white", //
			"maroon", "red", "purple", "fuchsia", //
			"green", "lime", "olive", "yellow", //
			"navy", "blue", "aqua", "teal" };
}

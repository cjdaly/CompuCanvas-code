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

package net.locosoft.CompuCanvas.controller.cascade.internal.cypher;

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.util.C3Util;

public class ControlWake extends WheelOfCypher.Cog {

	private int _wakeCycles = 4;
	private int _wakeCycle = _wakeCycles;

	public Cypher newCypher() {

		if (isLoggingEnabled()) {
			C3Util.log(getClass().getSimpleName() + " - wakeCycle: " + _wakeCycle);
		}

		_wakeCycle--;
		if (_wakeCycle == 0) {
			_wakeCycle = _wakeCycles;
			setGear("sleep");
		}

		return null;
	}

}

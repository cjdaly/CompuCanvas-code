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

public class ControlSleep extends WheelOfCypher.Cog {

	private boolean _sleepCycle = true;

	public Cypher newCypher() {

		logCog(null);

		if (_sleepCycle) {
			_sleepCycle = false;
		} else {
			setGear("wake");
			_sleepCycle = true;
		}

		return null;
	}
}

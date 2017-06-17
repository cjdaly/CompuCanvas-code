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

public class SleepChill extends WheelOfCypher.Cog {

	private static final int _ChillMillis = 10 * 1000;

	public Cypher newCypher() {

		logCog("chillMillis: " + _ChillMillis + " ....");
		try {
			Thread.sleep(_ChillMillis);
		} catch (InterruptedException e) {
		}
		logCog("chillMillis: " + _ChillMillis + " .");

		return null;
	}

}

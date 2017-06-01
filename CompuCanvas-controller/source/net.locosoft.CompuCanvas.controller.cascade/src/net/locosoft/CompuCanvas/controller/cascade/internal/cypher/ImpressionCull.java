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

public class ImpressionCull extends WheelOfCypher.Cog {

	private static final long _CULL_MILLIS = 5000;

	public Cypher newCypher() {
		Cypher cypher = new Cypher() {

			public String getText() {
				return "MATCH (n:Impression) WHERE (n.timeValue < $cullTime) DETACH DELETE n";
			}
		};

		long cullTime = System.currentTimeMillis() - _CULL_MILLIS;
		cypher.addParam("cullTime", cullTime);

		return cypher;
	}

}

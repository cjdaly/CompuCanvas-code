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

package net.locosoft.CompuCanvas.controller.cascade.internal;

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.Neo4j.INeo4jService;
import net.locosoft.CompuCanvas.controller.cascade.internal.cypher.WheelOfCypher;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class Cascade extends MonitorThread {

	private CascadeService _cascadeService;
	private INeo4jService _neo4jService;
	private WheelOfCypher _wheelOfCypher;

	public Cascade(CascadeService cascadeService) {
		_cascadeService = cascadeService;
		_neo4jService = cascadeService.getCoreService().getService(INeo4jService.class);
		_wheelOfCypher = WheelOfCypher.getDefault(cascadeService);
	}

	protected long getPreSleepMillis() {
		return 500;
	}

	public boolean cycle() throws Exception {

		int cypherQueueLength = _neo4jService.getCypherQueueLength();
		if (cypherQueueLength == 0) {
			Cypher cypher = _wheelOfCypher.nextCypher();
			if (cypher != null) {
				_neo4jService.runCypher(cypher);
			}
		} else {
			if (_cascadeService.serviceIsLoggingEnabled("cypher")) {
				C3Util.log("Cascade - waiting for Neo4j queue: " + cypherQueueLength + " ....");
			}
		}

		return true;
	}

}

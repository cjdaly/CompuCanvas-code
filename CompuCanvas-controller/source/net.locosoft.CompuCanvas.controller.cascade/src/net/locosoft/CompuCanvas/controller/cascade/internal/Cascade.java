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
import net.locosoft.CompuCanvas.controller.core.ICoreService;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class Cascade extends MonitorThread {

	private ICoreService _coreService;
	private INeo4jService _neo4jService;
	private WheelOfCypher _wheelOfCypher;

	public Cascade(ICoreService coreService, INeo4jService neo4jService) {
		_coreService = coreService;
		_neo4jService = neo4jService;
		_wheelOfCypher = WheelOfCypher.getDefault(_coreService);
	}

	protected long getPreSleepMillis() {
		return 1000;
	}

	public boolean cycle() throws Exception {

		Cypher cypher = _wheelOfCypher.nextCypher();
		if (cypher != null) {
			_neo4jService.runCypher(cypher);
		}

		return true;
	}

}

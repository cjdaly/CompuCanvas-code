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

package net.locosoft.CompuCanvas.controller.Neo4j.internal;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.Neo4j.INeo4jService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.util.C3Util;

public class Neo4jService extends AbstractC3Service implements INeo4jService {

	private Driver _driver;
	private Session _session;
	private Neo4jFeeder _feeder;

	// INeo4jService

	public void runCypher(Cypher cypher) {
		if (_feeder != null) {
			_feeder.enqueueCypher(cypher);
		}
	}

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return INeo4jService.class;
	}

	public void serviceStart() {
		try {
			_driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "neo4j"));
			_session = _driver.session();
			_feeder = new Neo4jFeeder(_session);
		} catch (Exception ex) {
			C3Util.log(ex.toString());
		}
	}

	public void serviceStop() {
		if (_feeder != null) {
			_feeder.stop(true);
		}
		if (_session != null) {
			_session.close();
		}
		if (_driver != null) {
			_driver.close();
		}
	}

}

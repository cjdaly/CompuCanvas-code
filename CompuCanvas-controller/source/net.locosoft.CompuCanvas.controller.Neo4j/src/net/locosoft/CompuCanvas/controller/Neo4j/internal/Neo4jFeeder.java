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

import java.util.LinkedList;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.exceptions.Neo4jException;

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class Neo4jFeeder extends MonitorThread {

	private Session _session;
	private LinkedList<Cypher> _cypherQueue = new LinkedList<Cypher>();
	private Neo4jException _lastEx = null;

	public Neo4jFeeder(Session session) {
		_session = session;
	}

	synchronized void enqueueCypher(Cypher cypher) {
		_cypherQueue.add(cypher);
	}

	synchronized Cypher dequeueCypher() {
		if (_cypherQueue.isEmpty())
			return null;
		else
			return _cypherQueue.removeFirst();
	}

	public boolean cycle() throws Exception {

		Cypher cypher = dequeueCypher();
		if (cypher == null)
			return true;

		if ((_session == null) || (!_session.isOpen()))
			return true;

		try {
			if (_lastEx != null) {
				Thread.sleep(5000);
				_lastEx = null;
			}

			StatementResult result = null;
			if (cypher.useTransaction()) {
				try (Transaction tx = _session.beginTransaction()) {
					result = tx.run(cypher.getText(), cypher.getParams());
					tx.success();
				}
			} else {
				result = _session.run(cypher.getText(), cypher.getParams());
			}
			if (result != null) {
				cypher.handleResult(result);
			}
		} catch (Neo4jException ex) {
			C3Util.log(ex.toString());
			_lastEx = ex;
		}

		return true;
	}
}

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

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.summary.ResultSummary;

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.util.C3Util;

public class SleepIndex extends WheelOfCypher.Cog {

	private static final String[] _Indices = { //
			"CREATE INDEX ON :Impression(timeValue)", //
			"CREATE INDEX ON :Impressor(chainIndex , linkIndex)" //
	};

	private int _indexIndex = 0;

	public Cypher newCypher() {

		Cypher cypher = new Cypher() {
			int _index = _indexIndex;

			public String getText() {
				return _Indices[_index];
			}

			protected void handle(StatementResult result) {
				if (isLoggingEnabled()) {
					ResultSummary summary = result.summary();
					C3Util.log(getSchemaSummaryText(summary.counters()) + ", idx: " + _index);
				}
			}
		};

		_indexIndex++;
		if (_indexIndex >= _Indices.length) {
			_indexIndex = 0;
		}

		return cypher;
	}

}

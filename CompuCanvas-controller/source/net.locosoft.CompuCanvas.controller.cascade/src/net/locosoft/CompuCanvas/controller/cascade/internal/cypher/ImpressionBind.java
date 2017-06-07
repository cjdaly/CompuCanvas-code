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

public class ImpressionBind extends WheelOfCypher.Cog {

	private static final String _ImpressionLimit = "LIMIT 16";

	public Cypher newCypher() {
		Cypher cypher = new Cypher() {

			public String getText() {
				return "    MERGE (r:Impressor { chainIndex:$chainIndex })" //
						+ " ON CREATE SET r.linkIndex = 0, r.linkIndexCounter = 0" //
						+ " ON MATCH SET r.linkIndexCounter = 0" //
						+ " WITH r" //
						+ " MATCH (n:Impression)" //
						+ " WITH r, n" //
						+ " ORDER BY n.timeValue DESC " + _ImpressionLimit //
						+ " FOREACH (n | " //
						+ "   MATCH (r1:Impressor { linkIndex:r.linkIndexCounter})" //
						+ "   MERGE (r1)-[:ImpressorLink]->(r2:Impressor { linkIndex:r.linkIndexCounter+1})" //
						+ "   WITH r, r1, n" //
						+ "   CREATE (n)-[:ImpressionBind]->(r1)" //
						+ "   WITH r SET r.linkIndexCounter = r.linkIndexCounter + 1" //
						+ " )";
			}

			protected void handle(StatementResult result) {
				ResultSummary summary = result.summary();
				C3Util.log(getSummaryText(summary.counters()));
			}
		};

		cypher.addParam("chainIndex", 0);

		return cypher;
	}

}

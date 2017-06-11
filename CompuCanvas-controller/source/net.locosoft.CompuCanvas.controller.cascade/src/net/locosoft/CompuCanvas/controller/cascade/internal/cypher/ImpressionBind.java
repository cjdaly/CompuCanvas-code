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
	private static final int _ChainCount = 4;
	private int _chainIndex = -1;

	public Cypher newCypher() {
		_chainIndex++;
		_chainIndex %= _ChainCount;

		Cypher cypher = new Cypher() {

			public String getText() {
				return //
				" MERGE (r0:Impressor { chainIndex:$chainIndex, linkIndex:0 })" + //
				" MATCH (imp:Impression)" + //
				" WITH imp" + //
				" ORDER BY imp.timeValue DESC " + _ImpressionLimit + //
				" WITH collect(imp) as imps" + //
				" UNWIND range(0,length(imps)-1) as idx" + //
				" WITH imps[idx] as imp, idx" + //
				" MATCH (r1:Impressor { chainIndex:$chainIndex, linkIndex:idx })" + //
				" WITH imp, idx, r1" + //
				" MERGE (r1)-[:ImpressorLink]->(r2:Impressor { chainIndex:$chainIndex, linkIndex:idx+1 })" + //
				" WITH imp, r1" + //
				" CREATE (imp)-[:ImpressionBind]->(r1)" + //
				"";
			}

			protected void handle(StatementResult result) {
				ResultSummary summary = result.summary();
				C3Util.log(getSummaryText(summary.counters()) + ", idx: " + _chainIndex);
			}
		};

		cypher.addParam("chainIndex", _chainIndex);

		return cypher;
	}

}

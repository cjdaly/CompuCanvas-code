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

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.util.C3Util;

public class SleepReport extends WheelOfCypher.Cog {

	public Cypher newCypher() {

		Cypher cypher = new Cypher() {

			public String getText() {
				return //
				" MATCH (n:Impression)" + //
				" WITH count(n) AS impressionCount" + //
				" MATCH (r:Impressor)" + //
				" RETURN impressionCount, count(r) AS impressorCount" + //
				"";
			}

			protected void handle(StatementResult result) {
				if (isLoggingEnabled()) {
					Record record = result.single();
					int impressionCount = record.get("impressionCount").asInt();
					int impressorCount = record.get("impressorCount").asInt();
					C3Util.log("SleepReport" + //
					" - Impressions: " + impressionCount + //
					", Impressors: " + impressorCount //
					);
				}
			}
		};

		return cypher;
	}
}

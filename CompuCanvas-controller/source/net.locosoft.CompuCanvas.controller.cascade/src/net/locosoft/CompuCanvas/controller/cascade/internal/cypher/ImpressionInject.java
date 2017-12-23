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

import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.summary.ResultSummary;

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDBuffer;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDValue;
import net.locosoft.CompuCanvas.controller.util.C3Util;

public class ImpressionInject extends WheelOfCypher.Cog {

	private long _lastImpressionMillis;

	public Cypher newCypher() {
		Cypher cypher = new Cypher() {

			public String getText() {
				return "UNWIND $impressionMap AS map CREATE (n:Impression) SET n = map";
			}

			protected void handle(StatementResult result) {
				if (isLoggingEnabled()) {
					ResultSummary summary = result.summary();
					C3Util.log(getSummaryText(summary.counters()));
				}
			}
		};

		TSDValue[] tsdValues = getCoreService().getTSDValuesAfter(_lastImpressionMillis);

		@SuppressWarnings("unchecked")
		Map<String, Object>[] impressionMap = new Map[tsdValues.length];

		int i = 0;
		for (TSDValue tsdValue : tsdValues) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("timeMillis", tsdValue.getTime());
			map.put("type", tsdValue.getType().getOrdinal());
			switch (tsdValue.getType()) {
			case String:
				map.put("value", tsdValue.asString());
				break;
			case StringArray:
				map.put("value", tsdValue.asStrings());
				break;
			case Long:
				map.put("value", tsdValue.asLong());
				break;
			case LongArray:
				map.put("value", tsdValue.asLongs());
				break;
			case Double:
				map.put("value", tsdValue.asDouble());
				break;
			case DoubleArray:
				map.put("value", tsdValue.asDoubles());
				break;
			}
			TSDBuffer tsdBuffer = tsdValue.getBuffer();
			map.put("path", tsdBuffer.getHashKey());

			impressionMap[i] = map;
			i++;

			if (_lastImpressionMillis < tsdValue.getTime()) {
				_lastImpressionMillis = tsdValue.getTime();
			}
		}

		cypher.addParam("impressionMap", impressionMap);

		return cypher;
	}
}

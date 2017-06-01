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

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.core.ICoreService;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDBuffer;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDValue;

public class ImpressionInject extends WheelOfCypher.Cog {

	private ICoreService _coreService;
	private long _lastImpressionMillis;

	public ImpressionInject(ICoreService coreService) {
		_coreService = coreService;
	}

	public Cypher newCypher() {
		Cypher cypher = new Cypher() {

			public String getText() {
				return "UNWIND $impressionMap AS map CREATE (n:Impression) SET n = map";
			}
		};

		TSDValue[] tsdValues = _coreService.getTSDValuesAfter(_lastImpressionMillis);

		@SuppressWarnings("unchecked")
		Map<String, Object>[] impressionMap = new Map[tsdValues.length];

		int i = 0;
		for (TSDValue tsdValue : tsdValues) {
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("timeValue", tsdValue.getTime());
			switch (tsdValue.getType()) {
			case String:
				map.put("stringValue", tsdValue.asString());
				break;
			case Long:
				map.put("longValue", tsdValue.asLong());
				break;
			case Double:
				map.put("doubleValue", tsdValue.asDouble());
				break;
			}
			TSDBuffer tsdBuffer = tsdValue.getBuffer();
			map.put("units", tsdBuffer.getUnits());
			map.put("buffer", tsdBuffer.getId());
			TSDGroup tsdGroup = tsdBuffer.getGroup();
			map.put("group", tsdGroup.getId());
			map.put("service", tsdGroup.getService().getServiceId());

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

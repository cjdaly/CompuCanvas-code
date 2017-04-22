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

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.summary.ResultSummary;

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.Neo4j.INeo4jService;
import net.locosoft.CompuCanvas.controller.core.ICoreService;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDBuffer;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDValue;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class Cascader extends MonitorThread {

	private ICoreService _coreService;
	private INeo4jService _neo4jService;

	public Cascader(ICoreService coreService, INeo4jService neo4jService) {
		_coreService = coreService;
		_neo4jService = neo4jService;
	}

	protected long getPreSleepMillis() {
		return 3000;
	}

	public boolean cycle() throws Exception {

		TSDValue[] tsdValues = _coreService.getLatestTSDValues(8);
		if (tsdValues.length > 0) {
			TSDValue tsdValue = tsdValues[0];
			TSDValueCypher tsdCypher = new TSDValueCypher(tsdValue);
			_neo4jService.runCypher(tsdCypher);
		}

		return true;
	}

	class TSDValueCypher extends Cypher {

		private String _nodeLabel;

		TSDValueCypher(TSDValue tsdValue) {
			super(true);

			addParam("ts", tsdValue.getTime());

			switch (tsdValue.getType()) {
			case String:
				_nodeLabel = "TSD_String";
				addParam("val", tsdValue.asString());
				break;
			case Long:
				_nodeLabel = "TSD_Long";
				addParam("val", tsdValue.asLong());
				break;
			case Double:
				_nodeLabel = "TSD_Double";
				addParam("val", tsdValue.asDouble());
				break;
			}

			TSDBuffer tsdBuffer = tsdValue.getBuffer();
			addParam("buf", tsdBuffer.getId());
			TSDGroup tsdGroup = tsdBuffer.getGroup();
			addParam("grp", tsdGroup.getId());
			addParam("svc", tsdGroup.getService().getServiceId());
		}

		public String getText() {
			return "CREATE (n:" + _nodeLabel + " { ts:$ts, val:$val, buf:$buf, grp:$grp, svc:$svc }) RETURN n";
		}

		protected void handle(StatementResult result) {
			C3Util.log("Cypher: " + getText());
			while (result.hasNext()) {
				Record r = result.next();
				C3Util.log("result: " + r.toString());
			}
			ResultSummary summary = result.consume();
			C3Util.log("summary: " + summary.toString());
		}

	}

}

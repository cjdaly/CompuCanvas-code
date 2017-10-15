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

package net.locosoft.CompuCanvas.controller.Neo4j.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.locosoft.CompuCanvas.controller.core.tsd.TSDBuffer;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDType;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.ExecUtil;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class Neo4jVitals extends MonitorThread {

	private TSDBuffer _vmPeakTSDs;
	private int _neo4jPID = -1;
	private static final int _Neo4jVmPeakMax = 900000;

	public Neo4jVitals(Neo4jService service) {
		TSDGroup tsdGroup = service.serviceCreateTSDGroup("vitals");
		_vmPeakTSDs = tsdGroup.createTSDBuffer("process.neo4j.vmPeak", "kB", TSDType.Long);
	}

	protected long getPreSleepMillis() {
		return 5 * 1000;
	}

	public boolean cycle() throws Exception {

		if (_neo4jPID < 0) {
			_neo4jPID = getNeo4jPID();
		}

		if (_neo4jPID > 0) {
			int vmPeak = C3Util.getProcessVmPeak(_neo4jPID);
			if (vmPeak > _Neo4jVmPeakMax) {
				C3Util.log("!!! Neo4j VmPeak: " + vmPeak + " ;  Reboot soon!");
			}
			_vmPeakTSDs.update(vmPeak);
		}

		return true;
	}

	private static final Pattern _Neo4jPidPattern = Pattern.compile("^neo4j\\s+(\\d+)\\s+", Pattern.MULTILINE);

	private int getNeo4jPID() {
		StringBuilder processOut = new StringBuilder();
		StringBuilder processErr = new StringBuilder();
		String psCommand = "ps -f -u neo4j";
		ExecUtil.execCommand(psCommand, processOut, processErr);

		Matcher matcher = _Neo4jPidPattern.matcher(processOut);
		if (matcher.find()) {
			String pidText = matcher.group(1);
			return C3Util.parseInt(pidText, -1);
		}

		return -1;
	}
}

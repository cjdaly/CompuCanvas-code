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

package net.locosoft.CompuCanvas.controller.Show2.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.locosoft.CompuCanvas.controller.util.MonitorThread;
import net.locosoft.Show2Eboogaloo.Show2Session;

public class Show2Listener extends MonitorThread {
	private Show2Session _session;

	public Show2Listener(Show2Session session) {
		_session = session;
	}

	private static final Pattern _SensorDataPattern = Pattern.compile("!! (\\w+):(.*) !!");

	public boolean cycle() throws Exception {

		String line = _session.pullOutputLine();
		if ((line != null) && (line.startsWith("!! "))) {
			Matcher matcher = _SensorDataPattern.matcher(line);
			if (matcher.matches()) {

				// System.out.println("Show2: " + line);

				// String sensorId = matcher.group(1);
				// HashMap<String, Object> vitalsData = new HashMap<String,
				// Object>();
				// String vitalsList = matcher.group(2);
				// String[] vitalsArray = vitalsList.split(",");
				// for (String vitalKeyValue : vitalsArray) {
				// int eqPos = vitalKeyValue.indexOf('=');
				// if (eqPos != -1) {
				// String key = vitalKeyValue.substring(0, eqPos).trim();
				// String value = vitalKeyValue.substring(eqPos + 1,
				// vitalKeyValue.length()).trim();
				// vitalsData.put(key, value);
				// }
				// }
			}
		}

		return true;
	}
}

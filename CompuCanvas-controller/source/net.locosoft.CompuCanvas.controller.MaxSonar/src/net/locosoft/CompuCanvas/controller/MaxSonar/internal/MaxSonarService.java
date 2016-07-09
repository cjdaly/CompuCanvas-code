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

package net.locosoft.CompuCanvas.controller.MaxSonar.internal;

import net.locosoft.CompuCanvas.controller.MaxSonar.IMaxSonarService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDBuffer;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDType;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.ExecUtil;

public class MaxSonarService extends AbstractC3Service implements IMaxSonarService {

	private TSDGroup _maxSonar;
	private TSDBuffer _maxSonarRange;

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return IMaxSonarService.class;
	}

	public void serviceStart() {
		String devicePath = serviceGetConfig("devicePath", null);
		if (devicePath == null) {
			C3Util.log("No MaxSonar device configured.");
		} else {
			C3Util.log("MaxSonar device configured: " + devicePath);

			_maxSonar = serviceCreateTSDGroup("maxSonar");
			_maxSonarRange = _maxSonar.createTSDBuffer("range", "mm", TSDType.Long);

			String command = C3Util.getC3ScriptsDir() + "/max-sonar.sh " + devicePath;
			ExecUtil.LineReader lineReader = new ExecUtil.LineReader() {

				public void readLine(String line) {
					if (line.startsWith("MaxSonar: ")) {
						C3Util.log("MaxSonar init: " + line.substring("MaxSonar: ".length()));
					} else if (line.startsWith("R")) {
						_maxSonarRange.update(line.substring(1));
					}
				}

				public boolean isDone() {
					return false;
				}
			};
			ExecUtil.execCommand(command, lineReader, null);
		}
	}

	public void serviceStop() {
	}

}

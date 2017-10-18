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

package net.locosoft.CompuCanvas.controller.CircuitPython.internal;

import java.io.File;

import net.locosoft.CompuCanvas.controller.CircuitPython.ICircuitPythonService;
import net.locosoft.CompuCanvas.controller.CircuitPython.internal.emoter.CircuitPythonEmoter;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.emoter.IEmoterService;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.ExecUtil;

public class CircuitPythonService extends AbstractC3Service implements ICircuitPythonService {

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return ICircuitPythonService.class;
	}

	public void serviceStart() {
		registerEmoters();
	}

	public void serviceStop() {
	}

	private static final String[] _CIRCUITPY_Suffixes = { "", "1", "2", "3", "4", "5", "6", "7" };
	private static final String _DeviceTypeTag = "#device_type:";
	private static final String _DeviceIdTag = "#device_id:";

	private void registerEmoters() {
		IEmoterService emoterService = getCoreService().getService(IEmoterService.class);

		for (String suffix : _CIRCUITPY_Suffixes) {
			String mediaPath = "/media/pi/CIRCUITPY" + suffix;
			String deviceType = getDeviceInfo(mediaPath, _DeviceTypeTag);
			String deviceId = getDeviceInfo(mediaPath, _DeviceIdTag);
			if (deviceType != null) {
				C3Util.log("CircuitPython (" + mediaPath + ") deviceType: " + deviceType + ", deviceId: " + deviceId);

				switch (deviceType) {
				case "TrinketM0":
					emoterService.registerEmoter(new CircuitPythonEmoter.TrinketM0(deviceId, mediaPath));
					break;
				case "GemmaM0":
					emoterService.registerEmoter(new CircuitPythonEmoter.GemmaM0(deviceId, mediaPath));
					break;
				case "CircuitPlaygroundExpress":
					emoterService.registerEmoter(new CircuitPythonEmoter.CircuitPlaygroundExpress(deviceId, mediaPath));
					break;
				}
			}
		}
	}

	private String getDeviceInfo(String mediaPath, String tag) {

		String mainPyFilePath = mediaPath + "/main.py";
		File mainPyFile = new File(mainPyFilePath);
		if (!mainPyFile.exists())
			return null;

		StringBuilder processOut = new StringBuilder();
		StringBuilder processErr = new StringBuilder();
		ExecUtil.execCommand("grep " + tag + " " + mainPyFilePath, processOut, processErr);
		String processOutText = processOut.toString();

		if (processOutText.startsWith(tag)) {
			String deviceTypeRaw = processOutText.substring(tag.length());
			String deviceType = deviceTypeRaw.trim();
			return deviceType;
		}

		return null;
	}

}

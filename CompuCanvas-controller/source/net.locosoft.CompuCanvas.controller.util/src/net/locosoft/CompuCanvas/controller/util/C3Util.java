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

package net.locosoft.CompuCanvas.controller.util;

import java.net.URI;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class C3Util {

	public static String getCompuCanvasModelId() {
		return System.getProperty("net.locosoft.CompuCanvas.modelId");
	}

	public static String getC3InternalVersion() {
		return System.getProperty("net.locosoft.CompuCanvas.controller.internalVersion");
	}

	public static String getC3HomeDir() {
		try {
			String eclipseLocation = System.getProperty("eclipse.home.location");
			IPath eclipsePath = new Path(new URI(eclipseLocation).getPath());
			IPath c3HomePath = eclipsePath.removeLastSegments(1).removeTrailingSeparator();

			return c3HomePath.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String getC3DataDir() {
		String c3HomeDir = getC3HomeDir();
		if (c3HomeDir != null)
			return c3HomeDir + "/data";
		else
			return null;
	}

	public static String getC3ConfigDir() {
		String c3HomeDir = getC3HomeDir();
		if (c3HomeDir != null)
			return c3HomeDir + "/config";
		else
			return null;
	}

	public static int getC3Pid() {
		String c3HomeDir = getC3HomeDir();
		String c3PIDFile = FileUtil.readFileToString(c3HomeDir + "/c3.PID", false);
		return parseInt(c3PIDFile.trim(), -1);
	}

	public static int parseInt(String value, int defaultValue) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			return defaultValue;
		}
	}

}

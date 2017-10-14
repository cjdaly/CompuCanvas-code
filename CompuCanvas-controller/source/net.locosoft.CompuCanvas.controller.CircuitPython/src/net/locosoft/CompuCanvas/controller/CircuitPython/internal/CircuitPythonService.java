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
import java.util.HashMap;

import net.locosoft.CompuCanvas.controller.CircuitPython.ICircuitPythonService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;

public class CircuitPythonService extends AbstractC3Service implements ICircuitPythonService {

	private REPLStarter _replStarter;
	private HashMap<String, REPLSession> _sessions = new HashMap<String, REPLSession>();
	private boolean _done = false;

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return ICircuitPythonService.class;
	}

	private void startREPLSession(String acmDevPath) {
		REPLSession session = new REPLSession(this, acmDevPath);
		_sessions.put(acmDevPath, session);
		session.start();
	}

	public void serviceStart() {
		_replStarter = new REPLStarter();
		_replStarter.start();
	}

	public void serviceStop() {
		_done = true;
		for (REPLSession session : _sessions.values()) {
			session.stop();
		}
	}

	private class REPLStarter extends Thread {
		public void run() {
			while (!_done) {
				try {
					for (int acmNum = 0; acmNum < 10; acmNum++) {
						String acmDevPath = "/dev/ttyACM" + acmNum;
						File acmDevFile = new File(acmDevPath);
						if (acmDevFile.exists()) {
							startREPLSession(acmDevPath);
							Thread.sleep(1000 * 30);
							acmNum++;
						}
					}
				} catch (InterruptedException ex) {
				}
			}
		}
	}
}

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

import net.locosoft.CompuCanvas.controller.CircuitPython.ICircuitPythonService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;

public class CircuitPythonService extends AbstractC3Service implements ICircuitPythonService {

	private REPLSession _session;

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return ICircuitPythonService.class;
	}

	public void serviceStart() {
		_session = new REPLSession(this);
		_session.start();
	}

	public void serviceStop() {
		if (_session != null) {
			_session.stop();
		}
	}
}

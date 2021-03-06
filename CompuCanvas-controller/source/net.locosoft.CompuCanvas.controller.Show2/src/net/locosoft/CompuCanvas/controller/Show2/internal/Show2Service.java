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

import net.locosoft.CompuCanvas.controller.Show2.IShow2Service;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.Show2Eboogaloo.Show2Session;

public class Show2Service extends AbstractC3Service implements IShow2Service {

	private Show2Session _session;
	private Show2CommandTSDGroup _commandTSDs;
	private Show2Listener _listener;
	private Show2Feeder _feeder;

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return IShow2Service.class;
	}

	public void serviceStart() {
		String devicePath = serviceGetConfig("devicePath", null);
		if (devicePath == null) {
			C3Util.log("No Show2 device configured.");
		} else {
			C3Util.log("Show2 device configured: " + devicePath);

			_session = new Show2Session(devicePath);
			_session.start();

			_commandTSDs = new Show2CommandTSDGroup(this);

			_listener = new Show2Listener(this, _session, _commandTSDs);
			_listener.start();

			_feeder = new Show2Feeder(this, _session, _commandTSDs);
			_feeder.start();
		}
	}

	public void serviceStop() {
		if (_feeder != null) {
			_feeder.stop(true);
		}
		if (_listener != null) {
			_listener.stop();
		}
		if (_session != null) {
			_session.stop();
		}
	}
}

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

package net.locosoft.CompuCanvas.controller.audio.internal;

import net.locosoft.CompuCanvas.controller.audio.IAudioService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.util.C3Util;

public class AudioService extends AbstractC3Service implements IAudioService {

	private AudioFeeder _feeder;

	// IAudioService

	public void speak(String message) {
		if (_feeder != null) {
			String command = C3Util.getC3ScriptsDir() + "/espeaker.sh '" + message + "'";
			_feeder.enqueueCommand(command);
		}
	}

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return IAudioService.class;
	}

	public void serviceStart() {
		_feeder = new AudioFeeder();
		_feeder.start();
	}

	public void serviceStop() {
		if (_feeder != null) {
			_feeder.stop();
		}
	}
}

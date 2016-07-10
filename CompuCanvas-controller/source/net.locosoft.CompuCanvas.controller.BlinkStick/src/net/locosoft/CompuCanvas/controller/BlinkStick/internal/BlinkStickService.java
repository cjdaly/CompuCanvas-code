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

package net.locosoft.CompuCanvas.controller.BlinkStick.internal;

import java.util.ArrayList;

import net.locosoft.CompuCanvas.controller.BlinkStick.IBlinkStick;
import net.locosoft.CompuCanvas.controller.BlinkStick.IBlinkStickService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.ExecUtil;

public class BlinkStickService extends AbstractC3Service implements IBlinkStickService {

	private BlinkStickFeeder _blinkStickFeeder;
	private RandomBlinker _randomBlinker;

	private ArrayList<BlinkStick> _blinkStickArray = new ArrayList<BlinkStick>();

	// IBlinkStickService

	public int getBlinkStickCount() {
		return _blinkStickArray.size();
	}

	public IBlinkStick getBlinkStick(int index) {
		return _blinkStickArray.get(index);
	}

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return IBlinkStickService.class;
	}

	public void serviceStart() {
		detectBlinkSticks();
		if (getBlinkStickCount() > 0) {
			_blinkStickFeeder = new BlinkStickFeeder(this);
			_blinkStickFeeder.start();

			_randomBlinker = new RandomBlinker(this);
			_randomBlinker.start();
		}
	}

	public void serviceStop() {
		if (getBlinkStickCount() == 0)
			return;

		_randomBlinker.stop(true);

		_blinkStickFeeder.stopSequence();
		_blinkStickFeeder.stop(true);
	}

	private void detectBlinkSticks() {
		StringBuilder blinkStickOut = new StringBuilder();
		StringBuilder blinkStickErr = new StringBuilder();
		String blinkStickInfoCommand = "blinkstick -i";
		int result = ExecUtil.execCommand(blinkStickInfoCommand, blinkStickOut, blinkStickErr);
		if (result != 0) {
			C3Util.logExecResult(result, blinkStickInfoCommand, blinkStickOut.toString(), blinkStickErr.toString());
		} else {
			String infoTmp = blinkStickOut.toString();
			int index = infoTmp.lastIndexOf("Found device:");
			while (index >= 0) {
				String infoText = infoTmp.substring(index);
				BlinkStick blinkStick = new BlinkStick(this, infoText);
				_blinkStickArray.add(blinkStick);
				C3Util.log("BlinkStick detected: " + //
						blinkStick.getSerial() + //
						" (kind: " + blinkStick.getKind() + //
						", mode: " + blinkStick.getMode() + ")");
				infoTmp = infoTmp.substring(0, index);
				index = infoTmp.lastIndexOf("Found device:");
			}
		}
	}

	void enqueueCommand(String command) {
		if (_blinkStickFeeder != null && !_blinkStickFeeder.isStopped()) {
			_blinkStickFeeder.enqueueCommand(command);
		}
	}

}

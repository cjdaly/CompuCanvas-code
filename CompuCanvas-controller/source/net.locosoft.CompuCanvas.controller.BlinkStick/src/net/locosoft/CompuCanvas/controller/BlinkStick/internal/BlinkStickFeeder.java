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

import net.locosoft.CompuCanvas.controller.BlinkStick.IBlinkStick;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDBuffer;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDType;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.CommandLineQueue;
import net.locosoft.CompuCanvas.controller.util.ExecUtil;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class BlinkStickFeeder extends MonitorThread {

	private BlinkStickService _service;
	private TSDGroup _tsdGroup;
	private TSDBuffer _inputBuffer;
	private CommandLineQueue _commandLineQueue = new CommandLineQueue();

	public BlinkStickFeeder(BlinkStickService service) {
		_service = service;
		_tsdGroup = service.serviceCreateTSDGroup("blinkstick");
		_inputBuffer = _tsdGroup.createTSDBuffer("input", "commands", TSDType.String);
		_inputBuffer.setPropagateValues(false);
	}

	void enqueueCommand(String command) {
		_commandLineQueue.enqueueCommand(command);
	}

	public boolean cycle() throws Exception {
		String blinkStickCommand = _commandLineQueue.dequeueCommand();

		if (blinkStickCommand != null) {
			StringBuilder blinkStickOut = new StringBuilder();
			StringBuilder blinkStickErr = new StringBuilder();

			_inputBuffer.update(blinkStickCommand);
			int result = ExecUtil.execCommand(blinkStickCommand, blinkStickOut, blinkStickErr);

			if (result != 0) {
				C3Util.logExecResult(result, blinkStickCommand, blinkStickOut.toString(), blinkStickErr.toString());
			}
		}

		if (_commandLineQueue.countCommands() > 32) {
			C3Util.log("BlinkStick service dumping command queue!");
			_commandLineQueue.clearCommands();
		}

		return true;
	}

	void stopSequence() {
		_commandLineQueue.clearCommands();

		for (int i = 0; i < _service.getBlinkStickCount(); i++) {
			IBlinkStick blinkStick = _service.getBlinkStick(i);
			blinkStick.setLED(-1, "off");
		}

		String nextCommand = _commandLineQueue.peekCommand();
		while (nextCommand != null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//
			}
			nextCommand = _commandLineQueue.peekCommand();
		}

	}
}

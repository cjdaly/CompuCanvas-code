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

import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.CommandLineQueue;
import net.locosoft.CompuCanvas.controller.util.ExecUtil;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class AudioFeeder extends MonitorThread {

	private CommandLineQueue _commandLineQueue = new CommandLineQueue();

	void enqueueCommand(String command) {
		_commandLineQueue.enqueueCommand(command);
	}

	public boolean cycle() throws Exception {
		String audioCommand = _commandLineQueue.dequeueCommand();

		if (audioCommand != null) {
			StringBuilder audioOut = new StringBuilder();
			StringBuilder audioErr = new StringBuilder();
			int result = ExecUtil.execCommand(audioCommand, audioOut, audioErr);
			if (result != 0) {
				C3Util.logExecResult(result, audioCommand, audioOut.toString(), audioErr.toString());
			}
		} else {
			// int hello = ThreadLocalRandom.current().nextInt(100);
			// if (hello == 99) {
			// String command = C3Util.getC3ScriptsDir() + "/espeaker.sh
			// 'hello'";
			// enqueueCommand(command);
			// }
		}

		if (_commandLineQueue.countCommands() > 8) {
			C3Util.log("Audio service dumping command queue!");
			_commandLineQueue.clearCommands();
		}

		return true;
	}

}

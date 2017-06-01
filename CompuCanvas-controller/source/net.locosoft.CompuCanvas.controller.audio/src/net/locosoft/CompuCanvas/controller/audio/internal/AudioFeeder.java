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

	private AudioService _service;
	private CommandLineQueue _commandLineQueue = new CommandLineQueue();
	private int _testMP3Countdown;
	private int _testMP3Count;

	public AudioFeeder(AudioService service) {
		_service = service;
	}

	void enqueueCommand(String command) {
		_commandLineQueue.enqueueCommand(command);
	}

	public void beginCycle() throws Exception {
		String[] mp3Ids = _service.getMP3Ids();
		StringBuilder sb = null;
		for (String mp3Id : mp3Ids) {
			if (sb == null) {
				sb = new StringBuilder(mp3Id);
			} else {
				sb.append(", ");
				sb.append(mp3Id);
			}
		}
		if (sb != null) {
			C3Util.log("audio MP3 IDs: " + sb.toString());
		}

		_testMP3Countdown = _service.serviceGetConfigInt("test.mp3.countdown", -1);
		_testMP3Count = _testMP3Countdown;
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

			if (_testMP3Count > 0) {
				_testMP3Count--;
				if (_testMP3Count == 0) {
					_testMP3Count = _testMP3Countdown;
					_service.playMP3("lonewolf");
				}
			}
		}

		if (_commandLineQueue.countCommands() > 8) {
			C3Util.log("Audio service dumping command queue!");
			_commandLineQueue.clearCommands();
		}

		return true;
	}

}

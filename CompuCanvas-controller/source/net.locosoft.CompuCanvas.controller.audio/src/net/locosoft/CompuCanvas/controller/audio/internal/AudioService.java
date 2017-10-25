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

import java.io.File;
import java.util.ArrayList;

import net.locosoft.CompuCanvas.controller.audio.IAudioService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;

public class AudioService extends AbstractC3Service implements IAudioService {

	private AudioFeeder _feeder;

	// IAudioService

	public void speak(String message) {
		if (_feeder != null) {
			String command = serviceGetContentDir() + "/espeaker.sh '" + message + "'";
			_feeder.enqueueCommand(command);
		}
	}

	private final String _NA_DIR = serviceGetContentDir() + "/NoAgenda";
	private ArrayList<String> _mp3IDs;

	public String[] getMP3Ids() {
		if (_mp3IDs == null) {
			_mp3IDs = new ArrayList<String>();
			File mp3Dir = new File(_NA_DIR);
			if (mp3Dir.isDirectory()) {
				File[] files = mp3Dir.listFiles();
				for (File file : files) {
					String fileName = file.getName();
					if (fileName.endsWith(".mp3")) {
						_mp3IDs.add(fileName.substring(0, fileName.length() - 4));
					}
				}
			}
		}
		return (String[]) _mp3IDs.toArray(new String[_mp3IDs.size()]);
	}

	public void playMP3(String id) {
		if (_feeder != null) {
			String mp3FilePath = _NA_DIR + "/" + id + ".mp3";
			File mp3File = new File(mp3FilePath);
			if (mp3File.exists()) {
				String command = "mpg321 " + mp3File;
				_feeder.enqueueCommand(command);
			}
		}
	}

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return IAudioService.class;
	}

	public void serviceStart() {
		_feeder = new AudioFeeder(this);
		_feeder.start();
	}

	public void serviceStop() {
		if (_feeder != null) {
			_feeder.stop();
		}
	}
}

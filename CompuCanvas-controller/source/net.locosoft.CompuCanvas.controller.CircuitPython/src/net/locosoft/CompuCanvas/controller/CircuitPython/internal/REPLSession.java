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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.locosoft.CompuCanvas.controller.util.C3Util;

public class REPLSession {

	private CircuitPythonService _service;
	private String _devicePath;
	private REPLReader _reader;
	private REPLWriter _writer;
	private boolean _done = false;
	private String _deviceType = null;
	private String _osUname = null;
	// private LinkedList<String> _outputLines = new LinkedList<String>();

	public REPLSession(CircuitPythonService service) {
		this(service, "/dev/ttyACM0");
	}

	public REPLSession(CircuitPythonService service, String devicePath) {
		_service = service;
		_devicePath = devicePath;
	}

	public boolean isDone() {
		return _done;
	}

	public void start() {
		File deviceFile = new File(_devicePath);
		if (deviceFile.exists()) {
			_reader = new REPLReader();
			_reader.start();

			// _writer = new REPLWriter();
			// _writer.start();
		} else {
			C3Util.log("CircuitPython device not found at" + _devicePath);
			_done = true;
		}
	}

	public void stop() {
		_done = true;
	}

	private void setDeviceType(String trimLine) {
		if (trimLine.startsWith("'Adafruit Trinket")) {
			_deviceType = "Trinket";
		} else if (trimLine.startsWith("'Adafruit Gemma")) {
			_deviceType = "Gemma";
		} else if (trimLine.startsWith("'Adafruit CircuitPlayground")) {
			_deviceType = "CircuitPlayground";
		}
		if (_deviceType != null) {
			C3Util.log("CircuitPython (" + _devicePath + ") deviceType: " + _deviceType);
		}
	}

	private void setOsUname(String trimLine) {
		if (trimLine.startsWith("(sysname=")) {
			_osUname = trimLine;
			C3Util.log("CircuitPython (" + _devicePath + ") os.uname(): " + _osUname);
		}
	}

	private class REPLReader extends Thread {
		public void run() {
			try (BufferedReader reader = new BufferedReader(new FileReader(_devicePath))) {
				Thread.sleep(5000);
				do {
					String line = reader.readLine();
					if (line != null) {
						String trimLine = line.trim();
						if (!"".equals(trimLine)) {
							if (_service.serviceIsLoggingEnabled("repl.reads")) {
								C3Util.log("CircuitPython (" + _devicePath + ") REPL: " + line);
							}
						}
					}

					Thread.sleep(100);
				} while (!_done);

			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (InterruptedException ex) {
			}
		}
	}

	private class REPLWriter extends Thread {
		public void run() {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(_devicePath, true))) {
				Thread.sleep(8000);

				// writer.write(3); // Ctrl-c
				// writer.flush();
				// Thread.sleep(1000);
				//
				// writer.write('\r');
				// writer.flush();
				// Thread.sleep(1000);
				//
				// writer.write(3); // Ctrl-c
				// writer.flush();
				// Thread.sleep(1000);

				writer.write('\r');
				writer.flush();
				Thread.sleep(1000);

				writer.write("import os\r");
				writer.flush();
				Thread.sleep(1000);

				writer.write("os.uname()\r");
				writer.flush();
				Thread.sleep(1000);

				writer.write("os.uname().machine\r");
				writer.flush();
				Thread.sleep(1000);

				do {
					// write...

					Thread.sleep(1000);
				} while (!_done);

			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (InterruptedException ex) {
			}
		}
	}

}

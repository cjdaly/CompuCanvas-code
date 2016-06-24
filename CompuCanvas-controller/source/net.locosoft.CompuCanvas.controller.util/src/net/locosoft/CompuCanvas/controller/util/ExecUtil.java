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

package net.locosoft.CompuCanvas.controller.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ExecUtil {

	public static int execCommand(String command, String processIn, OutputStream processOut, OutputStream processErr)
			throws IOException {
		int status = -1;

		Process process = Runtime.getRuntime().exec(command);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
				new BufferedOutputStream(process.getOutputStream()));
		outputStreamWriter.write(processIn.toString());
		outputStreamWriter.flush();
		outputStreamWriter.close();

		InputStream outStream = process.getInputStream();
		InputStream errStream = process.getErrorStream();

		byte[] buffer = new byte[1024];
		int bytesRead;

		while ((bytesRead = outStream.read(buffer)) != -1) {
			processOut.write(buffer, 0, bytesRead);
		}
		while ((bytesRead = errStream.read(buffer)) != -1) {
			processErr.write(buffer, 0, bytesRead);
		}

		try {
			status = process.waitFor();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		return status;
	}

	public static int execCommand(String command, StringBuilder processOut, StringBuilder processErr) {
		int status = -1;
		if (processOut == null)
			processOut = new StringBuilder();
		if (processErr == null) {
			processErr = new StringBuilder();
		}

		try {
			Process process = Runtime.getRuntime().exec(command);

			ProcessStreamReader outReader = new ProcessStreamReader(process.getInputStream(), processOut);
			ProcessStreamReader errReader = new ProcessStreamReader(process.getErrorStream(), processErr);

			outReader.start();
			errReader.start();

			status = process.waitFor();

			outReader.join();
			errReader.join();

		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return status;
	}

	public static class ProcessStreamReader extends Thread {
		private InputStream _inputStream;
		private StringBuilder _outputBuffer;

		ProcessStreamReader(InputStream inputStream, StringBuilder outputBuffer) {
			_inputStream = inputStream;
			_outputBuffer = outputBuffer;
		}

		public void run() {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(_inputStream))) {

				char[] buffer = new char[1024];

				int bytesRead;
				while ((bytesRead = reader.read(buffer)) != -1) {
					_outputBuffer.append(buffer, 0, bytesRead);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	//

	public static interface LineReader {
		public boolean isDone();

		public void readLine(String line);
	}

	public static class IgnoreLineReader implements LineReader {

		public boolean isDone() {
			return false;
		}

		public void readLine(String line) {
		}
	}

	public static class ErrorLineReader implements LineReader {
		public boolean isDone() {
			return false;
		}

		public void readLine(String line) {
			System.err.println(line);
		}
	}

	public static void execCommand(String command, LineReader outputLineReader, LineReader errorLineReader) {
		if (outputLineReader == null) {
			outputLineReader = new IgnoreLineReader();
		}
		if (errorLineReader == null) {
			errorLineReader = new ErrorLineReader();
		}

		try {
			Process process = Runtime.getRuntime().exec(command);
			ProcessLineReader outReader = new ProcessLineReader(process.getInputStream(), outputLineReader);
			ProcessLineReader errReader = new ProcessLineReader(process.getErrorStream(), errorLineReader);
			outReader.start();
			errReader.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static class ProcessLineReader extends Thread {
		private InputStream _inputStream;
		private LineReader _lineReader;

		ProcessLineReader(InputStream inputStream, LineReader lineReader) {
			_inputStream = inputStream;
			_lineReader = lineReader;
		}

		public void run() {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(_inputStream))) {
				do {
					String line = reader.readLine();
					if (line != null) {
						_lineReader.readLine(line);
					}
					Thread.sleep(10);
				} while (!_lineReader.isDone());
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}

	}

}

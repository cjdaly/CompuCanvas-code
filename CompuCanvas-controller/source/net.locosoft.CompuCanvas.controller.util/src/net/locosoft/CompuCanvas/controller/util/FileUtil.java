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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class FileUtil {

	public static Properties loadPropertiesFile(String path) {
		Properties properties = new Properties();

		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			properties.load(reader);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return properties;
	}

	public static String readFileToString(String path) {
		return readFileToString(path, true);
	}

	public static String readFileToString(String path, boolean logErrors) {
		StringBuilder outputText = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			int readRaw = reader.read();
			while (readRaw != -1) {
				char c = (char) readRaw;
				outputText.append(c);
				readRaw = reader.read();
			}
		} catch (FileNotFoundException ex) {
			if (logErrors)
				ex.printStackTrace();
		} catch (IOException ex) {
			if (logErrors)
				ex.printStackTrace();
		}
		return outputText.toString();
	}

}

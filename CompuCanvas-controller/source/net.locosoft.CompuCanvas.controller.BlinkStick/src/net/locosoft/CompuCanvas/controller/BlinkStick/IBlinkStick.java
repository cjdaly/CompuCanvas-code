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

package net.locosoft.CompuCanvas.controller.BlinkStick;

public interface IBlinkStick {

	public enum Kind {
		Nano, //
		Square, //
		Strip, //
		Squip, // square or strip
		Unknown //
	}

	public enum Mode {
		Directed, //
		Random
	}

	public String getSerial();

	public String getDescription();

	public Kind getKind();

	public Mode getMode();

	public void setMode(Mode mode);

	public int getLEDCount();

	public void setLED(int index, String color);

	public void setLED(int index, String color, int limit);

	public void morphLED(int index, String color);

	public void morphLED(int index, String color, int limit);

	public void pulseLED(int index, String color);

	public void pulseLED(int index, String color, int limit);

}

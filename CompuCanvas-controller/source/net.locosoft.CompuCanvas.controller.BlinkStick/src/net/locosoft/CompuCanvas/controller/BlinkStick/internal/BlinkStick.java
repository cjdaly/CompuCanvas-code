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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.locosoft.CompuCanvas.controller.BlinkStick.IBlinkStick;

public class BlinkStick implements IBlinkStick {

	private BlinkStickService _blinkStickService;
	private String _infoText;
	private String _serial;
	private String _description;
	private IBlinkStick.Kind _kind;
	private IBlinkStick.Mode _mode;
	private int _ledCount;

	private static final Pattern _serialPattern = Pattern.compile("Serial:\\s+(.*)");
	private static final Pattern _descriptionPattern = Pattern.compile("Description:\\s+(.*)");

	public BlinkStick(BlinkStickService blinkStickService, String infoText) {
		_blinkStickService = blinkStickService;
		_infoText = infoText;

		Matcher matcher = _serialPattern.matcher(_infoText);
		_serial = matcher.find() ? matcher.group(1) : "?";

		matcher = _descriptionPattern.matcher(_infoText);
		_description = matcher.find() ? matcher.group(1) : "?";

		if (_description.contains("Nano")) {
			_kind = Kind.Nano;
		} else if (_description.contains("BlinkStick")) {
			_kind = Kind.Squip;
		} else {
			_kind = Kind.Unknown;
		}

		_mode = Mode.Random2;

		switch (_kind) {
		case Square:
		case Strip:
		case Squip:
			_ledCount = 8;
			break;
		case Nano:
			_ledCount = 2;
			break;
		case Unknown:
		default:
			_ledCount = 1;
		}
	}

	public String getSerial() {
		return _serial;
	}

	public String getDescription() {
		return _description;
	}

	public IBlinkStick.Kind getKind() {
		return _kind;
	}

	public synchronized Mode getMode() {
		return _mode;
	}

	public synchronized void setMode(Mode mode) {
		_mode = mode;
	}

	public int getLEDCount() {
		return _ledCount;
	}

	public void setLED(int index, String color) {
		setLED(index, color, 80);
	}

	public void setLED(int index, String color, int limit) {
		index = fixIndex(index);
		limit = fixLimit(limit);
		if (index == -1) {
			for (int i = 0; i < getLEDCount(); i++) {
				setLEDHelper(i, color, limit);
			}
		} else {
			setLEDHelper(index, color, limit);
		}
	}

	private void setLEDHelper(int index, String color, int limit) {
		if ("off".equals(color)) {
			_blinkStickService.enqueueCommand( //
					"blinkstick -s " + getSerial() + //
							" --index " + index + " " //
							+ color);
		} else {
			_blinkStickService.enqueueCommand( //
					"blinkstick -s " + getSerial() + //
							" --index " + index + //
							" --limit " + limit + " " //
							+ color);
		}
	}

	public void morphLED(int index, String color) {
		morphLED(index, color, 80);
	}

	public void morphLED(int index, String color, int limit) {
		index = fixIndex(index);
		limit = fixLimit(limit);
		if (index == -1) {
			for (int i = 0; i < getLEDCount(); i++) {
				morphLEDHelper(i, color, limit);
			}
		} else {
			morphLEDHelper(index, color, limit);
		}
	}

	private void morphLEDHelper(int index, String color, int limit) {
		_blinkStickService.enqueueCommand( //
				"blinkstick -s " + getSerial() + //
						" --index " + index + //
						" --limit " + limit + //
						" --morph " //
						+ color);
	}

	public void pulseLED(int index, String color) {
		pulseLED(index, color, 80);
	}

	public void pulseLED(int index, String color, int limit) {
		index = fixIndex(index);
		limit = fixLimit(limit);

		if (index == -1) {
			for (int i = 0; i < getLEDCount(); i++) {
				pulseLEDHelper(i, color, limit);
			}
		} else {
			pulseLEDHelper(index, color, limit);
		}
	}

	private void pulseLEDHelper(int index, String color, int limit) {
		_blinkStickService.enqueueCommand( //
				"blinkstick -s " + getSerial() + //
						" --index " + index + //
						" --limit " + limit + //
						" --pulse " //
						+ color);
	}

	private int fixIndex(int index) {
		if (index < 0)
			return -1;
		if (index >= getLEDCount())
			return 0;
		return index;
	}

	private int fixLimit(int limit) {
		if (limit < 0)
			return 0;
		if (limit > 100)
			return 100;
		return limit;
	}

}

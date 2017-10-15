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

package net.locosoft.CompuCanvas.controller.CircuitPython.internal.emoter;

import net.locosoft.CompuCanvas.controller.emoter.AbstractEmoter;

public class CircuitPythonEmoter extends AbstractEmoter {

	private String _mediaPath;

	public CircuitPythonEmoter(String id, String mediaPath) {
		super(id);
		_mediaPath = mediaPath;
	}

	public String getMediaPath() {
		return _mediaPath;
	}

	public static class TrinketM0 extends CircuitPythonEmoter {
		public TrinketM0(String id, String mediaPath) {
			super(id, mediaPath);
		}
	}

	public static class GemmaM0 extends CircuitPythonEmoter {
		public GemmaM0(String id, String mediaPath) {
			super(id, mediaPath);
		}
	}

	public static class CircuitPlaygroundExpress extends CircuitPythonEmoter {
		public CircuitPlaygroundExpress(String id, String mediaPath) {
			super(id, mediaPath);
		}
	}
}

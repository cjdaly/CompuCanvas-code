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

import java.util.Map;

import net.locosoft.CompuCanvas.controller.emoter.AbstractEmote;

public abstract class NeoPixelEmote extends AbstractEmote {

	public NeoPixelEmote(String[] supportedSpectrumIds) {
		super(supportedSpectrumIds);
	}

	public static class Steady extends NeoPixelEmote {

		public Steady() {
			super(new String[] { "colors8" });
		}

		public boolean init() {
			return true;
		}

		public void invoke(Map<String, Object> spectrumOptions) {
		}

	}

	public static class Pulse extends NeoPixelEmote {

		public Pulse() {
			super(new String[] { "colors8" });
		}

		public boolean init() {
			return true;
		}

		public void invoke(Map<String, Object> spectrumOptions) {
		}

	}

}

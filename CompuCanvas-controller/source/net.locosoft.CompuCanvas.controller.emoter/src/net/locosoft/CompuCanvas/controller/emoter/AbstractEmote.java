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

package net.locosoft.CompuCanvas.controller.emoter;

import java.util.Map;

public abstract class AbstractEmote {

	private String[] _spectrumIds;

	public AbstractEmote(String[] supportedSpectrumIds) {
		_spectrumIds = supportedSpectrumIds;
	}

	public String[] getSupportedSpectrumIds() {
		return _spectrumIds;
	}

	public boolean init() {
		return true;
	}

	public abstract void invoke(Map<String, Object> spectrumOptions);

}

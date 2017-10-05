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

package net.locosoft.CompuCanvas.controller.emoter.internal;

import java.util.HashMap;

import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.emoter.IEmoter;
import net.locosoft.CompuCanvas.controller.emoter.IEmoterService;
import net.locosoft.CompuCanvas.controller.emoter.ISpectrum;
import net.locosoft.CompuCanvas.controller.util.C3Util;

public class EmoterService extends AbstractC3Service implements IEmoterService {

	private HashMap<String, Emoter> _emoters = new HashMap<String, Emoter>();
	private HashMap<String, DiscreteSpectrum> _spectra = new HashMap<String, DiscreteSpectrum>();

	public IEmoter registerEmoter(String id) {

		if (_emoters.containsKey(id)) {
			C3Util.log("EmoterService ignoring duplicate registerEmoter ID: " + id);
			return null;
		} else {
			Emoter emoter = new Emoter(id);
			_emoters.put(id, emoter);
			return emoter;
		}
	}

	public String[] getSpectrumIds() {
		return _spectra.keySet().toArray(new String[0]);
	}

	public ISpectrum getSpectrum(String id) {
		return _spectra.get(id);
	}

	//

	void registerSpectrum(DiscreteSpectrum spectrum) {
		_spectra.put(spectrum.getId(), spectrum);
	}

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return IEmoterService.class;
	}

	public void serviceStart() {
		registerSpectrum(new DiscreteSpectrum("elements", //
				new String[] { "earth", "water", "air", "fire", "spirit" }));
		registerSpectrum(new DiscreteSpectrum("colors8", //
				new String[] { "black", "red", "green", "yellow", "blue", "magenta", "cyan", "white" }));

	}

	public void serviceStop() {
	}
}

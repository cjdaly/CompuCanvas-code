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
import net.locosoft.CompuCanvas.controller.emoter.AbstractEmoter;
import net.locosoft.CompuCanvas.controller.emoter.IEmoterService;
import net.locosoft.CompuCanvas.controller.emoter.ISpectrum;
import net.locosoft.CompuCanvas.controller.util.C3Util;

public class EmoterService extends AbstractC3Service implements IEmoterService {

	private HashMap<String, AbstractEmoter> _emoters = new HashMap<String, AbstractEmoter>();
	private HashMap<String, DiscreteSpectrum> _spectra = new HashMap<String, DiscreteSpectrum>();

	public void registerEmoter(AbstractEmoter emoter) {

		String id = emoter.getId();

		if (_emoters.containsKey(id)) {
			C3Util.log("EmoterService ignoring duplicate registerEmoter ID: " + id);
		} else {
			_emoters.put(id, emoter);
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

		registerSpectrum(new Analog65535ContinuousSpectrum());
	}

	public void serviceStop() {
	}
}

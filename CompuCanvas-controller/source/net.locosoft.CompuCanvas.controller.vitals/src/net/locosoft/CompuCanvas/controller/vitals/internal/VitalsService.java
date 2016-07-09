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

package net.locosoft.CompuCanvas.controller.vitals.internal;

import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.vitals.IVitalsService;

public class VitalsService extends AbstractC3Service implements IVitalsService {

	private VitalsReader _vitalsReader;

	public Class<? extends IC3Service> getServiceInterface() {
		return IVitalsService.class;
	}

	public void serviceStart() {
		_vitalsReader = new VitalsReader(this);
		_vitalsReader.start();
	}

	public void serviceStop() {
		if (_vitalsReader != null) {
			_vitalsReader.stop();
		}
	}
}

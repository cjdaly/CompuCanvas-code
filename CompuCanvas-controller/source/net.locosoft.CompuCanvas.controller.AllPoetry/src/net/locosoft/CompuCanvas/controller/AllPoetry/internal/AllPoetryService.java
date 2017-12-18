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

package net.locosoft.CompuCanvas.controller.AllPoetry.internal;

import net.locosoft.CompuCanvas.controller.AllPoetry.IAllPoetryService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.util.C3Util;

public class AllPoetryService extends AbstractC3Service implements IAllPoetryService {

	private PoemReader _poemReader;

	// IAllPoetryService

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return IAllPoetryService.class;
	}

	public void serviceStart() {
		String isEnabled = serviceGetConfig("isEnabled", "false");
		if ("true".equals(isEnabled)) {
			_poemReader = new PoemReader(this);
			_poemReader.start();
		} else {
			C3Util.log("AllPoetry service is disabled.");
		}
	}

	public void serviceStop() {
		if (_poemReader != null) {
			_poemReader.stop();
		}
	}
}

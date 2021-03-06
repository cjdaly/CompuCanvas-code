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

package net.locosoft.CompuCanvas.controller.cascade.internal;

import net.locosoft.CompuCanvas.controller.cascade.ICascadeService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.util.C3Util;

public class CascadeService extends AbstractC3Service implements ICascadeService {

	private Cascade _cascade;

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return ICascadeService.class;
	}

	public void serviceStart() {
		String isEnabled = serviceGetConfig("isEnabled", "false");
		if ("true".equals(isEnabled)) {
			_cascade = new Cascade(this);
			_cascade.start();
		} else {
			C3Util.log("Cascade service is disabled.");
		}
	}

	public void serviceStop() {
		if (_cascade != null) {
			_cascade.stop();
		}
	}
}

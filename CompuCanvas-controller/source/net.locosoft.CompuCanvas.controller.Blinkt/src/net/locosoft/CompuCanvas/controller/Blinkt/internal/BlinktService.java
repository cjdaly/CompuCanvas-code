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

package net.locosoft.CompuCanvas.controller.Blinkt.internal;

import net.locosoft.CompuCanvas.controller.Blinkt.IBlinktService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;

public class BlinktService extends AbstractC3Service implements IBlinktService {
	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return IBlinktService.class;
	}

	public void serviceStart() {
	}

	public void serviceStop() {
	}
}

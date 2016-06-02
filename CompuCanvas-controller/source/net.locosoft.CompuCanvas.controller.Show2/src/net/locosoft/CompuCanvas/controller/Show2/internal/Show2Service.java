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

package net.locosoft.CompuCanvas.controller.Show2.internal;

import net.locosoft.CompuCanvas.controller.Show2.IShow2Service;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;

public class Show2Service extends AbstractC3Service implements IShow2Service {

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return IShow2Service.class;
	}

}

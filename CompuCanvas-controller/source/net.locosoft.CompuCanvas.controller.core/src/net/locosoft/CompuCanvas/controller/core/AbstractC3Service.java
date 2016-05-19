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

package net.locosoft.CompuCanvas.controller.core;

public abstract class AbstractC3Service implements IC3ServiceInternal {

	private String _id;

	public String getServiceId() {
		return _id;
	}

	public void serviceRegister(String id) {
		_id = id;
	}

	public void serviceStart() {
	}

	public void serviceStop() {
	}

}

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

import net.locosoft.CompuCanvas.controller.util.C3Util;

public abstract class AbstractC3Service implements IC3ServiceInternal {

	private String _id;
	private int _priority;
	private ICoreService _coreService;

	// IC3Service

	public final String getServiceId() {
		return _id;
	}

	public final ICoreService getCoreService() {
		return _coreService;
	}

	// IC3ServiceInternal

	public final int serviceGetPriority() {
		return _priority;
	}

	public final void serviceRegister(String id, int priority) {
		_id = id;
		_priority = priority;
	}

	public final void serviceInit(ICoreService coreService) {
		_coreService = coreService;
	}

	public void serviceStart() {
	}

	public void serviceStop() {
	}

	public final <C3S extends IC3Service> IC3Service serviceLookup(Class<C3S> serviceInterface) {
		return getCoreService().getService(serviceInterface);
	}

	public final String serviceGetConfig(String keySuffix, String defaultValue) {
		String key = "c3.service." + getServiceId() + "." + keySuffix;
		return getCoreService().getModelConfig(key);
	}

	public final int serviceGetConfigInt(String keySuffix, int defaultValue) {
		String key = "c3.service." + getServiceId() + "." + keySuffix;
		String value = getCoreService().getModelConfig(key);
		return C3Util.parseInt(value, defaultValue);
	}

}

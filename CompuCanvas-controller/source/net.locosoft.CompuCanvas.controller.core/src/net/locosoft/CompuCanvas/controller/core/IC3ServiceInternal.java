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

public interface IC3ServiceInternal extends IC3Service {

	public int serviceGetPriority();

	public void serviceRegister(String id, int priority);

	public void serviceInit(ICoreService coreService);

	public void serviceStart();

	public void serviceStop();

	public <C3S extends IC3Service> IC3Service serviceLookup(Class<C3S> serviceInterface);

	public String serviceGetConfig(String keySuffix, String defaultValue);

	public int serviceGetConfigInt(String keySuffix, int defaultValue);

}

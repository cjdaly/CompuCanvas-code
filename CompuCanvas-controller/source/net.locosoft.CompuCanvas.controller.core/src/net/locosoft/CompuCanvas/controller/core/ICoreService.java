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

public interface ICoreService extends IC3Service {

	public IC3Service getService(String id);

	public <C3S extends IC3Service> IC3Service getService(Class<C3S> serviceInterface);

	public String getModelConfig(String key);

}

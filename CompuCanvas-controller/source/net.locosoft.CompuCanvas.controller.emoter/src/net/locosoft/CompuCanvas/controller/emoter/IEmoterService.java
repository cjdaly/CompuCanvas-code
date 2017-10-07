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

package net.locosoft.CompuCanvas.controller.emoter;

import net.locosoft.CompuCanvas.controller.core.IC3Service;

public interface IEmoterService extends IC3Service {

	public void registerEmoter(AbstractEmoter emoter);

	public String[] getSpectrumIds();

	public ISpectrum getSpectrum(String id);

}

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

package net.locosoft.CompuCanvas.controller.audio;

import net.locosoft.CompuCanvas.controller.core.IC3Service;

public interface IAudioService extends IC3Service {

	void speak(String message);

	String[] getMP3Ids();

	void playMP3(String id);

}

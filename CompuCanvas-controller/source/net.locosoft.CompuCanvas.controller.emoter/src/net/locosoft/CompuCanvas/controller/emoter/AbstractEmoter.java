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

public abstract class AbstractEmoter {

	private String _id;

	public AbstractEmoter(String id) {
		_id = id;
	}

	public String getId() {
		return _id;
	}

	public boolean initPre() {
		return true;
	}

	public boolean initPost() {
		return true;
	}

}

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

package net.locosoft.CompuCanvas.controller.core.tsd;

import java.util.HashMap;

import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.core.ICoreService;

public class TSDGroup {

	private String _id;
	private String _hashKey;
	private IC3Service _service;
	private ICoreService _coreService;

	private HashMap<String, TSDBuffer> _buffers = new HashMap<String, TSDBuffer>();

	public TSDGroup(String id, IC3Service service) {
		_id = id;
		_service = service;
		_hashKey = _service.getServiceId() + ";" + _id;
		_coreService = _service.getCoreService();
	}

	public String getHashKey() {
		return _hashKey;
	}

	public String getId() {
		return _id;
	}

	public IC3Service getService() {
		return _service;
	}

	public TSDBuffer createTSDBuffer(String id, String units, TSDType type) {
		return createTSDBuffer(id, units, type, 64);
	}

	public TSDBuffer createTSDBuffer(String id, String units, TSDType type, int size) {
		TSDBuffer buffer = new TSDBuffer(id, units, type, size, this);
		_buffers.put(id, buffer);
		return buffer;
	}

	void propagate(TSDValue value) {
		_coreService.propagateTSDValue(value);
	}
}

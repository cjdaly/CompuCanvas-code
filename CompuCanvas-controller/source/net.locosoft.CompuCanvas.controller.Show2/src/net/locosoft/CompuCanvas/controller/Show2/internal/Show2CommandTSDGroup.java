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

import net.locosoft.CompuCanvas.controller.core.tsd.TSDBuffer;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDType;

public class Show2CommandTSDGroup {
	private Show2Service _service;
	private TSDGroup _group;
	private TSDBuffer _inputs;
	private TSDBuffer _queueSize;
	private TSDBuffer _outputs;

	public Show2CommandTSDGroup(Show2Service service) {
		_service = service;
		_group = _service.serviceCreateTSDGroup("commands");
		_inputs = _group.createTSDBuffer("input", "Show2 commands", TSDType.StringArray);
		_queueSize = _group.createTSDBuffer("queueSize", "size", TSDType.Long);
		_outputs = _group.createTSDBuffer("output", "Show2 output", TSDType.String);
	}

	public TSDGroup getGroup() {
		return _group;
	}

	public TSDBuffer getInputs() {
		return _inputs;
	}

	public TSDBuffer getQueueSize() {
		return _queueSize;
	}

	public TSDBuffer getOutputs() {
		return _outputs;
	}
}

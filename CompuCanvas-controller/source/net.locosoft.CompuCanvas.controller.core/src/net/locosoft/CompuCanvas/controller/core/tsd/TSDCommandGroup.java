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

import net.locosoft.CompuCanvas.controller.core.IC3ServiceInternal;

public class TSDCommandGroup {

	private IC3ServiceInternal _service;
	private TSDGroup _group;

	private TSDBuffer _inputs;
	private TSDBuffer _outputs;
	private TSDBuffer _errors;
	private TSDBuffer _results;

	public TSDCommandGroup(IC3ServiceInternal service, String id) {
		_service = service;
		_group = _service.serviceCreateTSDGroup(id);
		_inputs = _group.createTSDBuffer("input", "text", TSDType.String);
		_outputs = _group.createTSDBuffer("output", "text", TSDType.String);
		_errors = _group.createTSDBuffer("error", "text", TSDType.String);
		_results = _group.createTSDBuffer("result", "result", TSDType.Long);
	}

	public TSDGroup getGroup() {
		return _group;
	}

	public TSDBuffer getInputs() {
		return _inputs;
	}

	public TSDBuffer getOutputs() {
		return _outputs;
	}

	public TSDBuffer getErrors() {
		return _errors;
	}

	public TSDBuffer getResults() {
		return _results;
	}

}

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

package net.locosoft.CompuCanvas.controller.vitals.internal;

import java.util.Date;

import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;

public class TimeVitalSigns {

	private VitalsService _service;
	private TSDGroup _group;

	private TimeVitalSign _seconds;
	private TimeVitalSign _minutes;
	private TimeVitalSign _hours;

	public TimeVitalSigns(VitalsService service) {
		_service = service;
		_group = _service.serviceCreateTSDGroup("time");

		_seconds = new TimeVitalSign.Seconds(_group);
		_minutes = new TimeVitalSign.Minutes(_group);
		_hours = new TimeVitalSign.Hours(_group);
	}

	public void update(Date date) {
		_seconds.update(date);
		_minutes.update(date);
		_hours.update(date);
	}

}

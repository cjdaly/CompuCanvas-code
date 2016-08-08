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

public class DateVitalSigns {

	private VitalsService _service;
	private TSDGroup _group;

	private DateVitalSign _year;
	private DateVitalSign _month;
	private DateVitalSign _monthName;
	private DateVitalSign _dayOfMonth;
	private DateVitalSign _dayOfWeek;
	private DateVitalSign _dayOfWeekName;

	public DateVitalSigns(VitalsService service) {
		_service = service;
		_group = _service.serviceCreateTSDGroup("date");

		_year = new DateVitalSign.Year(_group);
		_month = new DateVitalSign.Month(_group);
		_monthName = new DateVitalSign.MonthName(_group);
		_dayOfMonth = new DateVitalSign.DayOfMonth(_group);
		_dayOfWeek = new DateVitalSign.DayOfWeek(_group);
		_dayOfWeekName = new DateVitalSign.DayOfWeekName(_group);
	}

	public void update(Date date) {
		_year.update(date);
		_month.update(date);
		_monthName.update(date);
		_dayOfMonth.update(date);
		_dayOfWeek.update(date);
		_dayOfWeekName.update(date);
	}
}

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
import net.locosoft.CompuCanvas.controller.core.tsd.TSDType;

public abstract class DateVitalSign extends VitalSign {

	public DateVitalSign(String id, String units, TSDType type, TSDGroup group) {
		super(id, units, type, group);
	}

	public static class Year extends DateVitalSign {
		public Year(TSDGroup group) {
			super("year", "date", TSDType.Long, group);
		}

		@SuppressWarnings("deprecation")
		public void update(Date date) {
			_buffer.update(date.getTime(), date.getYear() + 1900);
		}
	}

	public static class Month extends DateVitalSign {
		public Month(TSDGroup group) {
			super("month", "date", TSDType.Long, group);
		}

		@SuppressWarnings("deprecation")
		public void update(Date date) {
			_buffer.update(date.getTime(), date.getMonth() + 1);
		}
	}

	public static class MonthName extends DateVitalSign {
		public MonthName(TSDGroup group) {
			super("monthName", "date", TSDType.String, group);
		}

		@SuppressWarnings("deprecation")
		public void update(Date date) {

			_buffer.update(date.getTime(), _monthNames[date.getMonth()]);
		}

		private static String[] _monthNames = { //
				"January", //
				"February", //
				"March", //
				"April", //
				"May", //
				"June", //
				"July", //
				"August", //
				"September", //
				"October", //
				"November", //
				"December" //
		};
	}

	public static class DayOfMonth extends DateVitalSign {
		public DayOfMonth(TSDGroup group) {
			super("dayOfMonth", "date", TSDType.Long, group);
		}

		@SuppressWarnings("deprecation")
		public void update(Date date) {
			_buffer.update(date.getTime(), date.getDate());
		}
	}

	public static class DayOfWeek extends DateVitalSign {
		public DayOfWeek(TSDGroup group) {
			super("dayOfWeek", "date", TSDType.Long, group);
		}

		@SuppressWarnings("deprecation")
		public void update(Date date) {
			_buffer.update(date.getTime(), date.getDay());
		}
	}

	public static class DayOfWeekName extends DateVitalSign {
		public DayOfWeekName(TSDGroup group) {
			super("dayOfWeekName", "date", TSDType.String, group);
		}

		@SuppressWarnings("deprecation")
		public void update(Date date) {
			_buffer.update(date.getTime(), _dayNames[date.getDay()]);
		}

		private static String[] _dayNames = { //
				"Sunday", //
				"Monday", //
				"Tuesday", //
				"Wednesday", //
				"Thursday", //
				"Friday", //
				"Saturday" //
		};
	}
}

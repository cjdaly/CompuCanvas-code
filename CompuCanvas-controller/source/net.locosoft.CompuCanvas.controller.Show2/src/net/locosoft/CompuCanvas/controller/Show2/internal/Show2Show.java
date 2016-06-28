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

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

import net.locosoft.Show2Eboogaloo.Show2Commands;

public abstract class Show2Show {

	public static Show2Show nextShow() {
		int i = ThreadLocalRandom.current().nextInt(4);
		switch (i) {
		case 0:
			return new Date();
		default:
			return new Time();
		}
	}

	public Show2Commands emitCommands(int rotation) {
		Show2Commands commands = new Show2Commands();
		emitCommands(commands, rotation);
		return commands;
	}

	abstract void emitCommands(Show2Commands commands, int rotation);

	public static class Time extends Show2Show {
		private static final SimpleDateFormat _HourFormat = new SimpleDateFormat("hh");
		private static final SimpleDateFormat _MinuteFormat = new SimpleDateFormat("mm");
		private static final SimpleDateFormat _AmPmFormat = new SimpleDateFormat("a");

		void emitCommands(Show2Commands commands, int rotation) {
			commands.addCommand("cls");

			java.util.Date now = new java.util.Date();
			String hour = _HourFormat.format(now);
			String minute = _MinuteFormat.format(now);
			String amPm = _AmPmFormat.format(now);
			amPm = amPm.toLowerCase();

			if (isVertical(rotation)) {
				commands.addCommand("siz14");
				commands.addCommand("bg4");

				commands.addCommand("fg7");
				commands.addCommand("/0/" + hour);

				commands.addCommand("fg5");
				commands.addCommand("/1/" + amPm);

				commands.addCommand("fg7");
				commands.addCommand("/2/" + minute);
			} else {
				commands.addCommand("siz16");
				commands.addCommand("bg4");

				commands.addCommand("fg7");
				commands.addCommand("/0/" + hour.charAt(0));
				commands.addCommand("/1/" + hour.charAt(1));

				commands.addCommand("fg6");
				commands.addCommand("/0,1/" + amPm.charAt(0));
				commands.addCommand("/1,1/" + amPm.charAt(1));

				commands.addCommand("fg7");
				commands.addCommand("/0,2/" + minute.charAt(0));
				commands.addCommand("/1,2/" + minute.charAt(1));
			}

		}
	}

	public static class Date extends Show2Show {
		private static final SimpleDateFormat _DayOfWeekFormat = new SimpleDateFormat("EEE");
		private static final SimpleDateFormat _MonthFormat = new SimpleDateFormat("MMM");
		private static final SimpleDateFormat _DayFormat = new SimpleDateFormat("dd");

		void emitCommands(Show2Commands commands, int rotation) {
			commands.addCommand("cls");

			java.util.Date now = new java.util.Date();
			String dayOfWeek = _DayOfWeekFormat.format(now);
			String month = _MonthFormat.format(now);
			String day = _DayFormat.format(now);

			if (isVertical(rotation)) {
				commands.addCommand("siz13");
				commands.addCommand("bg0");

				commands.addCommand("fg7");
				commands.addCommand("/0/" + dayOfWeek);

				commands.addCommand("fg6");
				commands.addCommand("/1/" + month);
				commands.addCommand("/2/ " + day);

			} else {
				commands.addCommand("siz10");
				commands.addCommand("bg0");

				commands.addCommand("fg3");
				commands.addCommand("/0/ " + dayOfWeek);

				commands.addCommand("fg6");
				commands.addCommand("/1/ " + month);
				commands.addCommand("/2/  " + day);
			}
		}
	}

	boolean isVertical(int rotation) {
		return (rotation == 0) | (rotation == 2);
	}
}

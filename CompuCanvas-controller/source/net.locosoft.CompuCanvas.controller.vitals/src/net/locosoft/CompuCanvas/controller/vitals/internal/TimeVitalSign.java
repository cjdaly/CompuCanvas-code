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

public abstract class TimeVitalSign extends VitalSign {

	public TimeVitalSign(String id, String units, TSDType type, TSDGroup group) {
		super(id, units, type, group);
	}

	public static class Seconds extends TimeVitalSign {
		public Seconds(TSDGroup group) {
			super("seconds", "time", TSDType.Long, group);
		}

		@SuppressWarnings("deprecation")
		public void update(Date date) {
			_buffer.update(date.getTime(), date.getSeconds());
		}
	}

	public static class Minutes extends TimeVitalSign {
		public Minutes(TSDGroup group) {
			super("minutes", "time", TSDType.Long, group);
		}

		@SuppressWarnings("deprecation")
		public void update(Date date) {
			_buffer.update(date.getTime(), date.getMinutes());
		}
	}

	public static class Hours extends TimeVitalSign {
		public Hours(TSDGroup group) {
			super("hours", "time", TSDType.Long, group);
		}

		@SuppressWarnings("deprecation")
		public void update(Date date) {
			_buffer.update(date.getTime(), date.getHours());
		}
	}

}

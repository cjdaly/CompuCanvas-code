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

import net.locosoft.CompuCanvas.controller.util.MonitorThread;
import net.locosoft.Show2Eboogaloo.Show2Commands;
import net.locosoft.Show2Eboogaloo.Show2Session;

public class Show2Feeder extends MonitorThread {

	private Show2Session _session;

	public Show2Feeder(Show2Session session) {
		_session = session;
	}

	protected long getPreSleepMillis() {
		return 8000;
	}

	protected long getPostSleepMillis() {
		return 1000;
	}

	public void beginCycle() throws Exception {
		Show2Commands commands = new Show2Commands();
		commands.addCommand("-WB");
		commands.addCommand("blt255");
		commands.addCommand("siz12");
		_session.enqueueCommands(commands);
	}

	private int _fgColor = 0;

	private void nextFg(Show2Commands commands) {
		_fgColor++;
		if (_fgColor > 7) {
			_fgColor = 1;
		}
		commands.addCommand("fg" + _fgColor);
	}

	public boolean cycle() throws Exception {

		Show2Commands commands = new Show2Commands();
		commands.addCommand("cls");
		nextFg(commands);
		commands.addCommand("/0/ABC");
		nextFg(commands);
		commands.addCommand("/1/123");
		_session.enqueueCommands(commands);

		return true;
	}

}

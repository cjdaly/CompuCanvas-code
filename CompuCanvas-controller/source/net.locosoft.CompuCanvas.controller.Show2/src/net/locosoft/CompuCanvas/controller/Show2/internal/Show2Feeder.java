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

	private Show2Service _service;
	private Show2Session _session;
	private Show2CommandTSDGroup _commandTSDs;

	private int _defaultRotation;
	private int _defaultBacklight;
	private String _showCompuCanvas;
	private String _showCompuCanvasAlt;
	private String _showCCid;

	public Show2Feeder(Show2Service service, Show2Session session, Show2CommandTSDGroup commandTSDs) {
		_service = service;
		_session = session;
		_commandTSDs = commandTSDs;

		int defaultRotation = _service.serviceGetConfigInt("defaultRotation", 0);
		if ((defaultRotation < 0) || (defaultRotation > 3))
			defaultRotation = 0;
		_defaultRotation = defaultRotation;

		int defaultBacklight = _service.serviceGetConfigInt("defaultBacklight", 200);
		if (defaultBacklight < 0)
			defaultBacklight = 0;
		if (defaultBacklight > 255)
			defaultBacklight = 255;
		_defaultBacklight = defaultBacklight;

		_showCompuCanvas = _service.serviceGetConfig("show.CompuCanvas", null);
		_showCompuCanvasAlt = _service.serviceGetConfig("show.CompuCanvas.alt", null);
		_showCCid = _service.serviceGetConfig("show.CCid", null);
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
		commands.addCommand("blt" + _defaultBacklight);
		enqueueCommands(commands);
	}

	public boolean cycle() throws Exception {
		Show2Show show = Show2Show.nextShow(_showCompuCanvas, _showCompuCanvasAlt, _showCCid);
		Show2Commands commands = show.emitCommands(_defaultRotation);
		enqueueCommands(commands);
		return true;
	}

	public void endCycle() throws Exception {
		_session.clearCommands();
		Show2Commands commands = new Show2Commands();
		commands.addCommand("cls");
		enqueueCommands(commands);

		// wait for cls
		Thread.sleep(2000);
	}

	private void enqueueCommands(Show2Commands commands) {
		long timeMillis = System.currentTimeMillis();
		_session.enqueueCommands(commands);
		int queueSize = _session.getCommandQueueSize();
		_commandTSDs.getInputs().update(timeMillis, commands.getCommands());
		_commandTSDs.getQueueSize().update(timeMillis, queueSize);
	}

}

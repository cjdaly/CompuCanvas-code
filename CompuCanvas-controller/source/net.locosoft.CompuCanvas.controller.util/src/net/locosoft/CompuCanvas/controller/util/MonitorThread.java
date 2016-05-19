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

package net.locosoft.CompuCanvas.controller.util;

public abstract class MonitorThread implements Runnable {

	private Thread _thread = new Thread(this);
	private boolean _stopping = false;
	private boolean _stopped = true;

	protected long getPreSleepTime() {
		return -1;
	}

	protected long getPostSleepTime() {
		return 100;
	}

	public void start() {
		_thread.start();
	}

	public void stop() {
		_stopping = true;
	}

	public boolean isRunning() {
		return !(_stopped || _stopping);
	}

	public void run() {
		try {
			while (!_stopping) {
				long preSleepTime = getPreSleepTime();
				if (preSleepTime >= 0) {
					Thread.sleep(preSleepTime);
				}

				_stopping = !cycle();

				if (!_stopping) {
					long postSleepTime = getPostSleepTime();
					if (postSleepTime >= 0) {
						Thread.sleep(postSleepTime);
					}
				}
			}
			endCycle();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		_stopped = true;
	}

	/**
	 * @return <code>true</code> to continue cycling, <code>false</code> to stop
	 */
	public abstract boolean cycle() throws Exception;

	public void endCycle() throws Exception {
	}

}

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

	protected long getPreSleepMillis() {
		return -1;
	}

	protected long getPostSleepMillis() {
		return 100;
	}

	protected void init() {
	}

	public void start() {
		init();
		_thread.start();
	}

	public void stop() {
		stop(false);
	}

	public void stop(boolean waitForStop) {
		_stopping = true;

		if (waitForStop) {
			while (!isStopped()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					//
				}
			}
		}
	}

	public boolean isStopped() {
		return _stopped;
	}

	public void run() {
		try {
			_stopped = false;
			beginCycle();
			while (!_stopping) {
				long preSleepMillis = getPreSleepMillis();
				if (preSleepMillis >= 0) {
					Thread.sleep(preSleepMillis);
				}

				if (!_stopping) {
					_stopping = !cycle();
				}

				if (!_stopping) {
					long postSleepMillis = getPostSleepMillis();
					if (postSleepMillis >= 0) {
						Thread.sleep(postSleepMillis);
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

	public void beginCycle() throws Exception {
	}

	public void endCycle() throws Exception {
	}

}

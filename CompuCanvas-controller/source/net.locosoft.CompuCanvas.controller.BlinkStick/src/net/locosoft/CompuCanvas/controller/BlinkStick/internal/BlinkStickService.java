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

package net.locosoft.CompuCanvas.controller.BlinkStick.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import net.locosoft.CompuCanvas.controller.BlinkStick.IBlinkStick;
import net.locosoft.CompuCanvas.controller.BlinkStick.IBlinkStickService;
import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.util.ExecUtil;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class BlinkStickService extends AbstractC3Service implements IBlinkStickService {

	// IBlinkStickService

	public int getBlinkStickCount() {
		return _blinkStickArray.size();
	}

	public IBlinkStick getBlinkStick(int index) {
		return _blinkStickArray.get(index);
	}

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return IBlinkStickService.class;
	}

	public void serviceStart() {
		detectBlinkSticks();
		_blinkStickFeeder.start();
		_randomEnqueuer.start();
	}

	public void serviceStop() {
		_randomEnqueuer.stop(true);

		clearCommands();

		for (int i = 0; i < getBlinkStickCount(); i++) {
			IBlinkStick blinkStick = getBlinkStick(i);
			blinkStick.setLED(-1, "off");
		}

		String nextCommand = peekCommand();
		while (nextCommand != null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				//
			}
			nextCommand = peekCommand();
		}

		_blinkStickFeeder.stop();
	}

	private ArrayList<BlinkStick> _blinkStickArray = new ArrayList<BlinkStick>();

	private void detectBlinkSticks() {
		StringBuilder blinkStickOut = new StringBuilder();
		StringBuilder blinkStickErr = new StringBuilder();
		String blinkStickInfoCommand = "blinkstick -i";
		int result = ExecUtil.execCommand(blinkStickInfoCommand, blinkStickOut, blinkStickErr);
		if (result != 0) {
			System.out.println("Error (" + result + ") from: " + blinkStickInfoCommand);
			System.out.println(" stdout: " + blinkStickOut);
			System.out.println(" stderr: " + blinkStickErr);
		} else {
			String infoTmp = blinkStickOut.toString();
			int index = infoTmp.lastIndexOf("Found device:");
			while (index >= 0) {
				String infoText = infoTmp.substring(index);
				BlinkStick blinkStick = new BlinkStick(this, infoText);
				_blinkStickArray.add(blinkStick);
				System.out.println("BlinkStick detected: " + //
						blinkStick.getSerial() + //
						" (kind: " + blinkStick.getKind() + //
						", mode:" + blinkStick.getMode() + ")");
				infoTmp = infoTmp.substring(0, index);
				index = infoTmp.lastIndexOf("Found device:");
			}
		}
	}

	private LinkedList<String> _commandQueue = new LinkedList<String>();

	synchronized void enqueueCommand(String command) {
		_commandQueue.add(command);
	}

	synchronized String dequeueCommand() {
		if (_commandQueue.isEmpty())
			return null;
		else
			return _commandQueue.removeFirst();
	}

	synchronized String peekCommand() {
		if (_commandQueue.isEmpty())
			return null;
		else
			return _commandQueue.getFirst();
	}

	synchronized int countCommands() {
		return _commandQueue.size();
	}

	synchronized void clearCommands() {
		_commandQueue.clear();
	}

	private MonitorThread _blinkStickFeeder = new MonitorThread() {

		public boolean cycle() throws Exception {
			String blinkStickCommand = dequeueCommand();
			if (blinkStickCommand != null) {
				StringBuilder blinkStickOut = new StringBuilder();
				StringBuilder blinkStickErr = new StringBuilder();
				int result = ExecUtil.execCommand(blinkStickCommand, blinkStickOut, blinkStickErr);
				if (result != 0) {
					System.out.println("Error (" + result + ") from: " + blinkStickCommand);
					System.out.println(" stdout: " + blinkStickOut);
					System.out.println(" stderr: " + blinkStickErr);
				}
			}

			if (countCommands() > 32) {
				System.out.println("BlinkStick service dumping command queue!");
				clearCommands();
			}

			return true;
		}
	};

	private MonitorThread _randomEnqueuer = new MonitorThread() {
		protected long getPreSleepTime() {
			return 400;
		}

		public boolean cycle() throws Exception {
			if (random(100) >= _randomBlinkThreshold) {
				int randomBlinkStickIndex = random(getBlinkStickCount());
				IBlinkStick blinkStick = getBlinkStick(randomBlinkStickIndex);

				int limit = random(blinkStick.getLimitMin(), blinkStick.getLimitMax() + 1);
				String color = _BasicColors[random(3, _BasicColors.length)];

				switch (blinkStick.getMode()) {
				case Random1:
					int index = random(blinkStick.getLEDCount());
					blinkStick.setLED(index, color, limit);
					break;
				case Random2:
					blinkStick.setLED(-1, color, limit);
					break;
				case Random3:
					int firstIndex = random(2) * (blinkStick.getLEDCount() / 2);
					int lastIndex = firstIndex + (blinkStick.getLEDCount() / 2);
					for (int i = firstIndex; i < lastIndex; i++) {
						blinkStick.setLED(i, color, limit);
					}
					break;
				default:
				}
			}

			return true;
		}

		private int random(int range) {
			return ThreadLocalRandom.current().nextInt(range);
		}

		private int random(int minInclusive, int maxExclusive) {
			return ThreadLocalRandom.current().nextInt(minInclusive, maxExclusive);
		}

		private int _randomBlinkThreshold = 70; // 0 - 100

	};

}

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

import java.util.concurrent.ThreadLocalRandom;

import net.locosoft.CompuCanvas.controller.BlinkStick.IBlinkStick;
import net.locosoft.CompuCanvas.controller.BlinkStick.IBlinkStickService;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class RandomBlinker extends MonitorThread {

	private BlinkStickService _service;

	private int _randomSkipPercent;
	private int _randomDelayMillis;

	public RandomBlinker(BlinkStickService service) {
		_service = service;
	}

	protected void init() {
		_randomSkipPercent = _service.serviceGetConfigInt("random.skipPercent", 70);
		_randomDelayMillis = _service.serviceGetConfigInt("random.delayMillis", 500);
	}

	protected long getPreSleepMillis() {
		return _randomDelayMillis;
	}

	public boolean cycle() throws Exception {
		if (random(100) >= _randomSkipPercent) {
			int randomBlinkStickIndex = random(_service.getBlinkStickCount());
			IBlinkStick blinkStick = _service.getBlinkStick(randomBlinkStickIndex);

			int limit = random(blinkStick.getLimitMin(), blinkStick.getLimitMax() + 1);
			String color = IBlinkStickService._BasicColors[random(3, IBlinkStickService._BasicColors.length)];

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
}

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

import java.util.ArrayList;
import java.util.Date;

import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class VitalsReader extends MonitorThread {

	private VitalsService _service;
	private TSDGroup _vitals;
	private TimeVitalSigns _timeVitals;
	private DateVitalSigns _dateVitals;

	private ArrayList<VitalSign> _vitalSigns = new ArrayList<VitalSign>();

	public VitalsReader(VitalsService service) {
		_service = service;

		_vitals = _service.serviceCreateTSDGroup("vitals");

		_vitalSigns.add(new VitalSign.C3ProcessVmPeak(_vitals));
		_vitalSigns.add(new VitalSign.JVMTotalMemory(_vitals));
		_vitalSigns.add(new VitalSign.SystemLoad(_vitals));
		_vitalSigns.add(new VitalSign.SystemMemory(_vitals));
		_vitalSigns.add(new VitalSign.SystemStorage(_vitals));
		_vitalSigns.add(new VitalSign.CPUTemp(_vitals));
		_vitalSigns.add(new VitalSign.GPUTemp(_vitals));

		_timeVitals = new TimeVitalSigns(_service);
		_dateVitals = new DateVitalSigns(_service);
	}

	protected long getPostSleepMillis() {
		return 5000;
	}

	private int _vitalSignIndex = 0;

	public boolean cycle() throws Exception {
		Date date = new Date();

		_timeVitals.update(date);

		VitalSign vitalSign = _vitalSigns.get(_vitalSignIndex);
		vitalSign.update(date);

		_vitalSignIndex++;
		if (_vitalSignIndex == _vitalSigns.size()) {
			_vitalSignIndex = 0;
			_dateVitals.update(date);
		}

		return true;
	}

}

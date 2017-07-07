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

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.locosoft.CompuCanvas.controller.core.tsd.TSDBuffer;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDType;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;
import net.locosoft.Show2Eboogaloo.Show2Session;

public class Show2Listener extends MonitorThread {
	private Show2Service _service;
	private Show2Session _session;
	private Show2CommandTSDGroup _commandTSDs;

	private TSDGroup _bmp180;
	private TSDBuffer _bmp180Temp;
	private TSDBuffer _bmp180Baro;

	private TSDGroup _si1132;
	private TSDBuffer _si1132Vis;
	private TSDBuffer _si1132IR;
	private TSDBuffer _si1132UV;

	private TSDGroup _si7020;
	private TSDBuffer _si7020Temp;
	private TSDBuffer _si7020Humi;

	public Show2Listener(Show2Service service, Show2Session session, Show2CommandTSDGroup commandTSDs) {
		_service = service;
		_session = session;
		_commandTSDs = commandTSDs;

		_bmp180 = _service.serviceCreateTSDGroup("bmp180");
		_bmp180Temp = _bmp180.createTSDBuffer("temp", "Celsius", TSDType.Double);
		_bmp180Baro = _bmp180.createTSDBuffer("baro", "hPa", TSDType.Double);

		_si1132 = _service.serviceCreateTSDGroup("si1132");
		_si1132Vis = _si1132.createTSDBuffer("vis", "lux", TSDType.Long);
		_si1132IR = _si1132.createTSDBuffer("IR", "lux", TSDType.Long);
		_si1132UV = _si1132.createTSDBuffer("UV", "lux", TSDType.Double);

		_si7020 = _service.serviceCreateTSDGroup("si7020");
		_si7020Temp = _si7020.createTSDBuffer("temp", "Celsius", TSDType.Double);
		_si7020Humi = _si7020.createTSDBuffer("humi", "%", TSDType.Double);
	}

	private static final Pattern _SensorDataPattern = Pattern.compile("!! (\\w+):(.*) !!");

	public boolean cycle() throws Exception {

		String line = _session.pullOutputLine();
		if ((line != null) && (line.startsWith("!! "))) {
			Matcher matcher = _SensorDataPattern.matcher(line);
			if (matcher.matches()) {
				_commandTSDs.getOutputs().update(line);
				
				long timeMillis = System.currentTimeMillis();

				HashMap<String, String> vitalsData = new HashMap<String, String>();
				String vitalsList = matcher.group(2);
				String[] vitalsArray = vitalsList.split(",");
				for (String vitalKeyValue : vitalsArray) {
					int eqPos = vitalKeyValue.indexOf('=');
					if (eqPos != -1) {
						String key = vitalKeyValue.substring(0, eqPos).trim();
						String value = vitalKeyValue.substring(eqPos + 1, vitalKeyValue.length()).trim();
						vitalsData.put(key, value);
					}
				}

				String sensorId = matcher.group(1);
				switch (sensorId) {
				case "BMP180":
					recordSensorData(_bmp180Temp, timeMillis, vitalsData.get("temp"));
					recordSensorData(_bmp180Baro, timeMillis, vitalsData.get("pres"));
					break;
				case "Si1132":
					recordSensorData(_si1132Vis, timeMillis, vitalsData.get("vis"));
					recordSensorData(_si1132IR, timeMillis, vitalsData.get("IR"));
					recordSensorData(_si1132UV, timeMillis, vitalsData.get("UV"));
					break;
				case "Si7020":
					recordSensorData(_si7020Temp, timeMillis, vitalsData.get("temp"));
					recordSensorData(_si7020Humi, timeMillis, vitalsData.get("humi"));
					break;
				}
			}
		}

		return true;
	}

	private void recordSensorData(TSDBuffer tsdBuffer, long timeMillis, String valueText) {
		if (valueText == null)
			return;
		tsdBuffer.update(timeMillis, valueText);
	}
}

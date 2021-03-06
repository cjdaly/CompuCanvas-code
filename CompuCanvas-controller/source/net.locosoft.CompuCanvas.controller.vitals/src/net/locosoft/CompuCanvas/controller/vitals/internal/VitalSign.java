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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.locosoft.CompuCanvas.controller.core.tsd.TSDBuffer;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDType;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.ExecUtil;
import net.locosoft.CompuCanvas.controller.util.FileUtil;

public abstract class VitalSign {

	private String _id;
	private String _units;
	private TSDType _type;

	private TSDGroup _group;
	protected TSDBuffer _buffer;

	public VitalSign(String id, String units, TSDType type, TSDGroup group) {
		_id = id;
		_units = units;
		_type = type;
		_group = group;
		_buffer = _group.createTSDBuffer(id, units, type);
	}

	public String getId() {
		return _id;
	}

	public String getUnits() {
		return _units;
	}

	public TSDType getType() {
		return _type;
	}

	public abstract void update(Date date);

	public static class C3ProcessVmPeak extends VitalSign {
		private static final int _C3VmPeakMax = 500000;

		public C3ProcessVmPeak(TSDGroup group) {
			super("process.c3.vmPeak", "kB", TSDType.Long, group);
		}

		public void update(Date date) {
			int c3Pid = C3Util.getC3PID();
			if (c3Pid == -1)
				return;

			int vmPeak = C3Util.getProcessVmPeak(c3Pid);
			_buffer.update(date.getTime(), vmPeak);

			if (vmPeak > _C3VmPeakMax) {
				C3Util.log("!!! C3 VmPeak: " + vmPeak + " ; stopping C3 now!");
				C3Util.stopC3();
			}
		}
	}

	public static class JVMTotalMemory extends VitalSign {
		public JVMTotalMemory(TSDGroup group) {
			super("jvm.totalMemory", "bytes", TSDType.Long, group);
		}

		public void update(Date date) {
			long totalMemory = Runtime.getRuntime().totalMemory();
			_buffer.update(date.getTime(), totalMemory);
		}
	}

	public static class SystemLoad extends VitalSign {
		public SystemLoad(TSDGroup group) {
			super("system.loadAvg1Min", "load average", TSDType.Double, group);
		}

		public void update(Date date) {
			String procLoadAvg = FileUtil.readFileToString("/proc/loadavg");
			String[] loadAvgs = procLoadAvg.split("\\s+");
			if ((loadAvgs != null) && (loadAvgs.length > 0)) {
				String loadAvg1MinText = loadAvgs[0];
				_buffer.update(date.getTime(), loadAvg1MinText);
			}
		}
	}

	public static class SystemMemory extends VitalSign {
		public SystemMemory(TSDGroup group) {
			super("system.memFree", "kB", TSDType.Long, group);
		}

		private Pattern _memFreePattern = Pattern.compile("MemFree:\\s+(\\d+)\\s+kB");

		public void update(Date date) {
			String procMemInfo = FileUtil.readFileToString("/proc/meminfo");
			Matcher matcher = _memFreePattern.matcher(procMemInfo);
			if (matcher.find()) {
				String memFreeText = matcher.group(1);
				_buffer.update(date.getTime(), memFreeText);
			}
		}
	}

	public static class SystemStorage extends VitalSign {
		public SystemStorage(TSDGroup group) {
			super("system.diskUsePercent", "kB", TSDType.Long, group);
		}

		private Pattern _dfPattern = Pattern.compile("/dev/\\S+\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)%");

		public void update(Date date) {
			StringBuilder processOut = new StringBuilder();
			StringBuilder processErr = new StringBuilder();
			String dfCommand = "/bin/df -k " + C3Util.getC3DataDir();
			ExecUtil.execCommand(dfCommand, processOut, processErr);

			Matcher matcher = _dfPattern.matcher(processOut);
			if (matcher.find()) {
				String dfUsePercentText = matcher.group(4);
				_buffer.update(date.getTime(), dfUsePercentText);
			}
		}
	}

	public static class CPUTemp extends VitalSign {
		private float _CPUTemp_Warn;
		private float _CPUTemp_Max;

		public CPUTemp(TSDGroup group, VitalsService vitalsService) {
			super("system.temp.CPU", "Celsius", TSDType.Double, group);
			_CPUTemp_Warn = vitalsService.serviceGetConfigFloat("CPU.temp.warn", (float) 76.0);
			_CPUTemp_Max = vitalsService.serviceGetConfigFloat("CPU.temp.stop", (float) 79.0);
		}

		public void update(Date date) {
			StringBuilder processOut = new StringBuilder();
			StringBuilder processErr = new StringBuilder();
			String cpuTempCommand = "cat /sys/class/thermal/thermal_zone0/temp";
			ExecUtil.execCommand(cpuTempCommand, processOut, processErr);

			int cpuTemp1000 = C3Util.parseInt(processOut.toString().trim(), -1);
			double cpuTemp = (double) cpuTemp1000 / 1000.0;
			_buffer.update(date.getTime(), cpuTemp);
			if (cpuTemp >= _CPUTemp_Warn) {
				C3Util.log("!!! CPU temp: " + cpuTemp + "C ; C3 will stop at " + _CPUTemp_Max + "!");
			}
			if (cpuTemp >= _CPUTemp_Max) {
				C3Util.log("!!! CPU temp: " + cpuTemp + "C ; stopping C3 now!");
				C3Util.stopC3();
			}
		}
	}

	public static class GPUTemp extends VitalSign {
		private float _GPUTemp_Warn;
		private float _GPUTemp_Max;

		public GPUTemp(TSDGroup group, VitalsService vitalsService) {
			super("system.temp.GPU", "Celsius", TSDType.Double, group);
			_GPUTemp_Warn = vitalsService.serviceGetConfigFloat("GPU.temp.warn", (float) 76.0);
			_GPUTemp_Max = vitalsService.serviceGetConfigFloat("GPU.temp.stop", (float) 79.0);
		}

		private Pattern _gpuTempPattern = Pattern.compile("temp=([0-9.]+)'C");

		public void update(Date date) {
			StringBuilder processOut = new StringBuilder();
			StringBuilder processErr = new StringBuilder();
			String gpuTempCommand = "/opt/vc/bin/vcgencmd measure_temp";
			ExecUtil.execCommand(gpuTempCommand, processOut, processErr);

			Matcher matcher = _gpuTempPattern.matcher(processOut);
			if (matcher.find()) {
				String gpuTempText = matcher.group(1);
				double gpuTemp = C3Util.parseDouble(gpuTempText, -1);
				_buffer.update(date.getTime(), gpuTemp);
				if (gpuTemp >= _GPUTemp_Warn) {
					C3Util.log("!!! GPU temp: " + gpuTemp + "C ; C3 will stop at " + _GPUTemp_Max + "!");
				}
				if (gpuTemp >= _GPUTemp_Max) {
					C3Util.log("!!! GPU temp: " + gpuTemp + "C ; stopping C3 now!");
					C3Util.stopC3();
				}
			}
		}
	}
}

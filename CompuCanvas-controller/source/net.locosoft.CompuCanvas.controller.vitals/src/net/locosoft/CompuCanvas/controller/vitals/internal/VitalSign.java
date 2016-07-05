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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.ExecUtil;
import net.locosoft.CompuCanvas.controller.util.FileUtil;
import net.locosoft.CompuCanvas.controller.util.tsd.TSDBuffer;
import net.locosoft.CompuCanvas.controller.util.tsd.TSDType;
import net.locosoft.CompuCanvas.controller.util.tsd.TSDValue;

public abstract class VitalSign {

	private String _id;
	private String _units;
	private TSDType _type;
	protected TSDBuffer _buffer;

	public VitalSign(String id, String units, TSDType type) {
		_id = id;
		_units = units;
		_type = type;
		_buffer = new TSDBuffer(id, units, type);
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

	public void record() {
		TSDValue tsd = read();
		if (tsd != null) {
			_buffer.setLatest(tsd);
		}
	}

	protected abstract TSDValue read();

	public static class C3ProcessVmPeak extends VitalSign {
		public C3ProcessVmPeak() {
			super("process.c3.vmPeak", "kB", TSDType.Long);
		}

		private Pattern _vmPeakPattern = Pattern.compile("VmPeak:\\s+(\\d+)\\s+kB");

		public TSDValue read() {
			String procStatus = FileUtil.readFileToString("/proc/" + C3Util.getC3Pid() + "/status");
			Matcher matcher = _vmPeakPattern.matcher(procStatus);
			if (matcher.find()) {
				String vmPeakText = matcher.group(1);
				return new TSDValue(vmPeakText, getType());
			}
			return null;
		}
	}

	public static class JVMTotalMemory extends VitalSign {
		public JVMTotalMemory() {
			super("jvm.totalMemory", "bytes", TSDType.Long);
		}

		public TSDValue read() {
			long totalMemory = Runtime.getRuntime().totalMemory();
			return new TSDValue(totalMemory);
		}
	}

	public static class SystemLoad extends VitalSign {
		public SystemLoad() {
			super("system.loadAvg1Min", "load average", TSDType.Double);
		}

		public TSDValue read() {
			String procLoadAvg = FileUtil.readFileToString("/proc/loadavg");
			String[] loadAvgs = procLoadAvg.split("\\s+");
			if ((loadAvgs != null) && (loadAvgs.length > 0)) {
				String loadAvg1MinText = loadAvgs[0];
				return new TSDValue(loadAvg1MinText, getType());
			}
			return null;
		}
	}

	public static class SystemMemory extends VitalSign {
		public SystemMemory() {
			super("system.memFree", "kB", TSDType.Long);
		}

		private Pattern _memFreePattern = Pattern.compile("MemFree:\\s+(\\d+)\\s+kB");

		protected TSDValue read() {
			String procMemInfo = FileUtil.readFileToString("/proc/meminfo");
			Matcher matcher = _memFreePattern.matcher(procMemInfo);
			if (matcher.find()) {
				String memFreeText = matcher.group(1);
				return new TSDValue(memFreeText, getType());
			}
			return null;
		}
	}

	public static class SystemStorage extends VitalSign {
		public SystemStorage() {
			super("system.diskUsePercent", "kB", TSDType.Long);
		}

		private Pattern _dfPattern = Pattern.compile("/dev/\\S+\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)%");

		protected TSDValue read() {
			StringBuilder processOut = new StringBuilder();
			StringBuilder processErr = new StringBuilder();
			String dfCommand = "/bin/df -k " + C3Util.getC3DataDir();
			ExecUtil.execCommand(dfCommand, processOut, processErr);

			Matcher matcher = _dfPattern.matcher(processOut);
			if (matcher.find()) {
				String dfUsePercentText = matcher.group(4);
				return new TSDValue(dfUsePercentText, getType());
			}

			return null;
		}
	}
}

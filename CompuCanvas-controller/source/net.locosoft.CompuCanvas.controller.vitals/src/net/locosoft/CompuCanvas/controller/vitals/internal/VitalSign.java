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

	public abstract void update();

	public static class C3ProcessVmPeak extends VitalSign {
		public C3ProcessVmPeak(TSDGroup group) {
			super("process.c3.vmPeak", "kB", TSDType.Long, group);
		}

		private Pattern _vmPeakPattern = Pattern.compile("VmPeak:\\s+(\\d+)\\s+kB");

		public void update() {
			int c3Pid = C3Util.getC3Pid();
			if (c3Pid == -1)
				return;

			String procStatus = FileUtil.readFileToString("/proc/" + c3Pid + "/status");
			Matcher matcher = _vmPeakPattern.matcher(procStatus);
			if (matcher.find()) {
				String vmPeakText = matcher.group(1);
				_buffer.update(vmPeakText);
			}
		}
	}

	public static class JVMTotalMemory extends VitalSign {
		public JVMTotalMemory(TSDGroup group) {
			super("jvm.totalMemory", "bytes", TSDType.Long, group);
		}

		public void update() {
			long totalMemory = Runtime.getRuntime().totalMemory();
			_buffer.update(totalMemory);
		}
	}

	public static class SystemLoad extends VitalSign {
		public SystemLoad(TSDGroup group) {
			super("system.loadAvg1Min", "load average", TSDType.Double, group);
		}

		public void update() {
			String procLoadAvg = FileUtil.readFileToString("/proc/loadavg");
			String[] loadAvgs = procLoadAvg.split("\\s+");
			if ((loadAvgs != null) && (loadAvgs.length > 0)) {
				String loadAvg1MinText = loadAvgs[0];
				_buffer.update(loadAvg1MinText);
			}
		}
	}

	public static class SystemMemory extends VitalSign {
		public SystemMemory(TSDGroup group) {
			super("system.memFree", "kB", TSDType.Long, group);
		}

		private Pattern _memFreePattern = Pattern.compile("MemFree:\\s+(\\d+)\\s+kB");

		public void update() {
			String procMemInfo = FileUtil.readFileToString("/proc/meminfo");
			Matcher matcher = _memFreePattern.matcher(procMemInfo);
			if (matcher.find()) {
				String memFreeText = matcher.group(1);
				_buffer.update(memFreeText);
			}
		}
	}

	public static class SystemStorage extends VitalSign {
		public SystemStorage(TSDGroup group) {
			super("system.diskUsePercent", "kB", TSDType.Long, group);
		}

		private Pattern _dfPattern = Pattern.compile("/dev/\\S+\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)%");

		public void update() {
			StringBuilder processOut = new StringBuilder();
			StringBuilder processErr = new StringBuilder();
			String dfCommand = "/bin/df -k " + C3Util.getC3DataDir();
			ExecUtil.execCommand(dfCommand, processOut, processErr);

			Matcher matcher = _dfPattern.matcher(processOut);
			if (matcher.find()) {
				String dfUsePercentText = matcher.group(4);
				_buffer.update(dfUsePercentText);
			}
		}
	}
}

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

package net.locosoft.CompuCanvas.controller.core.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Properties;

import org.osgi.framework.BundleContext;

import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3ServiceInternal;
import net.locosoft.CompuCanvas.controller.core.ICoreService;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDGroup;
import net.locosoft.CompuCanvas.controller.core.tsd.TSDValue;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.FileUtil;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class CoreService extends AbstractC3Service implements ICoreService {

	private BundleContext _bundleContext;
	private HashMap<String, IC3ServiceInternal> _idToService;
	private HashMap<Class<? extends IC3Service>, IC3ServiceInternal> _ifaceToService;
	private ArrayList<IC3ServiceInternal> _orderedServices;

	private HashMap<String, TSDGroup> _tsdGroups;

	private Properties _logConfig;
	private Properties _modelConfig;

	private TSDCache _tsdCache = new TSDCache(this);

	// ICoreService

	public IC3Service getService(String id) {
		return _idToService.get(id);
	}

	@SuppressWarnings("unchecked")
	public <C3S> C3S getService(Class<C3S> serviceInterface) {
		return (C3S) _ifaceToService.get(serviceInterface);
	}

	public String getModelConfig(String key) {
		return (_modelConfig == null) ? null //
				: _modelConfig.getProperty(key);
	}

	public boolean isLoggingEnabled(String key) {
		if (_logConfig == null)
			return true;

		String value = _logConfig.getProperty(key, "false");
		return value.toLowerCase().equals("true");
	}

	public TSDGroup createTSDGroup(String id, IC3Service service) {
		TSDGroup tsdGroup = new TSDGroup(id, service);
		_tsdGroups.put(tsdGroup.getHashKey(), tsdGroup);
		return tsdGroup;
	}

	public void propagateTSDValue(TSDValue value) {
		_tsdCache.add(value);
	}

	public TSDValue[] getTSDValuesAfter(long timeMillis) {
		return _tsdCache.getAfter(timeMillis);
	}

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return ICoreService.class;
	}

	// for CoreActivator

	void activatorInit(BundleContext bundleContext, HashMap<String, IC3ServiceInternal> idToService,
			HashMap<Class<? extends IC3Service>, IC3ServiceInternal> ifaceToService,
			ArrayList<IC3ServiceInternal> orderedServices) {
		_bundleContext = bundleContext;
		_idToService = idToService;
		_ifaceToService = ifaceToService;
		_orderedServices = orderedServices;

		_tsdGroups = new HashMap<String, TSDGroup>();
	}

	void activatorStart() {
		C3Util.log("Starting CompuCanvas Controller (C3) ...");
		C3Util.log("C3 internal version: " + C3Util.getC3InternalVersion());
		C3Util.log("CompuCanvas model: " + C3Util.getCompuCanvasModelId());

		String logConfigFilePath = C3Util.getC3ConfigDir() + "/log.properties";
		File logConfigFile = new File(logConfigFilePath);
		if (logConfigFile.exists()) {
			_logConfig = FileUtil.loadPropertiesFile(logConfigFilePath);
			C3Util.log("Loaded log config: " + logConfigFilePath);
		}

		String modelConfigFilePath = C3Util.getC3ConfigDir() + "/model/" + C3Util.getCompuCanvasModelId()
				+ ".properties";
		File modelConfigFile = new File(modelConfigFilePath);
		if (modelConfigFile.exists()) {
			_modelConfig = FileUtil.loadPropertiesFile(modelConfigFilePath);
			C3Util.log("Loaded model config: " + modelConfigFilePath);
		}

		// order services
		Collections.sort(_orderedServices, new Comparator<IC3ServiceInternal>() {
			public int compare(IC3ServiceInternal s1, IC3ServiceInternal s2) {
				return s1.serviceGetPriority() - s2.serviceGetPriority();
			}
		});

		// initialize services
		C3Util.log("Initializing services: ", false);
		for (int i = _orderedServices.size() - 1; i >= 0; i--) {
			IC3ServiceInternal c3Service = _orderedServices.get(i);
			C3Util.log(c3Service.getServiceId(), false);
			if (i != 0) {
				C3Util.log(", ", false);
			}
			c3Service.serviceInit(this);
		}
		C3Util.log(".");

		// start services
		for (int i = _orderedServices.size() - 1; i >= 0; i--) {
			IC3ServiceInternal c3Service = _orderedServices.get(i);
			C3Util.log("Starting service '" + c3Service.getServiceId() //
					+ "' (prio: " + c3Service.serviceGetPriority() + ") ...");
			c3Service.serviceStart();
			C3Util.log("Service '" + c3Service.getServiceId() + "' started.");
		}

		_coreMonitor.start();
	}

	void activatorStop() {
		_coreMonitor.stop();
	}

	//
	//

	private MonitorThread _coreMonitor = new MonitorThread() {

		protected long getPreSleepMillis() {
			return 1000;
		}

		public boolean cycle() throws Exception {
			int c3Pid = C3Util.getC3PID();
			return c3Pid != -1;
		}

		public void endCycle() throws Exception {
			C3Util.log("Stopping c3...");

			// stop services
			for (int i = 0; i < _orderedServices.size(); i++) {
				IC3ServiceInternal c3Service = _orderedServices.get(i);
				C3Util.log("Stopping service '" + c3Service.getServiceId() + "' ...");
				c3Service.serviceStop();
				C3Util.log("Service '" + c3Service.getServiceId() + "' stopped.");
			}

			C3Util.log("All c3 services stopped.");
			_bundleContext.getBundle(0).stop();
		}
	};

}

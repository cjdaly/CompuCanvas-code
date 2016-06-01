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
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeMap;

import org.osgi.framework.BundleContext;

import net.locosoft.CompuCanvas.controller.core.AbstractC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3ServiceInternal;
import net.locosoft.CompuCanvas.controller.core.ICoreService;
import net.locosoft.CompuCanvas.controller.util.C3Util;
import net.locosoft.CompuCanvas.controller.util.FileUtil;
import net.locosoft.CompuCanvas.controller.util.MonitorThread;

public class CoreService extends AbstractC3Service implements ICoreService {

	// ICoreService

	public IC3Service getService(String id) {
		return _idToService.get(id);
	}

	public <C3S extends IC3Service> IC3Service getService(Class<C3S> serviceInterface) {
		return _ifaceToService.get(serviceInterface);
	}

	public String getModelConfig(String key) {
		return (_modelConfig == null) ? null //
				: _modelConfig.getProperty(key);
	}

	// IC3Service

	public Class<? extends IC3Service> getServiceInterface() {
		return ICoreService.class;
	}

	// for CoreActivator

	void activatorInit(BundleContext bundleContext, TreeMap<String, IC3ServiceInternal> idToService,
			HashMap<Class<? extends IC3Service>, IC3ServiceInternal> ifaceToService) {
		_bundleContext = bundleContext;
		_idToService = idToService;
		_ifaceToService = ifaceToService;
	}

	void activatorStart() {
		System.out.println("Starting c3...");
		System.out.println("C3 internal version: " + C3Util.getC3InternalVersion());
		System.out.println("CompuCanvas model: " + C3Util.getCompuCanvasModelId());

		String modelConfigFilePath = C3Util.getC3ConfigDir() + "/model/" + C3Util.getCompuCanvasModelId()
				+ ".properties";
		File modelConfigFile = new File(modelConfigFilePath);
		if (modelConfigFile.exists()) {
			_modelConfig = FileUtil.loadPropertiesFile(modelConfigFilePath);
			System.out.println("Loaded config: " + modelConfigFilePath);
		}

		for (IC3ServiceInternal c3Service : _idToService.values()) {
			System.out.println("Starting service '" + c3Service.getServiceId() + "' ...");
			c3Service.serviceInit(this);
			c3Service.serviceStart();
			System.out.println("Service '" + c3Service.getServiceId() + "' started.");
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
			int c3Pid = C3Util.getC3Pid();
			return c3Pid != -1;
		}

		public void endCycle() throws Exception {
			System.out.println("Stopping c3...");

			for (IC3ServiceInternal c3Service : _idToService.values()) {
				System.out.println("Stopping service '" + c3Service.getServiceId() + "' ...");
				c3Service.serviceStop();
				System.out.println("Service '" + c3Service.getServiceId() + "' stopped.");
			}

			System.out.println("All c3 services stopped.");
			_bundleContext.getBundle(0).stop();
		}
	};

	private BundleContext _bundleContext;
	private TreeMap<String, IC3ServiceInternal> _idToService;
	private HashMap<Class<? extends IC3Service>, IC3ServiceInternal> _ifaceToService;

	private Properties _modelConfig;
}

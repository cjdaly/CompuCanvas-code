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
			HashMap<Class<? extends IC3Service>, IC3ServiceInternal> ifaceToService,
			ArrayList<IC3ServiceInternal> orderedServices) {
		_bundleContext = bundleContext;
		_idToService = idToService;
		_ifaceToService = ifaceToService;
		_orderedServices = orderedServices;
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

		// order services
		Collections.sort(_orderedServices, new Comparator<IC3ServiceInternal>() {
			public int compare(IC3ServiceInternal s1, IC3ServiceInternal s2) {
				return s1.serviceGetPriority() - s2.serviceGetPriority();
			}
		});

		// initialize services
		System.out.print("Initializing services: ");
		for (int i = _orderedServices.size() - 1; i >= 0; i--) {
			IC3ServiceInternal c3Service = _orderedServices.get(i);
			System.out.print(c3Service.getServiceId());
			if (i != 0) {
				System.out.print(", ");
			}
			c3Service.serviceInit(this);
		}
		System.out.println(".");

		// start services
		for (int i = _orderedServices.size() - 1; i >= 0; i--) {
			IC3ServiceInternal c3Service = _orderedServices.get(i);
			System.out.println("Starting service '" + c3Service.getServiceId() + "' ...");
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

			// stop services
			for (int i = 0; i < _orderedServices.size(); i++) {
				IC3ServiceInternal c3Service = _orderedServices.get(i);
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
	private ArrayList<IC3ServiceInternal> _orderedServices;

	private Properties _modelConfig;
}

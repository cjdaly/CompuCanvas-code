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

import java.util.HashMap;
import java.util.TreeMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import net.locosoft.CompuCanvas.controller.core.IC3Service;
import net.locosoft.CompuCanvas.controller.core.IC3ServiceInternal;

public class CoreActivator implements BundleActivator {

	private CoreService _coreService;

	public void start(BundleContext bundleContext) throws Exception {
		_coreService = registerC3Services(bundleContext);
		_coreService.activatorStart();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		_coreService.activatorStop();
	}

	private CoreService registerC3Services(BundleContext bundleContext) {
		TreeMap<String, IC3ServiceInternal> idToService = new TreeMap<String, IC3ServiceInternal>();
		HashMap<Class<? extends IC3Service>, IC3ServiceInternal> ifaceToService = new HashMap<Class<? extends IC3Service>, IC3ServiceInternal>();
		CoreService coreService = null;

		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IConfigurationElement[] configurationElements = extensionRegistry
				.getConfigurationElementsFor("net.locosoft.CompuCanvas.controller.core.C3Service");
		for (IConfigurationElement configurationElement : configurationElements) {
			try {
				String id = configurationElement.getAttribute("id");
				Object extension = configurationElement.createExecutableExtension("implementation");

				IC3ServiceInternal c3Service = (IC3ServiceInternal) extension;
				c3Service.serviceRegister(id);

				idToService.put(id, c3Service);
				ifaceToService.put(c3Service.getServiceInterface(), c3Service);

				if (c3Service instanceof CoreService) {
					coreService = (CoreService) c3Service;
					coreService.activatorInit(bundleContext, idToService, ifaceToService);
				}

			} catch (ClassCastException ex) {
				ex.printStackTrace();
			} catch (CoreException ex) {
				ex.printStackTrace();
			}
		}
		return coreService;
	}

}

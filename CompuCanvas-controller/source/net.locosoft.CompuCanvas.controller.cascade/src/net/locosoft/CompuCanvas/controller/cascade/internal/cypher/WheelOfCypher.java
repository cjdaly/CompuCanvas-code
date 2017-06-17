/*****************************************************************************
 * Copyright (c) 2017 Chris J Daly (github user cjdaly)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   cjdaly - initial API and implementation
 ****************************************************************************/

package net.locosoft.CompuCanvas.controller.cascade.internal.cypher;

import java.util.ArrayList;
import java.util.HashMap;

import org.neo4j.driver.v1.summary.SummaryCounters;

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.cascade.internal.CascadeService;
import net.locosoft.CompuCanvas.controller.core.ICoreService;

public class WheelOfCypher {

	public static WheelOfCypher getDefault(CascadeService cascadeService) {
		WheelOfCypher wheel = new WheelOfCypher(cascadeService);

		wheel.addGear("wake");
		wheel.addCog("wake", new ControlWake());
		wheel.addCog("wake", new ImpressionCull());
		wheel.addCog("wake", new ImpressionInject());
		wheel.addCog("wake", new ImpressionBind());
		wheel.setGear("wake");

		wheel.addGear("sleep");
		wheel.addCog("sleep", new ControlSleep());

		return wheel;
	}

	private CascadeService _cascadeService;
	private ICoreService _coreService;

	private HashMap<String, ArrayList<Cog>> _gears = new HashMap<String, ArrayList<Cog>>();
	private String _gearId;
	private int _cogIndex = 0;

	public WheelOfCypher(CascadeService cascadeService) {
		_cascadeService = cascadeService;
		_coreService = _cascadeService.getCoreService().getService(ICoreService.class);
	}

	void addGear(String id) {
		_gears.put(id, new ArrayList<Cog>());
	}

	void addCog(String gearId, Cog cog) {
		ArrayList<Cog> gear = getGear(gearId);
		gear.add(cog);
		cog._wheel = this;
	}

	ArrayList<Cog> getGear(String gearId) {
		return _gears.get(gearId);
	}

	int getCogCount(String gearId) {
		return getGear(gearId).size();
	}

	void setGear(String gearId) {
		ArrayList<Cog> gear = getGear(gearId);
		if (gear != null) {
			_gearId = gearId;
			_cogIndex = 0;
		}
	}

	public Cypher nextCypher() {
		ArrayList<Cog> gear = getGear(_gearId);

		Cog cog = gear.get(_cogIndex);
		if (cog == null)
			return null;

		_cogIndex++;
		if (_cogIndex >= gear.size())
			_cogIndex = 0;

		return cog.newCypher();
	}

	public static abstract class Cog {

		private WheelOfCypher _wheel;

		public abstract Cypher newCypher();

		protected ICoreService getCoreService() {
			return _wheel._coreService;
		}

		protected void setGear(String gearId) {
			_wheel.setGear(gearId);
		}

		protected boolean isLoggingEnabled() {
			return _wheel._cascadeService.serviceIsLoggingEnabled("cypher");
		}

		protected String getSummaryText(SummaryCounters counters) {
			if (counters == null) {
				return getClass().getSimpleName() + " - ???";
			} else {
				return getClass().getSimpleName() + " - " //
						+ " N: +" + counters.nodesCreated() + ", -" + counters.nodesDeleted() //
						+ ", L: +" + counters.labelsAdded() + ", -" + counters.labelsRemoved() //
						+ ", R: +" + counters.relationshipsCreated() + ", -" + counters.relationshipsDeleted() //
						+ ", P: " + counters.propertiesSet();
			}
		}
	}
}

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

import org.neo4j.driver.v1.summary.SummaryCounters;

import net.locosoft.CompuCanvas.controller.Neo4j.Cypher;
import net.locosoft.CompuCanvas.controller.cascade.internal.CascadeService;
import net.locosoft.CompuCanvas.controller.core.ICoreService;

public class WheelOfCypher {

	public static WheelOfCypher getDefault(CascadeService cascadeService) {
		WheelOfCypher wheel = new WheelOfCypher(cascadeService, 3);

		wheel.add(0, new ImpressionCull());
		wheel.add(1, new ImpressionInject());
		wheel.add(2, new ImpressionBind());

		return wheel;
	}

	private CascadeService _cascadeService;
	private ICoreService _coreService;
	private Cog[] _cogs;
	private int _position = 0;

	public WheelOfCypher(CascadeService cascadeService, int size) {
		_cascadeService = cascadeService;
		_coreService = _cascadeService.getCoreService().getService(ICoreService.class);
		_cogs = new Cog[size];
	}

	public void add(int position, Cog cog) {
		_cogs[position] = cog;
		cog._wheel = this;
	}

	public Cypher nextCypher() {
		Cog cog = _cogs[_position];
		if (cog == null)
			return null;

		_position++;
		if (_position >= _cogs.length)
			_position = 0;

		return cog.newCypher();
	}

	public static abstract class Cog {

		private WheelOfCypher _wheel;

		public abstract Cypher newCypher();

		protected ICoreService getCoreService() {
			return _wheel._coreService;
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

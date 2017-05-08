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
import net.locosoft.CompuCanvas.controller.core.ICoreService;

public class WheelOfCypher {

	public static WheelOfCypher getDefault(ICoreService coreService) {
		WheelOfCypher wheel = new WheelOfCypher(2);

		Cog impressionInject = new ImpressionInject(coreService);
		wheel.add(0, impressionInject);

		Cog impressionCull = new ImpressionCull();
		wheel.add(1, impressionCull);

		return wheel;
	}

	private int _position = 0;
	private Cog[] _cogs;

	public WheelOfCypher(int size) {
		_cogs = new Cog[size];
	}

	public void add(int position, Cog cog) {
		_cogs[position] = cog;
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
		public abstract Cypher newCypher();

		protected String getSummaryText(SummaryCounters counters) {
			if (counters == null) {
				return getClass().getSimpleName() + " - ???";
			} else {
				return getClass().getSimpleName() + " - nodes: +" + counters.nodesCreated() + ", -"
						+ counters.nodesDeleted() + ", rels: +" + counters.relationshipsCreated() + ", -"
						+ counters.relationshipsDeleted();
			}
		}
	}
}

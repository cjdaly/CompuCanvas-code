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

package net.locosoft.CompuCanvas.controller.Neo4j;

import net.locosoft.CompuCanvas.controller.core.IC3Service;

public interface INeo4jService extends IC3Service {

	public boolean isAcceptingCypher();

	public int getCypherQueueLength();

	public void runCypher(Cypher cypher);

}

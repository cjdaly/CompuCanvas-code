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

import java.util.HashMap;
import java.util.Map;

import org.neo4j.driver.v1.StatementResult;

public abstract class Cypher {

	private HashMap<String, Object> _params = new HashMap<String, Object>();
	private boolean _useTransaction = false;
	private boolean _wasHandled = false;

	public Cypher() {
		this(false);
	}

	public Cypher(boolean useTransaction) {
		_useTransaction = useTransaction;
	}

	public abstract String getText();

	protected void handle(StatementResult result) {
	}

	public boolean useTransaction() {
		return _useTransaction;
	}

	public Map<String, Object> getParams() {
		return _params;
	}

	public void addParam(String key, String value) {
		_params.put(key, value);
	}

	public void addParam(String key, long value) {
		_params.put(key, value);
	}

	public void addParam(String key, double value) {
		_params.put(key, value);
	}

	public void addParam(String key, Map<String, Object>[] map) {
		_params.put(key, map);
	}

	public boolean wasHandled() {
		return _wasHandled;
	}

	public final void handleResult(StatementResult result) {
		handle(result);
		_wasHandled = true;
	}

}

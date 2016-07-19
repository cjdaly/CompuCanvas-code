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
import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.summary.ResultSummary;

public abstract class Cypher {

	private HashMap<String, Object> _params = new HashMap<String, Object>();
	private List<Record> _results;
	private ResultSummary _summary;
	private boolean _retainResults = false;
	private boolean _retainSummary = false;
	private boolean _wasHandled = false;

	public Cypher() {
		this(false);
	}

	public Cypher(boolean retainResults) {
		this(retainResults, false);
	}

	public Cypher(boolean retainResults, boolean retainSummary) {
		_retainResults = retainResults;
		_retainSummary = retainSummary;
	}

	public abstract String getText();

	protected abstract void handle(StatementResult result);

	public Map<String, Object> getParams() {
		return _params;
	}

	public List<Record> getResults() {
		return _results;
	}

	public ResultSummary getSummary() {
		return _summary;
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

	public boolean wasHandled() {
		return _wasHandled;
	}

	public void handleResult(StatementResult result) {
		if (_retainResults) {
			_results = result.list();
		}
		if (_retainSummary) {
			_summary = result.consume();
		}
		handle(result);
		_wasHandled = true;
	}

}

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

package net.locosoft.CompuCanvas.controller.util;

import java.util.LinkedList;

public class CommandLineQueue {

	private LinkedList<String> _commandQueue = new LinkedList<String>();

	public synchronized void enqueueCommand(String command) {
		_commandQueue.add(command);
	}

	public synchronized String dequeueCommand() {
		if (_commandQueue.isEmpty())
			return null;
		else
			return _commandQueue.removeFirst();
	}

	public synchronized String peekCommand() {
		if (_commandQueue.isEmpty())
			return null;
		else
			return _commandQueue.getFirst();
	}

	public synchronized int countCommands() {
		return _commandQueue.size();
	}

	public synchronized void clearCommands() {
		_commandQueue.clear();
	}

}

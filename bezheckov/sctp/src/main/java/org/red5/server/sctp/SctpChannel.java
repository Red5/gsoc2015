/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.red5.server.sctp;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.channels.spi.SelectorProvider;

public class SctpChannel extends AbstractSelectableChannel {
	
	public static enum State {
		CLOSED,
		COOKIE_WAIT,
		COOKIE_ECHOED,
		ESTABLISHED
	}
	
	private int verificationTag;
	
	private short port;
	
	private InetAddress ipAddress;
	
	private State state;

	public SctpChannel(SelectorProvider provider, final int verificationTag) {
		super(provider);
		setState(State.CLOSED);
		this.verificationTag = verificationTag;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public int getVerificationTag() {
		return verificationTag;
	}

	@Override
	protected void implCloseSelectableChannel() throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	protected void implConfigureBlocking(boolean block) throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public int validOps() {
		// TODO Auto-generated method stub
		return 0;
	}
}

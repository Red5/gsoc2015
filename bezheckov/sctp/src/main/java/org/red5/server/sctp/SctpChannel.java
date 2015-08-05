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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Random;

import org.red5.server.sctp.packet.SctpPacket;

public class SctpChannel implements IChannelControl {
	
	private static final int validCookieTime = 60; // in seconds
	
	private Timestamp creationTimestamp;
	
	private int verificationTagItself;
	
	private int verificationTag;
	
	private State state;
	
	private DatagramSocket destination;

	public SctpChannel(final Random random, SocketAddress destinationAddress) throws SocketException {
		setState(State.CLOSED);
		setVerificationTagItself(random.nextInt());
		destination = new DatagramSocket(destinationAddress);
		creationTimestamp = new Timestamp(System.currentTimeMillis());
	}

	public State getState() {
		return state;
	}
	
	public int getVerificationTag() {
		return verificationTag;
	}

	@Override
	public void setState(State state) {
		this.state = state;
	}

	@Override
	public void sendPacket(SctpPacket packet) throws IOException {
		byte[] data = packet.getBytes();
		destination.send(new DatagramPacket(data, data.length));
	}

	public int getVerificationTagItself() {
		return verificationTagItself;
	}

	public void setVerificationTagItself(int verificationTagItself) {
		this.verificationTagItself = verificationTagItself;
	}
}

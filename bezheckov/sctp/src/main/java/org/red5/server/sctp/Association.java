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
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Random;

import org.red5.server.sctp.packet.SctpPacket;
import org.red5.server.sctp.packet.chunks.Init;

public class Association implements IAssociationControl {
	
	private Timestamp creationTimestamp;
	
	private int verificationTagSource;
	
	private int verificationTagDestination;
	
	private int initialTSNSource;
	
	private int initialTSNDestination;
	
	private State state;
	
	private DatagramSocket source;
	
	private DatagramSocket destination;
	
	private Random random;

	public Association(final Random random, InetSocketAddress sourceAddress) throws SocketException {
		this.random = random;
		setState(State.CLOSED);
		setVerificationTagItself(random.nextInt());
		destination = new DatagramSocket(sourceAddress);
		creationTimestamp = new Timestamp(System.currentTimeMillis());
	}
	
	public boolean setUp(InetSocketAddress address) throws IOException {
		initialTSNSource = random.nextInt();
		Init initChunk = new Init(verificationTagSource, initialTSNSource);
		SctpPacket packet = new SctpPacket((short) source.getPort(), (short)destination.getPort(), 0, initChunk);
		byte[] data = packet.getBytes();
		source.send(new DatagramPacket(data, data.length, address));
		
		// wait init_ack
		// receive init_ack
		// send cookie_echo
		// receive cookie_ack
		
		return true;
	}

	public State getState() {
		return state;
	}
	
	public void setDestination(DatagramSocket destination) {
		this.destination = destination;
	}
	
	public int getVerificationTag() {
		return verificationTagDestination;
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
		return verificationTagSource;
	}

	public void setVerificationTagItself(int verificationTagItself) {
		this.verificationTagSource = verificationTagItself;
	}
}

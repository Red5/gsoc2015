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
package org.red5.server.sctp.packet.chunks;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.red5.server.sctp.IAssociationControl;
import org.red5.server.sctp.IServerChannelControl;
import org.red5.server.sctp.packet.SctpPacket;
import org.red5.server.sctp.SctpException;

public class Init extends Chunk {
	
	// initiateTag(4 byte) + advertisedReceiverWindowCredit(4 byte) + numberOfOutboundStreams(2 byte) + numberOfInboundStreams(2 byte) + TSN(4 byte) 
	private static final int MANDATORY_FIELD_SIZE = 16;

	private int initiateTag;
	
	private int advertisedReceiverWindowCredit;
	
	private short numberOfOutboundStreams;
	
	private short numberOfInboundStreams;
	
	private int initialTSN;
	
	public Init(short length, byte[] data) {
		super(ChunkType.INIT, (byte) 0x00, length, data);
	}
	
	public Init(int initialTSN, int initiateTag) {
		super(ChunkType.INIT, (byte)0x00);
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(MANDATORY_FIELD_SIZE);
		byteBuffer.putInt(initiateTag);
		byteBuffer.putInt(IAssociationControl.DEFAULT_ADVERTISE_RECEIVE_WINDOW_CREDIT);
		byteBuffer.putShort((short) IAssociationControl.DEFAULT_NUMBER_OF_OUTBOUND_STREAM);
		byteBuffer.putShort((short) IAssociationControl.DEFAULT_NUMBER_OF_INBOUND_STREAM);
		byteBuffer.putInt(initialTSN);
		super.setData(byteBuffer.toString().getBytes());
		super.setLength(MANDATORY_FIELD_SIZE);
	}
	
	public Init(final byte[] data, int offset, int length) throws SctpException {
		super(data, offset, length);
		ByteBuffer byteBuffer = ByteBuffer.wrap(data, offset + CHUNK_HEADER_SIZE, data.length - (offset + CHUNK_HEADER_SIZE));
		setLength((short)(data.length - (offset + CHUNK_HEADER_SIZE)));
		initiateTag = byteBuffer.getInt();
		advertisedReceiverWindowCredit = byteBuffer.getInt();
		numberOfOutboundStreams = byteBuffer.getShort();
		numberOfInboundStreams = byteBuffer.getShort();
		initialTSN = byteBuffer.getInt();
	}

	public int getInitiateTag() {
		return initiateTag;
	}

	public int getAdvertisedReceiverWindowCredit() {
		return advertisedReceiverWindowCredit;
	}

	public short getNumberOfOutboundStreams() {
		return numberOfOutboundStreams;
	}

	public short getNumberOfInboundStreams() {
		return numberOfInboundStreams;
	}

	public int getInitialTSN() {
		return initialTSN;
	}

	@Override
	public void apply(InetSocketAddress address, IServerChannelControl server)
			throws SctpException, InvalidKeyException, NoSuchAlgorithmException, IOException {
		IAssociationControl association = server.getPendingChannel(address);
		if (association != null) {
			throw new SctpException("init chunk : association already exist in pending pool");
		}
		
		// 1. generate state cookie & initAck chunk
		int verificationTag = server.getRandom().nextInt();
		int initialTSN = server.getRandom().nextInt();
		StateCookie stateCookie = new StateCookie(verificationTag, initialTSN);
		Chunk initAck = new InitAck(verificationTag, initialTSN, stateCookie, server.getMac());
		
		// 2. pack and send packet with initAck inside
		short sourcePort = (short)server.getPort();
		SctpPacket packet = new SctpPacket(sourcePort, (short)address.getPort(), verificationTag, initAck);
		server.send(packet, address);
	}

	@Override
	public void apply(IAssociationControl association) throws SctpException {
		throw new SctpException("init chunk : association already exist in pending pool");
	}
}

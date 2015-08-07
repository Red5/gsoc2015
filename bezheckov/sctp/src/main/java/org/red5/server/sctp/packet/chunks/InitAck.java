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

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import org.red5.server.sctp.IAssociationControl;
import org.red5.server.sctp.IServerChannelControl;
import org.red5.server.sctp.SctpException;

public final class InitAck extends Chunk {
	
	private static final int MANDATORY_FIELD_SIZE = 16;
	
	private static final int DEFAULT_ADVERTISE_RECEIVE_WINDOW_CREDIT = 1024;
	
	private static final int DEFAULT_NUMBER_OF_OUTBOUND_STREAM = 1;
	
	private static final int DEFAULT_NUMBER_OF_INBOUND_STREAM = 1;
	
	private int initiateTag;
	
	private int advertisedReceiverWindowCredit;
	
	private int numberOfOutboundStreams;
	
	private int numberOfInboundStreams;
	
	private int initialTSN;
	
	StateCookie stateCookie;
	
	public InitAck(byte flags, short length, byte[] data) {
		super(ChunkType.INIT_ACK, flags, length, data);
	}
	
	public InitAck(final byte[] data, int offset, int length) throws SctpException {
		super(data, offset, length);
		assert length - offset - CHUNK_HEADER_SIZE > MANDATORY_FIELD_SIZE;
		ByteBuffer byteBuffer = ByteBuffer.wrap(data, offset + CHUNK_HEADER_SIZE, length - offset - CHUNK_HEADER_SIZE);
		initiateTag = byteBuffer.getInt();
		advertisedReceiverWindowCredit = byteBuffer.getInt();
		numberOfOutboundStreams = byteBuffer.getShort() & 0xffff;
		numberOfInboundStreams = byteBuffer.getShort() & 0xffff;
		initialTSN = byteBuffer.getInt();
		stateCookie = new StateCookie(data, offset, length);
	}
	
	public InitAck(int initiateTag, int initialTSN, byte[] stateCookie) {
		super(ChunkType.INIT_ACK, (byte)0x00);
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(MANDATORY_FIELD_SIZE + stateCookie.length);
		byteBuffer.putInt(initiateTag);
		byteBuffer.putInt(DEFAULT_ADVERTISE_RECEIVE_WINDOW_CREDIT);
		byteBuffer.putShort((short) DEFAULT_NUMBER_OF_OUTBOUND_STREAM);
		byteBuffer.putShort((short) DEFAULT_NUMBER_OF_INBOUND_STREAM);
		byteBuffer.putInt(initialTSN);
		byteBuffer.put(stateCookie);
		super.setData(byteBuffer.toString().getBytes());
		super.setLength(MANDATORY_FIELD_SIZE + stateCookie.length);
	}
	
	@Override
	public byte[] getBytes() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(MANDATORY_FIELD_SIZE + CHUNK_HEADER_SIZE);
		byte[] data = super.getBytes();
		byteBuffer.put(data);
		byteBuffer.putInt(initiateTag);
		byteBuffer.putInt(advertisedReceiverWindowCredit);
		byteBuffer.putShort((short) numberOfOutboundStreams);
		byteBuffer.putShort((short) numberOfInboundStreams);
		byteBuffer.putInt(initialTSN);
		return byteBuffer.toString().getBytes();
	}
	
	@Override
	public void apply(IAssociationControl channel) {
		// TODO Auto-generated method stub
	}

	@Override
	public void apply(InetSocketAddress address, IServerChannelControl server) {
		// TODO Auto-generated method stub
	}
	
	public int getInitiateTag() {
		return initiateTag;
	}

	public int getAdvertisedReceiverWindowCredit() {
		return advertisedReceiverWindowCredit;
	}

	public int getNumberOfOutboundStreams() {
		return numberOfOutboundStreams;
	}

	public int getNumberOfInboundStreams() {
		return numberOfInboundStreams;
	}

	public int getInitialTSN() {
		return initialTSN;
	}
}

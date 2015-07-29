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

import java.nio.ByteBuffer;

public final class InitAck extends Chunk {
	
	private static int MANDATORY_FIELD_SIZE = 16;
	
	private int initiateTag;
	
	private int advertisedReceiverWindowCredit;
	
	private short numberOfOutboundStreams;
	
	private short numberOfInboundStreams;
	
	private int initialTSN;
	
	public InitAck(byte flags, short length, byte[] data) {
		super(ChunkType.INIT_ACK, flags, length, data);
	}
	
	public InitAck(
			int initiateTag,
			int advertisedReceiverWindowCredit,
			short numberOfOutboundStreams,
			short numberOfInboundStreams,
			int initialTSN) {
		super(ChunkType.INIT_ACK, (byte)0x00);
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(MANDATORY_FIELD_SIZE);
		byteBuffer.putInt(initiateTag);
		byteBuffer.putInt(advertisedReceiverWindowCredit);
		byteBuffer.putShort(numberOfOutboundStreams);
		byteBuffer.putShort(numberOfInboundStreams);
		byteBuffer.putInt(initialTSN);
		super.setData(byteBuffer.toString().getBytes());
		super.setLength((short) MANDATORY_FIELD_SIZE);
	}
	
	@Override
	public byte[] getBytes() {
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(MANDATORY_FIELD_SIZE + CHUNK_HEADER_SIZE);
		byte[] data = super.getBytes();
		byteBuffer.put(data);
		byteBuffer.putInt(initiateTag);
		byteBuffer.putInt(advertisedReceiverWindowCredit);
		byteBuffer.putShort(numberOfOutboundStreams);
		byteBuffer.putShort(numberOfInboundStreams);
		byteBuffer.putInt(initialTSN);
		return byteBuffer.toString().getBytes();
	}
	
	public InitAck(final byte[] data, final int offset) {
		super(data, offset);
		ByteBuffer byteBuffer = ByteBuffer.wrap(data, offset + CHUNK_HEADER_SIZE, data.length - CHUNK_HEADER_SIZE);
		initiateTag = byteBuffer.getInt();
		advertisedReceiverWindowCredit = byteBuffer.getInt();
		numberOfOutboundStreams = byteBuffer.getShort();
		numberOfInboundStreams = byteBuffer.getShort();
		initialTSN = byteBuffer.getInt();
		// TODO handle State cookie chunk
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
}

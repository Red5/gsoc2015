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
package org.red5.server.sctp.packet;

import java.nio.ByteBuffer;

public final class InitChunk extends Chunk {

	private int InitiateTag;
	
	private int AdvertisedReceiverWindowCredit;
	
	private short NumberOfOutboundStreams;
	
	private short NumberOfInboundStreams;
	
	private int InitialTSN;
	
	public InitChunk(byte flags, short length, byte[] data) {
		super(ChunkType.INIT, (byte) 0x00, length, data);
	}
	
	@Override
	public void parse(byte[] data, int offset) {
		super.parse(data, offset);
		ByteBuffer byteBuffer = ByteBuffer.wrap(data, offset + CHUNK_HEADER_SIZE, data.length - CHUNK_HEADER_SIZE);
		InitiateTag = byteBuffer.getInt();
		AdvertisedReceiverWindowCredit = byteBuffer.getInt();
		NumberOfOutboundStreams = byteBuffer.getShort();
		NumberOfInboundStreams = byteBuffer.getShort();
		InitialTSN = byteBuffer.getInt();
	}

	public int getInitiateTag() {
		return InitiateTag;
	}

	public int getAdvertisedReceiverWindowCredit() {
		return AdvertisedReceiverWindowCredit;
	}

	public short getNumberOfOutboundStreams() {
		return NumberOfOutboundStreams;
	}

	public short getNumberOfInboundStreams() {
		return NumberOfInboundStreams;
	}

	public int getInitialTSN() {
		return InitialTSN;
	}
}

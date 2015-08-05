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
import java.nio.ByteBuffer;

import org.red5.server.sctp.IChannelControl;
import org.red5.server.sctp.IServerChannelControl;
import org.red5.server.sctp.SctpException;

/*
 * see https://tools.ietf.org/html/rfc4960#section-3.2
 */
public abstract class Chunk {
	
	// type(1 byte) + flags(1 byte) + length(2 byte)
	protected static final int CHUNK_HEADER_SIZE = 4;
	
	private byte[] data;
	
	private ChunkType type;
	
	private byte flags;
	
	private int length;
	
	private IServerChannelControl server;
	
	public Chunk(byte[] data, int offset, int length, IServerChannelControl server) throws SctpException {
		// parse common header
		if (length < CHUNK_HEADER_SIZE) {
			throw new SctpException("not enough data for parse chunk common header " + data);
		}
		ByteBuffer byteBuffer = ByteBuffer.wrap(data, offset, CHUNK_HEADER_SIZE);
		type = ChunkType.values()[byteBuffer.get()];
		flags = byteBuffer.get();
		this.length = byteBuffer.getShort() & 0xffff;
		this.server = server;
	}
	
	public Chunk(final ChunkType type, final byte flags, final short length, final byte[] data) {
		this.type = type;
		this.flags = flags;
		this.length = length;
		this.data = data;
	}
	
	public abstract void apply(IChannelControl channel) throws SctpException, IOException;
	
	public Chunk(final ChunkType type, final byte flags) {
		this.type = type;
		this.flags = flags;
	}

	public int getSize() {
		return CHUNK_HEADER_SIZE + length;
	}

	public byte[] getBytes() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(CHUNK_HEADER_SIZE);
		byteBuffer.put((byte)type.getValue());
		byteBuffer.put(flags);
		byteBuffer.putShort((short)length);
		byte[] data = new byte[byteBuffer.limit()];
		byteBuffer.flip();
		byteBuffer.get(data);
		
		return data;
	}
	
	protected void setLength(int length) {
		this.length = length;
	}
	
	protected void setData(byte[] data) {
		this.data = data;
	}

	protected IServerChannelControl getServer() {
		return server;
	}
}

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

import java.util.ArrayList;

import org.red5.server.sctp.SctpException;
import org.red5.server.sctp.packet.chunks.Chunk;
import org.red5.server.sctp.packet.chunks.ChunkFactory;

public class SctpPacket {
	
	private SctpHeader header;
	
	private ArrayList<Chunk> chunks = new ArrayList<>();
	
	public byte[] getBytes() {
		int resultSize = getHeader().getSize();
		
		for (Chunk chunk : getChunks()) {
			resultSize += chunk.getSize();
		}
		
		byte[] result = new byte[resultSize];
		System.arraycopy(getHeader().getBytes(), 0, result, 0, getHeader().getSize());
		int previousSize = getHeader().getSize();
		for (Chunk chunk : getChunks()) {
			System.arraycopy(chunk.getBytes(), 0, result, previousSize, chunk.getSize());
			previousSize += chunk.getSize();
		}
		
		return result;
	}
	
	public SctpPacket(final byte[] data) throws SctpException {
		header = new SctpHeader(data);
		Chunk chunk = null;
		for (int i = header.getSize(); i < data.length; i += chunk.getSize()) {
			chunk = ChunkFactory.createChunk(data, i);
			getChunks().add(chunk);
		}
	}

	public SctpHeader getHeader() {
		return header;
	}

	public ArrayList<Chunk> getChunks() {
		return chunks;
	}
}

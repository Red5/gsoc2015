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

/*
 * see https://tools.ietf.org/html/rfc4960#section-3.1
 */
public class SctpHeader {
	
	private short sourcePort;
	
	private short destinationPort;
	
	private int verificationTag;
	
	private int checksum;
	
	// sourcePort(16 bit) + destinationPort(16 bit) + verificationTag(32 bit) + checksum(32 bit)
	private static final int HEADER_SIZE = 96;
	
	public SctpHeader(
			final short sourcePort,
			final short destinationPort,
			final int verificationTag,
			final int checksum
			) {
		this.sourcePort = sourcePort;
		this.destinationPort = destinationPort;
		this.verificationTag = verificationTag;
		this.checksum = checksum;
	}

	public int getSize() {
		return HEADER_SIZE;
	}

	public byte[] getBytes() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_SIZE);
		byteBuffer.putShort(sourcePort);
		byteBuffer.putShort(destinationPort);
		byteBuffer.putInt(verificationTag);
		byteBuffer.putInt(checksum);
		
		return byteBuffer.array();
	}
}

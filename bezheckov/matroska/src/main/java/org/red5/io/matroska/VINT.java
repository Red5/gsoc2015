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
package org.red5.io.matroska;

import static org.red5.io.matroska.ParserUtils.BIT_IN_BYTE;

import java.util.BitSet;

/**
 * variable size integer class
 * <a href="http://matroska.org/technical/specs/rfc/index.html">EBML RFC</a>
 */
public class VINT {
	public static final long MASK_BYTE_4 = 0b00001111111111111111111111111111;
	public static final long MASK_BYTE_3 = MASK_BYTE_4 >> BIT_IN_BYTE;
	public static final long MASK_BYTE_2 = MASK_BYTE_3 >> BIT_IN_BYTE;
	public static final long MASK_BYTE_1 = MASK_BYTE_2 >> BIT_IN_BYTE;
	private long binaryValue;
	
	private byte length;
	
	private long value;

	public VINT(long binaryValue) {
		BitSet bs = BitSet.valueOf(new long[]{binaryValue});
		length = (byte)(1 + bs.length() / BIT_IN_BYTE);
		long mask = MASK_BYTE_4 >> (4 - length) * BIT_IN_BYTE;
		value = binaryValue & mask;
		this.binaryValue = binaryValue;
	}
	
	public VINT(long binaryValue, byte length, long value) {
		if (binaryValue == 0L) {
			BitSet bs = BitSet.valueOf(new long[]{value});
			bs.set(length * BIT_IN_BYTE - length);
			this.binaryValue = bs.toLongArray()[0];
		} else {
			this.binaryValue = binaryValue;
		}
		this.length = length;
		this.value = value;
	}
	
	public byte getLength() {
		return length;
	}
	
	public long getBinary() {
		return binaryValue;
	}
	
	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value= value ;
	}

	public byte[] encode() {
		return ParserUtils.getBytes(binaryValue, length);
	}
}

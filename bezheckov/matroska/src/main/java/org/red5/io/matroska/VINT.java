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
	public static final long MASK_BYTE_3 = 0b000111111111111111111111;
	public static final long MASK_BYTE_2 = 0b0011111111111111;
	public static final long MASK_BYTE_1 = 0b01111111;
	private long binaryValue;
	
	private byte length;
	
	private long value;

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
	
	@Override
	public String toString() {
		return String.format("%s(%s)", value, length);
	}
	
	public static VINT fromBinary(long binary) {
		BitSet bs = BitSet.valueOf(new long[]{binary});
		byte length = (byte)(1 + bs.length() / BIT_IN_BYTE);
		long mask = MASK_BYTE_4;
		switch (length) {
			case 3:
				mask = MASK_BYTE_3;
				break;
			case 2:
				mask = MASK_BYTE_2;
				break;
			case 1:
				mask = MASK_BYTE_1;
				break;
		}
		long value = binary & mask;
		return new VINT(binary, length, value);
	}
	
	public static VINT fromValue(long value) {
		BitSet bs = BitSet.valueOf(new long[]{value});
		byte length = (byte)(1 + bs.length() / BIT_IN_BYTE);
		if (bs.length() == length * BIT_IN_BYTE) {
			length ++;
		}
		bs.set(length * BIT_IN_BYTE - length);
		long binary = bs.toLongArray()[0];
		return new VINT(binary, length, value);
	}
}

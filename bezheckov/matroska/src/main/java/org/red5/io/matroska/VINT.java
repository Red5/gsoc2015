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
	private long binaryValue;
	
	private byte length;
	
	private long value;

	public VINT(long binaryValue) {
		BitSet bs = BitSet.valueOf(new long[]{binaryValue});
		int l = bs.length();
		if (l > 3 * BIT_IN_BYTE) {
			length = 4;
		} else if (l > 2 * BIT_IN_BYTE) {
			length = 3;
		} else if (l > BIT_IN_BYTE) {
			length = 2;
		} else {
			length = 1;
		}
		bs.set(length * BIT_IN_BYTE - length, false);
		value = bs.toLongArray()[0];
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

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

/**
 * variable size integer class
 * <a href="http://matroska.org/technical/specs/rfc/index.html">EBML RFC</a>
 */
public class VINT {
	private long binaryValue;
	
	private byte length;
	
	private long value;
	
	public VINT(long binaryValue, byte length, long value) {
		this.binaryValue = binaryValue;
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
}

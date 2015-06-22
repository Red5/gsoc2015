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
package webm2flv.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;

import webm2flv.matroska.ParserUtils;
import webm2flv.matroska.VINT;


public class UnsignedIntegerTag extends Tag {
	
	private long value;

	public UnsignedIntegerTag(String name, VINT id) throws IOException {
		super(name, id);
	}
	
	public UnsignedIntegerTag(String name, VINT id, VINT size) throws IOException {
		super(name, id, size);
	}

	@Override
	public void parse(InputStream inputStream) throws IOException {
		value = ParserUtils.parseInteger(inputStream, (int) getSize());
	}
	
	
	public void setDefaultValue(String newValue) {
		setValue(Integer.parseInt(newValue));
	}
	public void setValue(long newValue) {
		value = newValue;
		int newSize = binaryCodedSize(value, 0);
		this.size = new VINT(newSize, (byte)(4), newSize);
	}
	
	public long getValue() {
		return value;
	}
	
	protected byte[] dataToByteArray() {
		int arraySize = (int) size.getValue();
		byte[] bytes = new byte[arraySize];
		long tempValue = value;
		for (int i = 0; i < arraySize; ++i) {
			bytes[i] = (byte) (tempValue >> (arraySize - i - 1 << 3));
		}
		return bytes;
	}
	
	public String toString() {
		return (getName() + " = " + value);
	}

}

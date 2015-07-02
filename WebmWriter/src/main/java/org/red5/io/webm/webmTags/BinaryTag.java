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
package webmTags;

import java.io.IOException;
import java.io.InputStream;

import webm2flv.matroska.ParserUtils;
import webm2flv.matroska.VINT;


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

public class BinaryTag extends Tag {
	
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	private byte[] value;

	public BinaryTag(String name, VINT id) {
		super(name, id);
	}
	
	public BinaryTag(String name, VINT id, VINT size) {
		super(name, id, size);
	}
	
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for ( int j = 0; j < bytes.length; j++ ) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	protected byte[] dataToByteArray() {
		byte[] bytes = new byte[1];
		return bytes;
	}

	@Override
	public void parse(InputStream inputStream) throws IOException {
		value = ParserUtils.parseBinary(inputStream, (int) getSize());
	}
	
	public void setDefaultValue(String newValue) {
	}

	
	public void setValue(byte[] newValue) {
		value = newValue;
	}

	public byte[] getValue() {
		return value;
	}
	
	public String toString() {
		return (getName() + " = binary " + (int) getSize());
	}

}

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
package org.red5.io.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.red5.io.matroska.ConverterException;
import org.red5.io.matroska.ParserUtils;
import org.red5.io.matroska.VINT;

public class SimpleBlock extends Tag {

	private VINT trackNumber;
	
	private long timeCode;
	
	private boolean keyFrame;
	
	private byte[] binary;
	
	public SimpleBlock(String name, VINT id) {
		super(name, id);
	}
	
	public SimpleBlock(String name, VINT id, VINT size) {
		super(name, id, size);
	}
	
	@Override
	public void parse(InputStream inputStream) throws IOException, ConverterException {
		trackNumber = ParserUtils.readVINT(inputStream);
		timeCode = ParserUtils.parseInteger(inputStream, 2); // int16 by specification
		keyFrame = (0x80 == (inputStream.read() & 0x80));
		binary = ParserUtils.parseBinary(inputStream, (int) getSize() - 4);
	}

	@Override
	protected void putValue(ByteBuffer bb) throws IOException {
		bb.put(trackNumber.encode());
		bb.put(ParserUtils.getBytes(timeCode, 2));
		bb.put((byte)(keyFrame ? 0x80 : 0x00));
		bb.put(binary);
	}

	public byte[] getBinary() {
		return binary;
	}

	public long getTimeCode() {
		return timeCode;
	}

	public int getTrackNumber() {
		return (int)trackNumber.getValue();
	}
	
	@Override
	public String toString() {
		return (getName() + " = binary " + binary.length);
	}

	public boolean isKeyFrame() {
		return keyFrame;
	}
}

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
import org.red5.io.matroska.VINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.red5.io.matroska.ParserUtils.BIT_IN_BYTE;

public abstract class Tag {
	static Logger log = LoggerFactory.getLogger(Tag.class);
	private String name;

	private VINT id;

	VINT size;

	public Tag(String name, VINT id) {
		this(name, id, new VINT(0L, (byte) 0, 0L));
	}

	public Tag(String name, VINT id, VINT size) {
		this.name = name;
		this.id = id;
		this.size = size;
	}

	public abstract void parse(InputStream inputStream) throws IOException, ConverterException;
	
	protected abstract void putValue(ByteBuffer bb) throws IOException;

	public String getName() {
		return name;
	}

	public long getId() {
		return id.getBinary();
	}

	public long getSize() {
		return size.getValue();
	}

	public byte[] encode() throws IOException {
		final byte[] eId = id.encode();
		final byte[] eSize = size.encode();
		final ByteBuffer buf = ByteBuffer.allocate((int)(eId.length + eSize.length + getSize()));
		log.debug("Id: " + id.getValue() + "; Idl: " + id.getLength() + "; Length: " + buf.limit() + "; size: " + getSize());
		buf.put(eId);
		buf.put(eSize);
		putValue(buf);
		buf.flip();
		return buf.array();
	}
}

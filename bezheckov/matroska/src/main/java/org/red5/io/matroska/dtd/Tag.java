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

public abstract class Tag {
	static Logger log = LoggerFactory.getLogger(Tag.class);
	private String name;

	private VINT id;

	private VINT size;

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

	private static int binaryCodedSize(final long value, int minSizeLen) {
		int octetsNumber = 0;
		if (value < 127) {
			octetsNumber = 1;
		} else if (value < 16383) {
			octetsNumber = 2;
		} else if (value < 2097151) {
			octetsNumber = 3;
		} else if (value < 268435455) {
			octetsNumber = 4;
		}
		if ((minSizeLen > 0) && (octetsNumber <= minSizeLen)) {
			octetsNumber = minSizeLen;
		}

		return octetsNumber;
	}

	private static byte[] makeEbmlCodedSize(final long size, int minSizeLen) {
		final int len = binaryCodedSize(size, minSizeLen);
		final byte[] ret = new byte[len];
		long mask = 0x00000000000000FFL;
		for (int i = 0; i < len; i++) {
			ret[len - 1 - i] = (byte) ((size & mask) >>> (i * 8));
			mask <<= 8;
		}
		ret[0] |= 0x80 >> (len - 1);
		return ret;
	}

	public byte[] encode() throws IOException {
		int len = id.getLength();

		final byte[] encodedSize = makeEbmlCodedSize(getSize(), 0);

		len += encodedSize.length;
		len += getSize();
		final ByteBuffer buf = ByteBuffer.allocate(len);
		log.debug("Id:" + id.getValue() + "Idl:" + id.getLength() + ", Length:" + len + "GetSize():" + getSize());
		buf.put(id.encode());
		buf.put(encodedSize);
		putValue(buf);
		buf.flip();
		return buf.array();
	}
}

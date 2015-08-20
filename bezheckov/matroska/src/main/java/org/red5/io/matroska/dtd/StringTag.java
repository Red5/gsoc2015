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
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.red5.io.matroska.ParserUtils;
import org.red5.io.matroska.VINT;

/**
 * http://matroska.org/technical/specs/index.html
 * 
 * String tag is class able to store strings
 * 
 */
public class StringTag extends Tag {
	private String value = "";
	
	/**
	 * Constructor
	 * 
	 * @see Tag#Tag(String, VINT)
	 */
	public StringTag(String name, VINT id) {
		super(name, id);
	}
	
	/**
	 * Constructor
	 * 
	 * @see Tag#Tag(String, VINT, VINT)
	 */
	public StringTag(String name, VINT id, VINT size) {
		super(name, id, size);
	}
	
	/**
	 * @see Tag#parse(InputStream)
	 */
	@Override
	public void parse(InputStream inputStream) throws IOException {
		value = ParserUtils.parseString(inputStream, (int) getSize());
	}

	/**
	 * @see Tag#putValue(ByteBuffer)
	 */
	@Override
	protected void putValue(ByteBuffer bb) throws IOException {
		bb.put(value.getBytes("UTF-8"));
	}
	
	/**
	 * getter for value
	 * 
	 * @return - value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * setter for value, updates the size of this tag
	 * 
	 * @param value - value to be set
	 * @return - this for chaining
	 */
	public StringTag setValue(String value) throws UnsupportedEncodingException {
		if (value != null) {
			this.value = value;
		}
		byte[] bb = this.value.getBytes("UTF-8");
		size = VINT.fromValue(bb.length);
		return this;
	}

	/**
	 * method to get "pretty" represented {@link Tag}
	 */
	@Override
	public String toString() {
		return (super.toString() + " = " + value);
	}
}

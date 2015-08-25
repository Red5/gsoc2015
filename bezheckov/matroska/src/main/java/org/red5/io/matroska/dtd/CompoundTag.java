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

import static org.red5.io.matroska.ParserUtils.BIT_IN_BYTE;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.red5.io.matroska.ConverterException;
import org.red5.io.matroska.ParserUtils;
import org.red5.io.matroska.VINT;


public class CompoundTag extends Tag {
	private ArrayList<Tag> subElements = new ArrayList<Tag>();
	
	/**
	 * Constructor
	 * 
	 * @see Tag#Tag(String, VINT)
	 *
	 * @param name - the name of tag to be created
	 * @param id - the id of tag to be created
	 */
	public CompoundTag(String name, VINT id) {
		super(name, id);
	}
	
	/**
	 * Constructor
	 * 
	 * @see Tag#Tag(String, VINT, VINT)
	 * 
	 * @param name - the name of tag to be created
	 * @param id - the id of tag to be created
	 * @param size - the size of tag to be created
	 */
	public CompoundTag(String name, VINT id, VINT size) {
		super(name, id, size);
	}
	
	/**
	 * @see Tag#parse(InputStream)
	 */
	@Override
	public void parse(InputStream inputStream) throws IOException, ConverterException {
		subElements = ParserUtils.parseMasterElement(inputStream, (int) getSize());
	}
	
	/**
	 * @see Tag#putValue(ByteBuffer)
	 */
	@Override
	protected void putValue(ByteBuffer bb) throws IOException {
		for (Tag tag : subElements) {
			bb.put(tag.encode());
		}
	}

	/**
	 * getter for type, overriden to return {@link Type#master}
	 */
	@Override
	public Type getType() {
		return Type.master;
	}

	/**
	 * method to add child tag to this {@link CompoundTag}, updates the size on add
	 * 
	 * @param ch - child {@link Tag} to be added
	 * @return - this for chaining
	 */
	public CompoundTag add(Tag ch) {
		subElements.add(ch);
		long sz = getSize() + ch.totalSize();
		byte length = 1;
		long v = (sz + 1) >> BIT_IN_BYTE;
		while (v > 0) {
			length++;
			v = v >> BIT_IN_BYTE;
		}
		size = new VINT(0L, length, sz);
		return this;
	}
	
	/**
	 * method to get "pretty" represented {@link Tag}
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(super.toString() + "\n");
		for (Tag tag : subElements) {
			result.append("\t" + tag + "\n");
		}
		return result.toString();
	}
}

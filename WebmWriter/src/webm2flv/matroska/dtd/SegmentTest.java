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
import java.util.ArrayList;

import webm2flv.ConverterException;
import webm2flv.matroska.ParserUtils;
import webm2flv.matroska.VINT;

public class SegmentTest extends Tag {

	private ArrayList<Tag> subElements = new ArrayList<Tag>();

	public SegmentTest(String name, VINT id) {
		super(name, id);
	}

	
	public SegmentTest(String name, VINT id, VINT size) {
		super(name, id, size);
	}
	
	protected byte[] dataToByteArray() {
		byte[] bytes = new byte[1];
		return bytes;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder(getName() + "\n");
		for (Tag tag : subElements) {
			result.append("    " + tag + "\n");
		}
		return result.toString();
	}

	@Override
	public void parse(InputStream inputStream) throws IOException, ConverterException {
		
		// parse meta seek information
		subElements.add(ParserUtils.parseTag(inputStream));
		
		// parse void
		subElements.add(ParserUtils.parseTag(inputStream));
		
		// parse segment information
		subElements.add(ParserUtils.parseTag(inputStream));
		
		// parse tracks
		subElements.add(ParserUtils.parseTag(inputStream));
		
		// parse tags
		subElements.add(ParserUtils.parseTag(inputStream));
		
		// parse clusters test
		for (int i = 0; i < 14; ++i) {
			subElements.add(ParserUtils.parseTag(inputStream));
		}
	}
	
	public void setDefaultValue(String newValue) {
	}


}

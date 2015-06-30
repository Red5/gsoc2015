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
package org.red5.server.plugin.webm2flv.matroska;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.red5.server.plugin.webm2flv.ConverterException;
import org.red5.server.plugin.webm2flv.matroska.ParserUtils;
import org.red5.server.plugin.webm2flv.matroska.dtd.StringTag;
import org.red5.server.plugin.webm2flv.matroska.dtd.Tag;
import org.red5.server.plugin.webm2flv.matroska.dtd.UnsignedIntegerTag;

import junit.framework.Assert;
import junit.framework.TestCase;

public class MatroskaParserTest extends TestCase {
	
	// size = 1, value = 0x37
	byte[] ebmlTagBytes = {0x1A, 0x45, (byte) 0xdf, (byte) 0xa3, (byte) 0x81, 0x37};
	
	// size = 1, value = 1
	byte[] ebmlVersionTagBytes = {0x42, (byte) 0x86, (byte) 0x81, 0x01};
	
	// size = 1, value = 255
	byte[] ebmlReadVersionTagBytes = {0x42, (byte) 0xf7, (byte) 0x81, (byte) 0xff};
	
	// size = 8, value = "matroska" -> positive scenario
	byte[] ebmlDocTypeTagBytesMatroska = {0x42, (byte) 0x82, (byte) 0x88,
			0x6d, 0x61, 0x74, 0x72, 0x6f, 0x73, 0x6b, 0x61};
	
	// size = 4, value = "webm" -> positive scenario
	byte[] ebmlDocTypeTagBytesWebm = {0x42, (byte) 0x82, (byte) 0x84, 0x77, 0x65, 0x62, 0x6d};
	
	// size = 4, value = "arch" -> negative scenario
	byte[] ebmlDocTypeTagBytesArch = {0x42, (byte) 0x82, (byte) 0x84, 0x61, 0x72, 0x63, 0x68};
	
	public void testParseTagEBML() throws IOException, ConverterException {
		InputStream inputStream = new ByteArrayInputStream(ebmlTagBytes);
		
		Tag tag = ParserUtils.parseTag(inputStream);
		
		// by specification
		Assert.assertEquals("EBML", tag.getName());
		Assert.assertEquals(0x1a45dfa3, tag.getId());
		Assert.assertEquals(1, tag.getSize());
	}
	
	public void testParseTagEBMLVersion() throws IOException, ConverterException {
		InputStream inputStream = new ByteArrayInputStream(ebmlVersionTagBytes);
		
		Tag tag = ParserUtils.parseTag(inputStream);
		
		// by specification
		Assert.assertEquals("EBMLVersion", tag.getName());
		Assert.assertEquals(0x4286, tag.getId());
		Assert.assertEquals(1, tag.getSize());
		tag.parse(inputStream);
		Assert.assertEquals(1, ((UnsignedIntegerTag)tag).getValue());
	}
	
	public void testParseTagEBMLReadVersion() throws IOException, ConverterException {
		InputStream inputStream = new ByteArrayInputStream(ebmlReadVersionTagBytes);
		
		Tag tag = ParserUtils.parseTag(inputStream);
		
		// by specification
		Assert.assertEquals(tag.getName(), "EBMLReadVersion");
		Assert.assertEquals(tag.getId(), 0x42f7);
		Assert.assertEquals(tag.getSize(), 1);
		tag.parse(inputStream);
		Assert.assertEquals(((UnsignedIntegerTag)tag).getValue(), 255);
	}
	
	public void testParseTagDocType() throws IOException, ConverterException {
		InputStream inputStream = new ByteArrayInputStream(ebmlDocTypeTagBytesMatroska);
		Tag tag = ParserUtils.parseTag(inputStream);
		
		Assert.assertEquals(tag.getName(), "DocType");
		Assert.assertEquals(tag.getId(), 0x4282);
		Assert.assertEquals(tag.getSize(), 8);
		tag.parse(inputStream);
		Assert.assertEquals(((StringTag)tag).getValue(), "matroska");
		
		inputStream = new ByteArrayInputStream(ebmlDocTypeTagBytesWebm);
		tag = ParserUtils.parseTag(inputStream);
		Assert.assertEquals(tag.getName(), "DocType");
		Assert.assertEquals(tag.getId(), 0x4282);
		Assert.assertEquals(tag.getSize(), 4);
		tag.parse(inputStream);
		Assert.assertEquals(((StringTag)tag).getValue(), "webm");
		
		inputStream = new ByteArrayInputStream(ebmlDocTypeTagBytesArch);
		tag = ParserUtils.parseTag(inputStream);
		Assert.assertEquals(tag.getName(), "DocType");
		Assert.assertEquals(tag.getId(), 0x4282);
		Assert.assertEquals(tag.getSize(), 4);
		tag.parse(inputStream);
		Assert.assertFalse("webm".equals(((StringTag)tag).getValue()));
		Assert.assertFalse("matroska".equals(((StringTag)tag).getValue()));
	}
}

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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.red5.io.matroska.dtd.CompoundTag;
import org.red5.io.matroska.dtd.Tag;
import org.red5.io.matroska.dtd.TagFactory;

public class EncoderTest {
	
	@Test
	public void testCreateTags() throws IOException, ConverterException {
		InputStream inputStream = new ByteArrayInputStream(ParserTest.ebmlTagBytes);
		Tag ebml1 = ParserUtils.parseTag(inputStream);
		Tag ebml2 = TagFactory.createTag("EBML");
		assertEquals("EBML:: IDs are not equals", ebml1.getId(), ebml2.getId());
		
		inputStream = new ByteArrayInputStream(ParserTest.ebmlVersionTagBytes);
		Tag ebmlV1 = ParserUtils.parseTag(inputStream);
		Tag ebmlV2 = TagFactory.createTag("EBMLVersion");
		assertEquals("EBMLVersion:: IDs are not equals", ebmlV1.getId(), ebmlV2.getId());

		inputStream = new ByteArrayInputStream(ParserTest.trackEntryTagBytes);
		Tag te1 = ParserUtils.parseTag(inputStream);
		Tag te2 = TagFactory.createTag("TrackEntry");
		assertEquals("TrackEntry:: IDs are not equals", te1.getId(), te2.getId());
	}
	
	@Test
	public void testEncodeVINT() throws IOException, ConverterException {
		VINT v1 = new VINT(0L, (byte)1, 1L);
		assertArrayEquals("VINT decoded with errors", ParserTest.vint1Bytes, v1.encode());
		
		VINT v2 = new VINT(0L, (byte)2, 500L);
		assertArrayEquals("VINT decoded with errors", ParserTest.vint2Bytes, v2.encode());
	}

	@Test
	public void testEncodeTagEBML() throws IOException, ConverterException {
		CompoundTag t = TagFactory.<CompoundTag>create("EBML").setValue(0x37);
		
		assertArrayEquals("EBML tag encoded with errors", ParserTest.ebmlTagBytes, t.encode());
	}
}

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
package org.red5.io.webm;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.red5.io.matroska.ConverterException;
import org.red5.io.matroska.dtd.Tag;
import org.red5.io.matroska.dtd.Tag.Type;
import org.red5.io.matroska.parser.TagCrawler;
import org.red5.io.matroska.parser.TagHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebmTest {
	private static Logger log = LoggerFactory.getLogger(WebmTest.class);
	private static final String WEBM_FILE_PROPERTY = "webm.file.path";
	
	@Before
	public void before() {
		assumeTrue(System.getProperties().containsKey(WEBM_FILE_PROPERTY));
	}
	
	@Test
	public void crawl() throws IOException, ConverterException {
		final TagHandler compoundHandler = new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input) throws IOException, ConverterException {
				log.debug("Tag found: " + tag.getName());
				if (tag.getType() != Type.master) {
					long size = tag.getSize();
					while (size > 0) {
						size -= input.skip(size);
					}
				}
			}
		};
		TagCrawler crawler = new TagCrawler() {
			@Override
			public TagHandler getHandler(String name) {
				return compoundHandler;
			}
		};
		File webmF = new File(System.getProperty(WEBM_FILE_PROPERTY));
		if (webmF.exists() && webmF.isFile()) {
			try (FileInputStream fis = new FileInputStream(webmF)) {
				crawler.process(fis);
				assertEquals("Zero bytes should remain in file", 0, fis.available());
			}
		}
	}
	
	@Test
	public void testApp() throws FileNotFoundException {
		File webmF = new File(System.getProperty(WEBM_FILE_PROPERTY));
		//WebM2FLVPlugin.convert(new FileInputStream(webmF), new ByteArrayOutputStream());
		assertTrue("Invalid webM file is specified", webmF.exists() && webmF.isFile());
	}
}

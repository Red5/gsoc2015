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

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.red5.io.matroska.ConverterException;
import org.red5.io.matroska.dtd.Tag;
import org.red5.io.matroska.dtd.Tag.Type;
import org.red5.io.matroska.parser.TagCrawler;
import org.red5.io.matroska.parser.TagHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class able to parse webm Tags from file
 *
 */
public class WebmReader implements Closeable {
	private static Logger log = LoggerFactory.getLogger(WebmReader.class);

	private FileInputStream fis = null;
	private final TagConsumer processor;
	private TagCrawler crawler;

	/**
	 * Constructor
	 * 
	 * @param file - file to be read
	 * @param processor - handler for the tags found
	 * @throws FileNotFoundException - will be thrown if file not found
	 */
	public WebmReader(File file, TagConsumer processor) throws FileNotFoundException {
		fis = new FileInputStream(file);
		this.processor = processor;

		final TagHandler anyHandler = new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input) throws IOException, ConverterException {
				log.debug("Tag found: " + tag.getName());
				if (tag.getType() != Type.master) {
					tag.parse(input);
				}
				WebmReader.this.processor.consume(tag);
			}
		};
		crawler = new TagCrawler() {
			@Override
			public TagHandler getHandler(Tag tag) {
				return anyHandler;
			}
		};
	}

	/**
	 * will process given file and send all tags found to the consumer
	 * 
	 * @throws IOException - in case of any exception during reading and/or consumeng
	 * @throws ConverterException - in case of any error during conversions
	 */
	public void process() throws IOException, ConverterException {
		crawler.process(fis);
	}
	
	/**
	 * Will close all opened resources
	 */
	@Override
	public void close() throws IOException {
		if (fis != null) {
			try {
				fis.close();
				fis = null;
			} catch (Throwable th) {
				//no-op
			}
		}
	}
}

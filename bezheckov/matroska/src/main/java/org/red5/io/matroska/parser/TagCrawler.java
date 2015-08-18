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
package org.red5.io.matroska.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.red5.io.matroska.ConverterException;
import org.red5.io.matroska.ParserUtils;
import org.red5.io.matroska.dtd.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagCrawler {
	private static Logger log = LoggerFactory.getLogger(TagCrawler.class);
	private final Map<String, TagHandler> handlers = new HashMap<>();
	private final TagHandler skipHandler;
	
	public TagCrawler() {
		skipHandler = createSkipHandler();
	}
	
	public TagCrawler addHandler(String name, TagHandler handler) {
		handlers.put(name, handler);
		return this;
	}

	public TagCrawler removeHandler(String name, TagHandler handler) {
		if (handlers.containsKey(name)) {
			handlers.remove(name);
		}
		return this;
	}
	
	public TagHandler getHandler(String name) {
		if (handlers.containsKey(name)) {
			return handlers.get(name);
		}
		return null;
	}
	
	public TagHandler createSkipHandler() {
		return new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input) throws IOException, ConverterException {
				log.debug("Going to skip tag: " + tag.getName());
				long size = tag.getSize();
				while (size > 0) {
					size -= input.skip(size);
				}
			}
		};
	}
	
	public void process(InputStream input) throws IOException, ConverterException {
		while (0 != input.available()) {
			Tag tag = ParserUtils.parseTag(input);
			TagHandler handler = getHandler(tag.getName());
			if (null == handler) {
				skipHandler.handle(tag, input);
			} else {
				handler.handle(tag, input);
			}
		}
	}
}

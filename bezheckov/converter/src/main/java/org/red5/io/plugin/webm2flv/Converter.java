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
package org.red5.io.plugin.webm2flv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.red5.io.plugin.webm2flv.flv.FLVWriter;
import org.red5.io.plugin.webm2flv.flv.TagHandler;
import org.red5.io.plugin.webm2flv.matroska.ParserUtils;
import org.red5.io.plugin.webm2flv.matroska.dtd.StringTag;
import org.red5.io.plugin.webm2flv.matroska.dtd.Tag;

public class Converter {
	
	private HashMap<Long, TagHandler> handlers = new HashMap<>();
	
	public Converter() {
		
		handlers.put(0x1a45dfa3L, new TagHandler() {
			@Override
			public void handle(Tag tag, OutputStream output) throws IOException {
				FLVWriter.writeHeader(output);
			}
		});
		
		handlers.put(0x4282L, new TagHandler() {
			@Override
			public void handle(Tag tag, OutputStream output) throws IOException, ConverterException {
				String docType = ((StringTag)tag).getValue();
				if (docType != "matroska") {
					throw new ConverterException("not supported format " + docType);
				}
			}
		});
	}
		
	public void convert(InputStream input, OutputStream output) throws IOException, ConverterException {
		Tag tag = ParserUtils.parseTag(input);
		if (handlers.containsKey(tag.getId())) {
			handlers.get(tag.getId()).handle(tag, output);
		}
		else {
			input.skip(tag.getSize());
		}
	}
}

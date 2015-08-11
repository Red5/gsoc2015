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
package org.red5.server.plugin.webm2flv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.red5.io.matroska.ConverterException;
import org.red5.server.plugin.Red5Plugin;
import org.red5.logging.Red5LoggerFactory;
import org.slf4j.Logger;

public class WebM2FLVPlugin extends Red5Plugin {
	
	private static Logger log = Red5LoggerFactory.getLogger(WebM2FLVPlugin.class, "plugins");
	
	public String getName() {
		return "WebM2FLVPlugin";
	}
	
	public static void convert(InputStream input, OutputStream output) {
		Converter converter = new Converter();
		try {
			converter.convert(input, output);
		} catch (IOException e) {
			log.error("IO exception " + e.getMessage());
		} catch (ConverterException e) {
			log.error("ConverterException " + e.getMessage());
		}
	}
}

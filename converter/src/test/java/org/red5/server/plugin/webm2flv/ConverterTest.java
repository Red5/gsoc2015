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

import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConverterTest {
	private static final String WEBM_INFILE_PROPERTY = "webm.in.file";
	private static final String FLV_OUTFILE_PROPERTY = "flv.out.file";
	
	@Before
	public void before() {
		//assumeTrue(System.getProperties().containsKey(WEBM_INFILE_PROPERTY));
		//assumeTrue(System.getProperties().containsKey(FLV_OUTFILE_PROPERTY));
	}
	
	@Test
	public void testConvert() throws FileNotFoundException {
		File webmF = new File("/home/dbezheckov/Documents/gsoc/sample.mkv");
		File flvF = new File("/home/dbezheckov/Documents/gsoc/example.flv");
		assertTrue("Invalid webM file is specified", webmF.exists() && webmF.isFile());
		WebM2FLVPlugin.convert(new FileInputStream(webmF), new FileOutputStream(flvF));
	}
}

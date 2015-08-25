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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
	private static final String WEBM_FILE_PROPERTY = "webm.file.path";
	
	@Before
	public void before() {
		assumeTrue(System.getProperties().containsKey(WEBM_FILE_PROPERTY));
	}
	
	@Test
	public void testApp() throws FileNotFoundException {
		File webmF = new File(System.getProperty(WEBM_FILE_PROPERTY));
		WebM2FLVPlugin.convert(new FileInputStream(webmF), new ByteArrayOutputStream());
		assertTrue("Invalid webM file is specified", webmF.exists() && webmF.isFile());
	}
}

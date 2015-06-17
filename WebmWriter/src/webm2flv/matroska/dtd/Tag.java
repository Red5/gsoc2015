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
import java.io.OutputStream;

import webm2flv.ConverterException;
import webm2flv.matroska.VINT;


public abstract class Tag {
	
	private String name;
	
	private VINT id;
	
	private VINT size;
	
	public Tag(String name, VINT id, VINT size) {
		this.name = name;
		this.id = id;
		this.size = size;
	}
	
	public abstract void parse(InputStream inputStream) throws IOException, ConverterException;

	public String getName() {
		return name;
	}
	public void writeTag(OutputStream output) throws IOException {
				
	}

	public long getId() {
		return id.getBinary();
	}

	public long getSize() {
		return size.getValue();
	}
}

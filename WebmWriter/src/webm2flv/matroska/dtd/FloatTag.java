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

import webm2flv.matroska.ParserUtils;
import webm2flv.matroska.VINT;


public class FloatTag extends Tag {

	private double value;

	public FloatTag(String name, VINT id) {
		super(name, id);
	}
	
	public FloatTag(String name, VINT id, VINT size) {
		super(name, id, size);
	}

	@Override
	public void parse(InputStream inputStream) throws IOException {
		value = ParserUtils.parseFloat(inputStream, (int) getSize());
	}
	
	protected byte[] dataToByteArray() {
		byte[] bytes = new byte[1];
		return bytes;
	}
	
	public void setDefaultValue(String newValue) {
		setValue(Float.parseFloat(newValue));
	}
	public void setValue(double newValue) {
		value = newValue;
		if(value < Float.MAX_VALUE) {
			size = new VINT(4, (byte)(4), 4);
		} else {
			size = new VINT(8, (byte)(4), 8);
		}
	}

	public double getValue() {
		return value;
	}
	
	public String toString() {
		return (getName() + " = " + value);
	}

}

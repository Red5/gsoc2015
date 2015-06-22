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

import java.io.FileInputStream;
import java.util.Properties;

import webm2flv.ConverterException;
import webm2flv.matroska.VINT;


public class TagFactory {
	
	static Properties propertyies;
	static Properties writerProperties;
	
	static {
		propertyies = new Properties();
		try (FileInputStream input = new FileInputStream("src/resources/matroska_type_definition_config.properties")) {
			propertyies.load(input);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		writerProperties = new Properties();
		try (FileInputStream input = new FileInputStream("src/resources/matroska_type_prototypes.properties")) {
			writerProperties.load(input);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Tag createTag(VINT id, VINT size) throws ConverterException {
		String value = propertyies.getProperty(Long.toHexString(id.getBinary()));
		if (null == value) {
			throw new ConverterException("not supported matroska tag: " + id.getBinary());
		}
		String[] parameters = value.split(",");
		String className = parameters[1];
		String name = parameters[0];
		
		try {
			Class<?> type = Class.forName(TagFactory.class.getPackage().getName() + "." + className);
			return (Tag) type
					.getConstructor(String.class, VINT.class, VINT.class)
					.newInstance(name, id, size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Tag createTag(String tagName) throws ConverterException {
		String value = writerProperties.getProperty(tagName);
		if (null == value) {
			throw new ConverterException("not supported matroska tag: " + tagName);
		}
		String[] parameters = value.split(",");
		String id = parameters[0];
		String className = parameters[1];

		long longId = Long.parseLong(id, 16); 
		VINT typeVint = new VINT(longId, (byte)(id.length() / 2), longId);
		try {
			Class<?> type = Class.forName(TagFactory.class.getPackage().getName() + "." + className);
			System.out.println(TagFactory.class.getPackage().getName() + "." + className);
			Tag newTag = (Tag) type
					.getConstructor(String.class, VINT.class)
					.newInstance(tagName, typeVint);
			return newTag;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	public static byte[] hexStringToByteArray(String hex) {
	    int len = hex.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
	                             + Character.digit(hex.charAt(i+1), 16));
	    }
	    return data;
	}
	
}

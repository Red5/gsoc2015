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
package org.red5.io.matroska.dtd;

import java.io.InputStream;
import java.util.Properties;

import org.red5.io.matroska.ConverterException;
import org.red5.io.matroska.VINT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * factory for creating matroska tags,
 * it use property file - matroska_type_definition_config.properties
 * with structure:
 * long id = "name provided specification","java class representing tag data"
 */
public class TagFactory {
	static Logger log = LoggerFactory.getLogger(TagFactory.class);
	static final Properties tagsById;
	static final Properties tagsByName;
	
	static {
		tagsById = new Properties();
		try (InputStream input = TagFactory.class.getResourceAsStream("matroska_type_by_id_definition.properties")) {
			tagsById.load(input);
		} catch (Exception e) {
			log.error("Unexpected exception while reading properties", e);
		}
		tagsByName = new Properties();
		try (InputStream input = TagFactory.class.getResourceAsStream("matroska_type_by_name_definition.properties")) {
			tagsByName.load(input);
		} catch (Exception e) {
			log.error("Unexpected exception while reading properties", e);
		}
	}
	
	public static Tag createTag(VINT id, VINT size) throws ConverterException {
		String value = tagsById.getProperty(Long.toHexString(id.getBinary()));
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
			log.error("Unexpected exception while creating tag", e);
		}
		
		return null;
	}
	
	public static Tag createTag(String tagName) throws ConverterException {
		log.debug("Tag: " + tagName);
		String value = tagsByName.getProperty(tagName);
		if (null == value) {
			throw new ConverterException("not supported matroska tag: " + tagName);
		}
		String[] parameters = value.split(",");
		String id = parameters[0];
		String className = parameters[1];

		long longId = Long.parseLong(id, 16); 
		VINT typeVint = new VINT(longId);
		try {
			log.debug("Class name: " + TagFactory.class.getPackage().getName() + "." + className);
			Class<?> type = Class.forName(TagFactory.class.getPackage().getName() + "." + className);
			log.debug(TagFactory.class.getPackage().getName() + "." + className);
			Tag newTag = (Tag) type
					.getConstructor(String.class, VINT.class)
					.newInstance(tagName, typeVint);
			return newTag;
		} catch (Exception e) {
			log.error("Can not find property", e);
		}
		
		return null;
	}
}

package com.gsoc.dbezheckov.matroska.dtd;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.gsoc.dbezheckov.matroska.VINT;

public class TagFactory {
	
	static Properties propertyies;
	
	static {
		propertyies = new Properties();
		try (FileInputStream input = new FileInputStream("src/main/resources/matroska_type_definition_config.properties")) {
			propertyies.load(input);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Tag createTag(VINT id, VINT size, InputStream inputStream) {
		String[] parameters = propertyies.getProperty(Long.toHexString(id.getBinary())).split(",");
		String className = parameters[1];
		String name = parameters[0];
		
		try {
			Class<?> type = Class.forName(TagFactory.class.getPackage().getName() + "." + className);
			return (Tag) type
					.getConstructor(String.class, VINT.class, VINT.class, InputStream.class)
					.newInstance(name, id, size, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}

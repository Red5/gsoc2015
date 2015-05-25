package com.gsoc.dbezheckov.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;

import com.gsoc.dbezheckov.matroska.ParserUtils;
import com.gsoc.dbezheckov.matroska.VINT;

public class StringTag extends Tag {
	
	private String value;
	
	public StringTag(String name, VINT id, VINT size, InputStream inputStream) throws IOException {
		super(name, id, size);
		parse(inputStream);
	}
	
	public String getValue() {
		return value;
	}

	@Override
	public void parse(InputStream inputStream) throws IOException {
		value = ParserUtils.parseString(inputStream, (int) getSize());
	}
	
	public String toString() {
		return (getName() + " = " + value);
    }

}

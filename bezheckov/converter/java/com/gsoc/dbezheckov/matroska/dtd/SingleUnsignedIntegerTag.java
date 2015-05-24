package com.gsoc.dbezheckov.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;

import com.gsoc.dbezheckov.matroska.ParserUtils;
import com.gsoc.dbezheckov.matroska.VINT;

public class SingleUnsignedIntegerTag extends Tag {
	
	private long value;
	
	public SingleUnsignedIntegerTag(String name, VINT id, VINT size, InputStream inputStream) throws IOException {
		super(name, id, size);
		parse(inputStream);
	}

	@Override
	public void parse(InputStream inputStream) throws IOException {
		value = ParserUtils.parseInteger(inputStream, (int) getSize());
	}
	
	public long getValue() {
		return value;
	}
	
	public String toString() {
		return (getName() + " = " + value);
    }

}

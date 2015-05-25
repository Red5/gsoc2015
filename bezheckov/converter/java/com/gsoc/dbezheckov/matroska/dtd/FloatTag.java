package com.gsoc.dbezheckov.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;

import com.gsoc.dbezheckov.matroska.ParserUtils;
import com.gsoc.dbezheckov.matroska.VINT;

public class FloatTag extends Tag {

	private double value;
	
	public FloatTag(String name, VINT id, VINT size, InputStream inputStream) throws IOException {
		super(name, id, size);
		parse(inputStream);
	}

	@Override
	public void parse(InputStream inputStream) throws IOException {
		value = ParserUtils.parseFloat(inputStream, (int) getSize());
	}
	
	public double getValue() {
		return value;
	}
	
	public String toString() {
		return (getName() + " = " + value);
    }

}

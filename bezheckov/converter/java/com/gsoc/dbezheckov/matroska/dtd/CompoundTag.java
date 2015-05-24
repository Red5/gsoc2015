package com.gsoc.dbezheckov.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.gsoc.dbezheckov.matroska.ParserUtils;
import com.gsoc.dbezheckov.matroska.VINT;

public class CompoundTag extends Tag {

	private ArrayList<Tag> subElements = new ArrayList<Tag>();
	
	public CompoundTag(String name, VINT id, VINT size, InputStream inputStream) throws IOException {
		super(name, id, size);
		parse(inputStream);
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder(getName() + "\n");
		for (Tag tag : subElements) {
			result.append("    " + tag + "\n");
		}
        return result.toString();
    }

	@Override
	public void parse(InputStream inputStream) throws IOException {
		subElements = ParserUtils.parseMasterElement(inputStream, (int) getSize());
	}

}

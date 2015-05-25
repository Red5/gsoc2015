package com.gsoc.dbezheckov.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.gsoc.dbezheckov.matroska.ParserUtils;
import com.gsoc.dbezheckov.matroska.VINT;

public class SegmentTest extends Tag {

	private ArrayList<Tag> subElements = new ArrayList<Tag>();
	
	public SegmentTest(String name, VINT id, VINT size, InputStream inputStream) throws IOException {
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
		
		// parse meta seek information
		subElements.add(ParserUtils.parseTag(inputStream));
		
		// parse void
		subElements.add(ParserUtils.parseTag(inputStream));
		
		// parse segment information
		subElements.add(ParserUtils.parseTag(inputStream));
		
		// parse tracks
		subElements.add(ParserUtils.parseTag(inputStream));
	}

}

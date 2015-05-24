package com.gsoc.dbezheckov.matroska;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.gsoc.dbezheckov.matroska.dtd.Tag;

public class SimpleMatroskaParser implements MatroskaParser {

	@Override
	public ArrayList<Tag> parse(InputStream inputStream) throws IOException {
		
		ArrayList<Tag> listOfTags = new ArrayList<Tag>();
		
		// parse EBML tag
		listOfTags.add(ParserUtils.parseTag(inputStream));
		
		// parse Segment tag
		listOfTags.add(ParserUtils.parseTag(inputStream));
		
		return listOfTags;
	}

}

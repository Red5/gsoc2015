package com.gsoc.dbezheckov.matroska;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.gsoc.dbezheckov.matroska.dtd.Tag;

public interface MatroskaParser {
	ArrayList<Tag> parse(InputStream inputStream) throws IOException;
}

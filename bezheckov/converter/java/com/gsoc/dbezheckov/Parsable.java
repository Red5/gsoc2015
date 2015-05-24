package com.gsoc.dbezheckov;

import java.io.IOException;
import java.io.InputStream;

public interface Parsable {
	void parse(InputStream inputStream) throws IOException;
}

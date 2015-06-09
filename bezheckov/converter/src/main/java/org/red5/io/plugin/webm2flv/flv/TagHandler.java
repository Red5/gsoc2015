package org.red5.io.plugin.webm2flv.flv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.red5.io.plugin.webm2flv.ConverterException;
import org.red5.io.plugin.webm2flv.matroska.dtd.Tag;

public interface TagHandler {
	void handle(Tag tag, InputStream input, OutputStream output) throws IOException, ConverterException;
}

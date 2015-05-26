package com.gsoc.dbezheckov.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;

import com.gsoc.dbezheckov.matroska.ParserUtils;
import com.gsoc.dbezheckov.matroska.VINT;

public class BinaryTag extends Tag {
	
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	private byte[] value;

	public BinaryTag(String name, VINT id, VINT size, InputStream inputStream) throws IOException {
		super(name, id, size);
		parse(inputStream);
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	@Override
	public void parse(InputStream inputStream) throws IOException {
		value = ParserUtils.parseBinary(inputStream, (int) getSize());
	}

	public byte[] getValue() {
		return value;
	}
	
	public String toString() {
		return (getName() + " = binary " + (int) getSize());
    }

}

package com.gsoc.dbezheckov.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;

import com.gsoc.dbezheckov.matroska.VINT;

public abstract class Tag {
	
	private String name;
	
	private VINT id;
	
	private VINT size;
	
	public Tag(String name, VINT id, VINT size) {
		this.name = name;
		this.id = id;
		this.size = size;
	}
	
	public abstract void parse(InputStream inputStream) throws IOException;

	public String getName() {
		return name;
	}

	public long getId() {
		return id.getBinary();
	}

	public long getSize() {
		return size.getValue();
	}
}

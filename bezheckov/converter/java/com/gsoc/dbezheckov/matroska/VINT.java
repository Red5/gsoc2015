package com.gsoc.dbezheckov.matroska;

public class VINT {
	private long binaryValue;
	
	private byte length;
	
	private long value;
	
	public VINT(long binaryValue, byte length, long value) {
		this.binaryValue = binaryValue;
		this.length = length;
		this.value = value;
	}
	
	public byte getLength() {
		return length;
	}
	
	public long getBinary() {
		return binaryValue;
	}
	
	public long getValue() {
		return value;
	}
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package webmTags;

import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import webm2flv.ConverterException;
import webm2flv.matroska.VINT;


public abstract class Tag {
	
	private String name;
	
	protected VINT id;
	
	protected VINT size;
	
	public Tag() {
	}
	
	public Tag(String name, VINT id) {
		this.name = name;
		this.id = id;
	}
	
	public Tag(String name, VINT id, VINT size) {
		this.name = name;
		this.id = id;
		this.size = size;
	}
	
	
	public abstract void parse(InputStream inputStream) throws IOException, ConverterException;
	
	public abstract void setDefaultValue(String newValue);
	  
	protected abstract byte[] dataToByteArray() throws IOException;

	public String getName() {
		return name;
	}
	public void writeTag(OutputStream output) throws IOException {
				
	}
	
	public static byte[] makeEbmlCodedSize(final long size, int minSizeLen)
	{
	    final int len = binaryCodedSize(size, minSizeLen);
	    final byte[] ret = new byte[len];
	    long mask = 0x00000000000000FFL;
	    for (int i = 0; i < len; i++)
	    {
	        ret[len - 1 - i] = (byte) ((size & mask) >>> (i * 8));
	        mask <<= 8;
	    }
	    ret[0] |= 0x80 >> (len - 1);
	    return ret;
	}
	
    public ByteBuffer writeHeaderData() throws IOException
	{
	    int len = 0;

	    len += id.getLength();

	    final byte[] encodedSize = makeEbmlCodedSize(getSize(), 0);
	    
	    len += encodedSize.length;
	    len += getSize();
	    final ByteBuffer buf = ByteBuffer.allocate(len);
	    System.out.println("Id:" + id.getValue() + "Idl:" + id.getLength() + ", Length:" + len + "GetSize():" + getSize());
	    buf.put(convertToByteArray(id.getValue(), (int) id.getLength()));
	    buf.put(encodedSize);
	    buf.put(dataToByteArray());
	    buf.flip();
	    return buf;
	}
    
	protected byte[] convertToByteArray(long value, int size) {
		byte[] bytes = new byte[size];
		long tempValue = value;
		for (int i = 0; i < size; ++i) {
			bytes[i] = (byte) (tempValue >> (size - i - 1 << 3));
		}
		return bytes;
	}

    
	public static int binaryCodedSize(final long value, int minSizeLen)
	{
	    int octetsNumber = 0;
	    if (value < 127)
	    {
	    	octetsNumber = 1;
	    }
	    else if (value < 16383)
	    {
	    	octetsNumber = 2;
	    }
	    else if (value < 2097151)
	    {
	    	octetsNumber = 3;
	    }
	    else if (value < 268435455)
	    {
	    	octetsNumber = 4;
	    }
	    if ((minSizeLen > 0) && (octetsNumber <= minSizeLen))
	    {
	    	octetsNumber = minSizeLen;
	    }

	    return octetsNumber;
	}
	  
	  
	public long getId() {
		return id.getBinary();
	}

	public long getSize() {
		return size.getValue();
	}
}

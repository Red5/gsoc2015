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
package webm2flv.matroska;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import webm2flv.ConverterException;
import webmTags.Tag;
import webmTags.TagFactory;


public class ParserUtils {
	
	/**
	 * it is used for : int, uint and date
	 */
	public static long parseInteger(InputStream inputStream, final int size) throws IOException {
		byte[] buffer = new byte[size];
		int numberOfReadsBytes = inputStream.read(buffer, 0, size);
		assert numberOfReadsBytes == size;
		
		long value = buffer[0] & (long)0xff;
		for (int i = 1; i < size; ++i) {
			value = (value << BIT_IN_BYTE) | ((long)buffer[i] & (long)0xff);
		}
		
		return value;
	}
	
	public static String parseString(InputStream inputStream, final int size) throws IOException {
		if (0 == size) {
			return "";
		}
		
		byte[] buffer = new byte[size];
		int numberOfReadsBytes = inputStream.read(buffer, 0, size);
		assert numberOfReadsBytes == size;
		
		return new String(buffer, "UTF-8");
	}
	
	public static double parseFloat(InputStream inputStream, final int size) throws IOException {
		byte[] buffer = new byte[size];
		int numberOfReadsBytes = inputStream.read(buffer, 0, size);
		assert numberOfReadsBytes == size;
		
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer, 0, size).order(ByteOrder.BIG_ENDIAN);
		if (8 == size) {
			return byteBuffer.getDouble();
		}
		
		return byteBuffer.getFloat();
	}
	
	public static ArrayList<Tag> parseMasterElement(InputStream inputStream, final int size) throws IOException, ConverterException {
		byte bufferForSubElements[] = new byte[size];
		int readOfBytes = inputStream.read(bufferForSubElements, 0, size);
		assert readOfBytes == size;
		
		ArrayList<Tag> subElements = new ArrayList<Tag>();
		ByteArrayInputStream inputStreamForSubElements = new ByteArrayInputStream(bufferForSubElements);
		while (0 != inputStreamForSubElements.available()) {
			subElements.add(parseTag(inputStreamForSubElements));
		}
		
		return subElements;
	}
	
	public static byte[] parseBinary(InputStream inputStream, final int size) throws IOException {
		byte value[] = new byte[size];
		int numberOfBytesRead = inputStream.read(value, 0, value.length);
		assert numberOfBytesRead ==  value.length;
		return value;
	}
	
	public static int readIntByVINT(InputStream inputStream) throws IOException {
		return (int)readVINT(inputStream).getValue();
	}
	
	private static final int BIT_IN_BYTE = 8;
	
	private static VINT readVINT(InputStream inputStream) throws IOException {
		ArrayList<Byte> lengthBytes = new ArrayList<Byte>();
		byte length = determineLength(inputStream, lengthBytes);
		
		long binaryValue = lengthBytes.get(0) & (long)0xff;
		long value = ((long)0xff >> (length + 1)) & binaryValue;
		for (int i = 1; i < lengthBytes.size(); ++i) {
			binaryValue = (binaryValue << BIT_IN_BYTE) | ((long)lengthBytes.get(i) & (long)0xff);
			value = (value << BIT_IN_BYTE) | ((long)lengthBytes.get(i) & (long)0xff);
		}
		
		for (int i = 0; i < length; ++i) {
			byte nextByte = (byte)inputStream.read();
			binaryValue = (binaryValue << BIT_IN_BYTE) | ((long)nextByte & (long)0xff);
			value = (value << BIT_IN_BYTE) | ((long)nextByte & (long)0xff);
		}
		
		return new VINT(binaryValue, (byte) (length + 1), value);
	}
	
	private static byte determineLength(InputStream inputStream, ArrayList<Byte> lengthBytes) throws IOException {
		byte length = 0;
		
		// search mark set bit
		
		// skip zeroes bytes
		byte tmp = 0;
		while (0 == (tmp = (byte)inputStream.read())) {
			length += BIT_IN_BYTE;
			lengthBytes.add(tmp);
		}
		
		// skip zeroes bits
		int position = BIT_IN_BYTE - 1;
		lengthBytes.add(tmp);
		while (position > 0 && 0 == getBit(tmp, position--)) { ++length; }
		
		return length;
	}
	
	private static byte getBit(byte value, int position) {
		return (byte) ((value >> position) & 1);
	}

	public static Tag parseTag(InputStream inputStream) throws IOException, ConverterException {
		
		VINT id = readVINT(inputStream);
		VINT size = readVINT(inputStream);
		
		return TagFactory.createTag(id, size);
	}
	
}

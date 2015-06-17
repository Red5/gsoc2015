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
package org.red5.io.plugin.webm2flv.flv;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class FLVWriter {
	
	private static final byte[] MAGICS = "FLV".getBytes();
	
	private static final byte VERSION = 0x01;
	
	private static final byte FLAG_MASK = 0x05;
	
	private static final byte[] DATA_OFFSET = {0x00, 0x00, 0x00, 0x09};
	
	private static final byte[] FIRST_PREVIOUS_TAG_SIZE = {0x00, 0x00, 0x00, 0x00};
	
	private static final byte[] ON_META_DATA_HEADER = {
		0x12, 0x00, 0x00, (byte) 0xf9, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 00, 0x02, 0x00, 0x0a, 0x6f,
		0x6e, 0x4d, 0x65, 0x74, 0x61, 0x44, 0x61, 0x74, 0x61, 0x08, 0x00, 0x00, 0x00, 0x0B};
	
	private static final byte[] ON_META_DATA_FOOTER = { 0x00, 0x00, 0x09, 0x00, 0x00, 0x01, 0x04 };
	
	private static final byte DOUBLE_TYPE = 0x00;
	
	private static final byte INT_TYPE = 0x01;
	
	private static final byte AUDIO_TAG_TYPE = 0x08;
	
	private static final int AUDIO_TAG_HEADER_SIZE = 1;
	
	private static final int LINEAR_PCM_TYPE = 0x3f;
	
	private static final int VIDEO_TAG_HEADER_SIZE = 5;
	
	private static final byte VIDEO_TAG_TYPE = 0x09;
	
	private static final int FLV_TAG_HEADER_SIZE = 11;
	
	public static void writeHeader(OutputStream output) throws IOException {
		output.write(MAGICS);
		output.write(VERSION);
		output.write(FLAG_MASK);
		output.write(DATA_OFFSET);
		output.write(FIRST_PREVIOUS_TAG_SIZE);
	}
	
	private static byte[] toByteArray(double value) {
	    byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).putDouble(value);
	    return bytes;
	}
	
	private static byte[] toByteArrayUInt24(int value) {
	    byte[] bytes = new byte[3];
	    bytes[2] = (byte) (value & 0xff);
	    bytes[1] = (byte) ((value & 0xff00) >> 8);
	    bytes[0] = (byte) ((value & 0xff0000) >> 16);
	    return bytes;
	}
	
	private static byte[] toByteArrayUInt32(int value) {
	    byte[] bytes = new byte[4];
	    bytes[3] = (byte) (value & 0xff);
	    bytes[2] = (byte) ((value & 0xff00) >> 8);
	    bytes[1] = (byte) ((value & 0xff0000) >> 16);
	    bytes[0] = (byte) ((value & 0xff000000) >> 24);
	    return bytes;
	}
	
	private static void writeOnMetaDataElement(byte[] elementName, double value, OutputStream output) throws IOException {
		output.write((byte)((elementName.length & 0xff00) >> 8));
		output.write((byte)(elementName.length & 0x00ff));
		output.write(elementName);
		output.write(DOUBLE_TYPE);
		output.write(toByteArray(value));
	}
	
	private static void writeOnMetaDataElement(byte[] elementName, boolean value, OutputStream output) throws IOException {
		output.write((byte)((elementName.length & 0xff00) >> 8));
		output.write((byte)(elementName.length & 0x00ff));
		output.write(elementName);
		output.write(INT_TYPE);
		output.write(value == true ? 1 : 0);
	}
	
	public static void writeOnMetaDataTag(FLVOnMetaData metaData, OutputStream output) throws IOException {
		output.write(ON_META_DATA_HEADER);
		writeOnMetaDataElement("duration".getBytes(), metaData.getDuration(), output);
		writeOnMetaDataElement("width".getBytes(), metaData.getWidth(), output);
		writeOnMetaDataElement("height".getBytes(), metaData.getHeight(), output);
		writeOnMetaDataElement("videodatarate".getBytes(), FLVOnMetaData.VIDEO_DATA_RATE, output);
		writeOnMetaDataElement("framerate".getBytes(), FLVOnMetaData.FRAME_RATE, output);
		writeOnMetaDataElement("videocodecid".getBytes(), FLVOnMetaData.VIDEO_CODEC_ID, output);
		writeOnMetaDataElement("audiodatarate".getBytes(), FLVOnMetaData.AUDIO_DATA_RATE, output);
		writeOnMetaDataElement("audiosamplerate".getBytes(), metaData.getAudioSampleRate(), output);
		writeOnMetaDataElement("audiosamplesize".getBytes(), metaData.getAudioSampleSize(), output);
		writeOnMetaDataElement("stereo".getBytes(), metaData.isStereo(), output);
		writeOnMetaDataElement("audiocodecid".getBytes(), FLVOnMetaData.AUDIO_CODEC_ID, output);
		output.write(ON_META_DATA_FOOTER);
	}
	
	private static void writeTagHeader(byte tagType, int size, int timestamp, OutputStream output) throws IOException {
		output.write(tagType);
		output.write(toByteArrayUInt24(size));
		output.write(toByteArrayUInt24(timestamp));
		output.write(0x00); // timestamp extended
		
		// streamId always three zeros
		output.write(0x00);
		output.write(0x00);
		output.write(0x00);
	}
	
	public static void writeVideoTag(int timestamp, int compositionTime, byte[] data, boolean isKeyFrame, byte packetType, OutputStream output) 
			throws IOException {
		writeTagHeader(VIDEO_TAG_TYPE, data.length + VIDEO_TAG_HEADER_SIZE, timestamp, output);
		
		output.write(((isKeyFrame ? 0x1 : 0x2) << 4) | (byte)FLVOnMetaData.VIDEO_CODEC_ID);
		output.write(packetType);
		output.write(toByteArrayUInt24(compositionTime));
		output.write(data);
		output.write(toByteArrayUInt32(FLV_TAG_HEADER_SIZE + data.length + VIDEO_TAG_HEADER_SIZE));
	}
	
	public static void writeAudioTag(int timestamp, int compositionTime, byte[] data, OutputStream output) 
			throws IOException {
		writeTagHeader(AUDIO_TAG_TYPE, data.length + AUDIO_TAG_HEADER_SIZE, timestamp, output);
		output.write(LINEAR_PCM_TYPE);
		output.write(data);
		output.write(toByteArrayUInt32(FLV_TAG_HEADER_SIZE + data.length + AUDIO_TAG_HEADER_SIZE));
	}
	
}

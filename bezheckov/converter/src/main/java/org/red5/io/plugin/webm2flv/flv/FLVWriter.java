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
	
}

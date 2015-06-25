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
package org.red5.io.plugin.webm2flv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import org.red5.io.plugin.webm2flv.flv.FLVOnMetaData;
import org.red5.io.plugin.webm2flv.flv.FLVWriter;
import org.red5.io.plugin.webm2flv.flv.TagHandler;
import org.red5.io.plugin.webm2flv.matroska.ParserUtils;
import org.red5.io.plugin.webm2flv.matroska.dtd.BinaryTag;
import org.red5.io.plugin.webm2flv.matroska.dtd.FloatTag;
import org.red5.io.plugin.webm2flv.matroska.dtd.SimpleBlock;
import org.red5.io.plugin.webm2flv.matroska.dtd.StringTag;
import org.red5.io.plugin.webm2flv.matroska.dtd.Tag;
import org.red5.io.plugin.webm2flv.matroska.dtd.UnsignedIntegerTag;

public class Converter {
	
	private HashMap<String, TagHandler> handlers = new HashMap<>();
	
	private FLVOnMetaData onMetaData;
	
	private byte[] codecPrivate;
	
	boolean codecPrivateDone = false;
	
	private long clusterTimecode = 0;
	
	private int lastTrackNumber = -1;
	
	private int videoTrackNumber = -1;
	
	private int audioTrackNumber = -1;
	
	private int currentVideoTimestamp = 0;
	
	public Converter() {
		
		TagHandler enterHandler = new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output) {
				// nothing to do
				// this handler is used for compound tags, so as not to skip the sub tags 
			}
		};
		
		handlers.put("DocType", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				String docType = ((StringTag)tag).getValue();
				if (!"matroska".equals(docType) && !"webm".equals(docType)) {
					throw new ConverterException("not supported document type " + docType);
				}
			}
		});
		
		handlers.put("Segment", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output) throws IOException{
				FLVWriter.writeHeader(output);
			}
		});
		
		handlers.put("Info", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output) {
				onMetaData = new FLVOnMetaData();
			}
		});
		
		handlers.put("Duration", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				onMetaData.setDuration(((FloatTag)tag).getValue() / 1000.0); // in seconds
			}
		});
		
		handlers.put("Tracks", enterHandler);
		handlers.put("TrackEntry", enterHandler);
		handlers.put("TrackNumber", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				lastTrackNumber = (int) ((UnsignedIntegerTag)tag).getValue();
			}
		});
		
		handlers.put("Video", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				videoTrackNumber = lastTrackNumber;
			}
		});
		
		handlers.put("PixelWidth", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				onMetaData.setWidth(((UnsignedIntegerTag)tag).getValue());
			}
		});
		
		handlers.put("PixelHeight", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				onMetaData.setHeight(((UnsignedIntegerTag)tag).getValue());
			}
		});
		
		handlers.put("CodecPrivate", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				codecPrivate = ((BinaryTag)tag).getValue();
			}
		});
		
		handlers.put("Audio", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				audioTrackNumber = lastTrackNumber;
			}
		});
		
		handlers.put("SamplingFrequency", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				onMetaData.setAudioSampleRate(((FloatTag)tag).getValue());
			}
		});
		
		handlers.put("BitDepth", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				onMetaData.setAudioSampleSize(((UnsignedIntegerTag)tag).getValue());
				FLVWriter.writeOnMetaDataTag(onMetaData, output);
				FLVWriter.writeVideoTag(0, 0, codecPrivate, true, (byte) 0, output);
			}
		});
		
		handlers.put("Channels", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				onMetaData.setStereo(2 == ((UnsignedIntegerTag)tag).getValue());
			}
		});
		
		handlers.put("CodecID", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				String codecName = ((StringTag)tag).getValue();
				if (!"V_MPEG4/ISO/AVC".equals(codecName) && !"A_PCM/INT/LIT".equals(codecName)) {
					throw new ConverterException("not supported codec " + codecName);
				}
			}
		});
		
		handlers.put("Cluster", enterHandler);
		
		handlers.put("Timecode", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				clusterTimecode = ((UnsignedIntegerTag)tag).getValue();
			}
		});
		
		handlers.put("SimpleBlock", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream input, OutputStream output)
					throws IOException, ConverterException {
				tag.parse(input);
				int trackNumber = ((SimpleBlock)tag).getTrackNumber();
				long timeCode = ((SimpleBlock)tag).getTimeCode();
				byte[] data = ((SimpleBlock)tag).getBinary();
				boolean isKeyFrame = ((SimpleBlock)tag).isKeyFrame();
				if (trackNumber == videoTrackNumber) {
					FLVWriter.writeVideoTag(currentVideoTimestamp, 0, data, isKeyFrame, (byte) 0x1, output);
					currentVideoTimestamp += FLVWriter.VIDEO_TIMESTAMP_STEP;
				}
				else if (trackNumber == audioTrackNumber) {
					FLVWriter.writeAudioTag((int)(clusterTimecode + timeCode), 0, data, output);
				}
			}
		});
		
	}
	
	public void convert(InputStream input, OutputStream output) throws IOException, ConverterException {
		
		// 1. check first tag in input - this _must_ be EBML tag
		Tag tag = ParserUtils.parseTag(input);
		if (!"EBML".equals(tag.getName())) {
			throw new ConverterException("not supported file format, first tag should be EBML");
		}
		
		// 2. read through all tags and gather info to output, like a SAX parser
		while (0 != input.available()) {
			tag = ParserUtils.parseTag(input);
			TagHandler tagHanlder = handlers.get(tag.getName());
			if (null != tagHanlder) {
				tagHanlder.handle(tag, input, output);
			}
			else {
				warrantySkip(input, tag.getSize());
			}
		}
	}
	
	private void warrantySkip(InputStream input, long size) throws IOException {
		while (size > 0) {
			size -= input.skip(size);
		}
	}
}

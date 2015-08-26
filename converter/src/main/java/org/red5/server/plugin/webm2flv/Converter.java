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
package org.red5.server.plugin.webm2flv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.red5.server.plugin.webm2flv.flv.FLVOnMetaData;
import org.red5.server.plugin.webm2flv.flv.FLVWriter;
import org.red5.io.matroska.ConverterException;
import org.red5.io.matroska.ParserUtils;
import org.red5.io.matroska.dtd.BinaryTag;
import org.red5.io.matroska.dtd.FloatTag;
import org.red5.io.matroska.dtd.SimpleBlock;
import org.red5.io.matroska.dtd.StringTag;
import org.red5.io.matroska.dtd.Tag;
import org.red5.io.matroska.dtd.UnsignedIntegerTag;
import org.red5.io.matroska.parser.TagCrawler;
import org.red5.io.matroska.parser.TagHandler;

/**
 * class for convert webm/mkv h264 video and pcm_16_le audio to flv
 * with same video and audio encoding
 */
public class Converter {
	private OutputStream output;
	
	private final TagCrawler crawler;
	
	private FLVOnMetaData onMetaData;
	
	private byte[] codecPrivate;
	
	boolean codecPrivateDone = false;
	
	private long clusterTimecode = 0;
	
	private int lastTrackNumber = -1;
	
	private int videoTrackNumber = -1;
	
	private int audioTrackNumber = -1;
	
	private int currentVideoTimestamp = 0;
	
	public Converter() {
		crawler = new TagCrawler();
		
		crawler.addHandler("DocType", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				String docType = ((StringTag)tag).getValue();
				if (!"matroska".equals(docType) && !"webm".equals(docType)) {
					throw new ConverterException("not supported document type " + docType);
				}
			}
		});
		
		crawler.addHandler("Segment", new TagHandler() {			
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				FLVWriter.writeHeader(output);
			}
		});
		
		crawler.addHandler("Info", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				onMetaData = new FLVOnMetaData();
			}
		});
		
		crawler.addHandler("Duration", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				onMetaData.setDuration(((FloatTag)tag).getValue() / 1000.0); // in seconds
			}
		});
		
		TagHandler enterHandler = new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				// nothing to do
				// this handler is used for compound tags, so as not to skip the sub tags
			}
		};
		
		crawler.addHandler("Tracks", enterHandler);
		crawler.addHandler("TrackEntry", enterHandler);
		
		crawler.addHandler("TrackNumber", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				lastTrackNumber = (int) ((UnsignedIntegerTag)tag).getValue();
			}
		});
		
		crawler.addHandler("Video", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				videoTrackNumber = lastTrackNumber;
			}
		});
		
		crawler.addHandler("PixelWidth", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				onMetaData.setWidth(((UnsignedIntegerTag)tag).getValue());
			}
		});
		
		crawler.addHandler("PixelHeight", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				onMetaData.setHeight(((UnsignedIntegerTag)tag).getValue());
			}
		});
		
		crawler.addHandler("CodecPrivate", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				codecPrivate = ((BinaryTag)tag).getValue();
			}
		});
		
		crawler.addHandler("Audio", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				audioTrackNumber = lastTrackNumber;
			}
		});
		
		crawler.addHandler("SamplingFrequency", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				onMetaData.setAudioSampleRate(((FloatTag)tag).getValue()); 
			}
		});
		
		crawler.addHandler("BitDepth", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				onMetaData.setAudioSampleSize(((UnsignedIntegerTag)tag).getValue());
				FLVWriter.writeOnMetaDataTag(onMetaData, output);
				FLVWriter.writeVideoTag(0, 0, codecPrivate, true, (byte) 0, output);
			}
		});
		
		crawler.addHandler("Channels", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				onMetaData.setStereo(2 == ((UnsignedIntegerTag)tag).getValue());
			}
		});
		
		crawler.addHandler("CodecID", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				String codecName = ((StringTag)tag).getValue();
				if (!"V_MPEG4/ISO/AVC".equals(codecName) && !"A_PCM/INT/LIT".equals(codecName)) {
					throw new ConverterException("not supported codec " + codecName);
				}
			}
		});
		
		crawler.addHandler("Cluster", enterHandler);
		
		crawler.addHandler("Timecode", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
				clusterTimecode = ((UnsignedIntegerTag)tag).getValue();
			}
		});
		
		crawler.addHandler("SimpleBlock", new TagHandler() {
			@Override
			public void handle(Tag tag, InputStream is) throws IOException, ConverterException {
				tag.parse(is);
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
	
	/**
	 * convert input webm/mkv to flv
	 * work like a Simple API for XML parser (SAX)
	 * 
	 * <pre>
	 * algorithm:
	 * in constructor this class we define handler by tag
	 * then:
	 * 	for (Tag tag : input)
	 * 	{
	 * 		if (tag have handler) -> handle(tag);
	 * 		else skip tag
	 * 	}
	 * </pre>
	 * 
	 * @param input
	 * @param output
	 * @throws IOException
	 * @throws ConverterException
	 */
	public void convert(InputStream input, OutputStream output) throws IOException, ConverterException {
		
		// 1. check first tag in input - this _must_ be EBML tag
		Tag tag = ParserUtils.parseTag(input);
		if (!"EBML".equals(tag.getName())) {
			throw new ConverterException("not supported file format, first tag should be EBML");
		}
		
		// 2. read through all tags and gather info to output, like a SAX parser
		this.output = output;
		crawler.process(input);
	}
}

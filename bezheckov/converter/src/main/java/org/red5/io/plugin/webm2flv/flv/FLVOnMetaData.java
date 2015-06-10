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

public class FLVOnMetaData {
	
	public static final double VIDEO_DATA_RATE = 0D;
	
	public static final double FRAME_RATE = 25D;
	
	public static final double VIDEO_CODEC_ID = 7D;
	
	public static final double AUDIO_DATA_RATE = 1378.125D;
	
	public static final double AUDIO_CODEC_ID = 3D;
	
	private double duration;
	
	private double width;
	
	private double height;
	
	private double audioSampleSize;
	
	private double audioSampleRate;
	
	private boolean stereo;

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getAudioSampleSize() {
		return audioSampleSize;
	}

	public void setAudioSampleSize(double audioSampleSize) {
		this.audioSampleSize = audioSampleSize;
	}

	public double getAudioSampleRate() {
		return audioSampleRate;
	}

	public void setAudioSampleRate(double audioSampleRate) {
		this.audioSampleRate = audioSampleRate;
	}

	public boolean isStereo() {
		return stereo;
	}

	public void setStereo(boolean stereo) {
		this.stereo = stereo;
	}
	
}

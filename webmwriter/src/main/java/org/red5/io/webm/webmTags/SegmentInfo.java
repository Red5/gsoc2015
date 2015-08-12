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

package org.red5.io.webm.webmTags;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

import org.red5.io.matroska.ConverterException;
import org.red5.io.matroska.dtd.BinaryTag;
import org.red5.io.matroska.dtd.CompoundTag;
import org.red5.io.matroska.dtd.DateTag;
import org.red5.io.matroska.dtd.FloatTag;
import org.red5.io.matroska.dtd.StringTag;
import org.red5.io.matroska.dtd.TagFactory;
import org.red5.io.matroska.dtd.UnsignedIntegerTag;


public class SegmentInfo {
/*
	  private static final long BLOCK_SIZE = 128;

	  private Double duration;
	  private long timecodeScale = 1000000;
	  private Date segmentDate = new Date();

	  public SegmentInfo()
	  {
	  }
	  
	  public byte[] writeElement() throws IOException, ConverterException
	  {
		  long bufferSize = 0; 
		  final CompoundTag segmentInfoElem = (SegmentElement) TagFactory.createTag("Segment");
		  byte[] segmentData = segmentInfoElem.writeHeaderData().array();
		  bufferSize += segmentData.length;

		  final StringTag writingAppElem = (StringTag) TagFactory.createTag("WritingApp");
		  writingAppElem.setValue("Webm Writer");
		  byte[] writingAppElemData = writingAppElem.writeHeaderData().array();
		  bufferSize += writingAppElemData.length;
  
		  final StringTag muxingAppElem = (StringTag) TagFactory.createTag("MuxingApp");
		  muxingAppElem.setValue("Webm Writer");
		  byte[] muxingAppElemData = muxingAppElem.writeHeaderData().array();
		  bufferSize += muxingAppElemData.length;
		  
		  final DateTag dateElem = (DateTag) TagFactory.createTag("DateUTC");
		  dateElem.setValue(segmentDate);
		  byte[] dateElemData = dateElem.writeHeaderData().array();
		  bufferSize += dateElemData.length;

		  final UnsignedIntegerTag timecodescaleElem = (UnsignedIntegerTag) TagFactory.createTag("TimecodeScale");
		  timecodescaleElem.setValue(timecodeScale);
		  byte[] timecodescaleElemData = timecodescaleElem.writeHeaderData().array();
		  bufferSize += timecodescaleElemData.length;

		  byte[] durationElemData = null;
		  if (duration != null)
		  {
			  final FloatTag durationElem = (FloatTag) TagFactory.createTag("Duration");
			  durationElem.setValue(duration);
			  durationElemData = durationElem.writeHeaderData().array();
			  bufferSize += durationElemData.length;
		  }

		  final BinaryTag spacer = (BinaryTag) TagFactory.createTag("Void");
		  spacer.setValue(new byte[(int)(BLOCK_SIZE - bufferSize)]);
		  final byte[] spacerData = spacer.writeHeaderData().array();
		  bufferSize += spacerData.length;
		  
		  ByteBuffer resBuffer = ByteBuffer.allocate((int)bufferSize);
		  resBuffer.put(segmentData);
		  resBuffer.put(writingAppElemData);
		  resBuffer.put(muxingAppElemData);
		  resBuffer.put(dateElemData);
		  resBuffer.put(timecodescaleElemData);
		  if(durationElemData != null) {
			  resBuffer.put(durationElemData);
		  }
		  resBuffer.put(spacerData);
		  resBuffer.flip();  
		  
		  return resBuffer.array();
	  }


	  public double getDuration()
	  {
	    return duration;
	  }

	  public void setDuration(final double duration)
	  {
	    this.duration = duration;
	  }

	  public Date getDate()
	  {
	    return segmentDate;
	  }

	  public void setDate(final Date date)
	  {
	    this.segmentDate = date;
	  }

	  public long getTimecodeScale()
	  {
	    return timecodeScale;
	  }

	  public void setTimecodeScale(final long timecodeScale)
	  {
	    this.timecodeScale = timecodeScale;
	  }	
	
	*/
}

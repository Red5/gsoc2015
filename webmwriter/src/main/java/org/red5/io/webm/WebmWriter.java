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

package org.red5.io.webm;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.red5.io.matroska.dtd.Tag;
import org.red5.io.matroska.dtd.TagFactory;
import org.red5.io.matroska.dtd.UnsignedIntegerTag;
import org.red5.io.matroska.dtd.StringTag;

public class WebmWriter {

	private static Logger log = LoggerFactory.getLogger(WebmWriter.class);

	private boolean append;

	private RandomAccessFile dataFile;

	private RandomAccessFile file;

	private volatile long bytesWritten;

	private String filePath;

	public WebmWriter(File file, boolean append) {
		filePath = file.getAbsolutePath();
		try {
			this.append = append;
			if (append) {
				// grab the file we will append to
				this.dataFile = new RandomAccessFile(file, "rws");
				if (!file.exists() || !file.canRead() || !file.canWrite()) {
					log.warn("File does not exist or cannot be accessed");
				} else {
					log.trace("File size: {} last modified: {}", file.length(), file.lastModified());
					// update the bytes written so we write to the correct starting position
					bytesWritten = file.length();
				}
			} else {
				// temporary data file for storage of stream data
				File dat = new File(filePath + ".ser");
				if (dat.exists()) {
					dat.delete();
					dat.createNewFile();
				}
				this.dataFile = new RandomAccessFile(dat, "rws");
				// the final version of the file will go here
				this.file = new RandomAccessFile(file, "rws");
			}
		} catch (Exception e) {
			log.error("Failed to create FLV writer", e);
		}

	}

	public WebmWriter(Object outputFile, boolean append2) {
		// TODO Auto-generated constructor stub
	}

	public void writeHeader() throws IOException {
		try {
			UnsignedIntegerTag ebmlVersion = (UnsignedIntegerTag) TagFactory.createTag("EBMLVersion");
			ebmlVersion.setValue(1);
			file.write(ebmlVersion.toData().array());

			UnsignedIntegerTag ebmlReadVersion = (UnsignedIntegerTag) TagFactory.createTag("EBMLReadVersion");
			ebmlReadVersion.setValue(1);
			file.write(ebmlReadVersion.toData().array());

			UnsignedIntegerTag ebmlMaxIDLength = (UnsignedIntegerTag) TagFactory.createTag("EBMLMaxIDLength");
			ebmlMaxIDLength.setValue(4);
			file.write(ebmlMaxIDLength.toData().array());

			UnsignedIntegerTag ebmlMaxSizeLength = (UnsignedIntegerTag) TagFactory.createTag("EBMLMaxSizeLength");
			ebmlMaxSizeLength.setValue(8);
			file.write(ebmlMaxSizeLength.toData().array());

			StringTag docTypeTag = (StringTag) TagFactory.createTag("DocType");
			byte[] bytes = { (byte) 0x77, (byte) 0x65, (byte) 0x62, (byte) 0x6D };
			docTypeTag.setValue(new String(bytes, "UTF-8"));
			file.write(docTypeTag.toData().array());

			UnsignedIntegerTag docTypeVersion = (UnsignedIntegerTag) TagFactory.createTag("DocTypeVersion");
			docTypeVersion.setValue(3);
			file.write(docTypeVersion.toData().array());

			UnsignedIntegerTag docTypeReadVersion = (UnsignedIntegerTag) TagFactory.createTag("DocTypeReadVersion");
			docTypeReadVersion.setValue(2);
			file.write(docTypeReadVersion.toData().array());
		} catch (Exception e) {
			log.error("Failed to create FLV writer", e);
		}
	}

	public void writeTag(Tag tag) throws IOException {
		file.write(tag.toData().array());
	}

}

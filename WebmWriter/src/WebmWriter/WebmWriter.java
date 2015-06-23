package WebmWriter;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import webmTags.TagFactory;
import webmTags.UnsignedIntegerTag;

public class WebmWriter {
	
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
					/*log.warn("File does not exist or cannot be accessed");*/
				} else {
					//log.trace("File size: {} last modified: {}", file.length(), file.lastModified());
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
			//log.error("Failed to create FLV writer", e);
		}

	}
	
	public void writeHeader() throws IOException {
		try {
			UnsignedIntegerTag ebmlVersion = (UnsignedIntegerTag) TagFactory.createTag("EBMLVersion");
			ebmlVersion.setValue(1);
			file.write(ebmlVersion.writeHeaderData().array());
			
			UnsignedIntegerTag ebmlReadVersion = (UnsignedIntegerTag) TagFactory.createTag("EBMLReadVersion");
			ebmlReadVersion.setValue(1);
			file.write(ebmlReadVersion.writeHeaderData().array());
			
			UnsignedIntegerTag ebmlMaxIDLength = (UnsignedIntegerTag) TagFactory.createTag("EBMLMaxIDLength");
			ebmlMaxIDLength.setValue(4);
			file.write(ebmlMaxIDLength.writeHeaderData().array());
			
			UnsignedIntegerTag ebmlMaxSizeLength = (UnsignedIntegerTag) TagFactory.createTag("EBMLMaxSizeLength");
			ebmlMaxSizeLength.setValue(8);
			file.write(ebmlMaxSizeLength.writeHeaderData().array());
			
			UnsignedIntegerTag docTypeVersion = (UnsignedIntegerTag) TagFactory.createTag("DocTypeVersion");
			docTypeVersion.setValue(3);
			file.write(docTypeVersion.writeHeaderData().array());
			
			UnsignedIntegerTag docTypeReadVersion = (UnsignedIntegerTag) TagFactory.createTag("DocTypeReadVersion");
			docTypeReadVersion.setValue(2);
			file.write(docTypeReadVersion.writeHeaderData().array());
		} catch (Exception e) {
			//log.error("Failed to create FLV writer", e);
		}
	}

	
}

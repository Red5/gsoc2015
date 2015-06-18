package WebmWriter;

import webm2flv.matroska.ParserUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import webm2flv.ConverterException;
import webm2flv.flv.TagHandler;
import webm2flv.matroska.dtd.Tag;

public class WebmWriter {

	// see https://github.com/tungol/EBML/blob/master/doctypes/EBML.dtd
	private static final long EBML_ID = 0x1a45dfa3L;
	private static final long EBML_VERSION_ID = 0x4286L;
	private static final long EBML_READ_VERSION_ID = 0x42f7L;
	private static final long EBML_MAX_ID_LENGTH_ID = 0x42f2L;
	private static final long EBML_MAX_SIZE_LENGTH_ID = 0x42f3L;
	private static final long DOC_TYPE_ID = 0x4287L;
	private static final long DOC_TYPE_VERSION_ID = 0x4287L;
	private static final long DOC_TYPE_READ_VERSION = 0x4285L;
	
	private FileOutputStream output;
	
	private static final int BIT_IN_BYTE = 8;
	
	private Map<Long, ElementType> typeInfo = new HashMap<Long, ElementType>();
	
	private InputStream inputStream;
	
	public WebmWriter(InputStream inputStream) {

		
		Tag tag = ParserUtils.parseTag(input);
		if (!"EBML".equals(tag.getName())) {
			throw new ConverterException("not supported file format, first tag should be EBML");
		}
		
		// 2. read through all tags and gather info to output, like a SAX parser
		while (0 != input.available()) {
			tag = ParserUtils.parseTag(input);
			
		}
		
		this.inputStream = inputStream;
		
		// simple stub for "document type definition - DTD"
		// see https://github.com/tungol/EBML/blob/master/doctypes/EBML.dtd
		typeInfo.put(EBML_ID, ElementType.MasterElement);
		typeInfo.put(EBML_VERSION_ID, ElementType.UnsignedInteger);
		typeInfo.put(EBML_READ_VERSION_ID, ElementType.UnsignedInteger);
		typeInfo.put(EBML_MAX_ID_LENGTH_ID, ElementType.UnsignedInteger);
		typeInfo.put(EBML_MAX_SIZE_LENGTH_ID, ElementType.UnsignedInteger);
		
		typeInfo.put(DOC_TYPE_ID, ElementType.UnsignedInteger);
		typeInfo.put(DOC_TYPE_VERSION_ID, ElementType.UnsignedInteger);
		typeInfo.put(DOC_TYPE_READ_VERSION, ElementType.UnsignedInteger);
	}
	
	public void writeHeader() throws IOException {

	}


	/** {@inheritDoc} */
	public boolean writeStream(byte[] b) {
		try {
			dataFile.write(b);
			return true;
		} catch (IOException e) {
			//log.error("", e);
		}
		return false;
	}

	private void writeMetadataTag(double duration, int videoCodecId, int audioCodecId) throws IOException {

	}

	/** 
	 * Ends the writing process, then merges the data file with the flv file header and metadata.
	 */
	public void close() {
		log.debug("close");
		log.debug("Meta tags: {}", metaTags);
		try {
			lock.acquire();
			if (!append) {
				// write the file header
				writeHeader();
				// write the metadata with the final duration
				writeMetadataTag(duration * 0.001d, videoCodecId, audioCodecId);
				// set the data file the beginning 
				dataFile.seek(0);
				file.getChannel().transferFrom(dataFile.getChannel(), bytesWritten, dataFile.length());
			} else {
				// TODO update duration

			}
		} catch (IOException e) {
			log.error("IO error on close", e);
		} catch (InterruptedException e) {
			log.warn("Exception acquiring lock", e);
		} finally {
			try {
				if (dataFile != null) {
					// close the file
					dataFile.close();
					//TODO delete the data file
					File dat = new File(filePath + ".ser");
					if (dat.exists()) {
						dat.delete();
					}
				}
			} catch (IOException e) {
				log.error("", e);
			}
			try {
				if (file != null) {
					// run a test on the flv if debugging is on
					if (log.isDebugEnabled()) {
						// debugging
						try {
							ITagReader reader = null;
							if (flv != null) {
								reader = flv.getReader();
							}
							if (reader == null) {
								file.seek(0);
								reader = new FLVReader(file.getChannel());
							}
							log.trace("reader: {}", reader);
							log.debug("Has more tags: {}", reader.hasMoreTags());
							ITag tag = null;
							while (reader.hasMoreTags()) {
								tag = reader.readTag();
								log.debug("\n{}", tag);
							}
						} catch (IOException e) {
							log.warn("", e);
						}
					}
					// close the file
					file.close();
				}
			} catch (IOException e) {
				log.error("", e);
			}
			lock.release();
		}
	}

	/** {@inheritDoc}
	 */
	public IStreamableFile getFile() {
		return flv;
	}

	public void setFLV(IFLV flv) {
		this.flv = flv;
	}

	/** 
	 * {@inheritDoc}
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * Setter for offset
	 *
	 * @param offset Value to set for offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/** 
	 * {@inheritDoc}
	 */
	public long getBytesWritten() {
		return bytesWritten;
	}
}

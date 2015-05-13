package bezheckov;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EBMLParser {

	// see https://github.com/tungol/EBML/blob/master/doctypes/EBML.dtd
	private static final long EBML_ID = 0x1a45dfa3L;
	private static final long EBML_VERSION_ID = 0x4286L;
	private static final long EBML_READ_VERSION_ID = 0x42f7L;
	private static final long EBML_MAX_ID_LENGTH_ID = 0x42f2L;
	private static final long EBML_MAX_SIZE_LENGTH_ID = 0x42f3L;
	private static final long DOC_TYPE_ID = 0x4287L;
	private static final long DOC_TYPE_VERSION_ID = 0x4287L;
	private static final long DOC_TYPE_READ_VERSION = 0x4285L;
	
	private static final int BIT_IN_BYTE = 8;
	
	private Map<Long, ElementType> typeInfo = new HashMap<Long, ElementType>();
	
	private InputStream inputStream;
	
	public EBMLParser(InputStream inputStream) {

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
	
	public void parseHeader() {
		try {
			// see layout by specification http://matroska.org/technical/specs/rfc/index.html
			Element ebml = readElement();
			Element ebmlVersion = readElement();
			Element ebmlReadVersion = readElement();
			Element ebmlMaxIdLength = readElement();
			Element ebmlMaxSizeLength = readElement();

			System.out.println("success");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Element readElement() throws IOException {
		VInt elementId = readVInt();
		VInt elementSize = readVInt();
		ElementType type = typeInfo.get(elementId.getValue()); 
		
		// type dependent action with data
		
		return new Element(elementId, elementSize, null, type);
	}
	
	private VInt readVInt() throws IOException {
		ArrayList<Byte> lengthBytes = new ArrayList<Byte>();
		byte length = determineLength(lengthBytes);
		
		long value = lengthBytes.get(0);
		for (int i = 1; i < lengthBytes.size(); ++i) {
			value = (value << BIT_IN_BYTE) | ((long)lengthBytes.get(i) & (long)0xff);
		}
		
		for (int i = 0; i < length; ++i) {
			byte nextByte = (byte)inputStream.read();
			lengthBytes.add(nextByte);
			value = (value << BIT_IN_BYTE) | ((long)nextByte & (long)0xff);
		}
		
		return new VInt(value, length);
	}
	
	private byte determineLength(ArrayList<Byte> lengthBytes) throws IOException {
		byte length = 0;
		
		// search mark set bit
		
		// skip zero bytes
		byte tmp = 0;
		while (0 == (tmp = (byte)inputStream.read())) {
			length += BIT_IN_BYTE;
			lengthBytes.add(tmp);
		}
		
		// skip zero bits
		int position = BIT_IN_BYTE - 1;
		lengthBytes.add(tmp);
		while (position >= 0 && 0 == getBit(tmp, position--)) { ++length; }
		
		return length;
	}
	
	private byte getBit(byte value, int position) {
		return (byte) ((value >> position) & 1);
	}
}

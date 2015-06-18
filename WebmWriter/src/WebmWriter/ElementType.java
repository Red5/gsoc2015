package WebmWriter;

public enum ElementType {
	/**
	 * The signed integer
	 */
	SignedInteger,
	
	/**
	 * The unsigned integer
	 */
	UnsignedInteger,
	
	/**
	 * The floating point number
	 */
	Float,
	
	/**
	 * The character string in the ASCII encoding
	 */
	AsciiString,
	
	/**
	 * The character string in the UTF-8 encoding
	 */
	UTF8String,
	
	/**
	 * The date
	 */
	Date,
	
	/**
	 * The binary data
	 */
	Binary,
	
	/**
	 * Contains other EBML sub-elements of the next lower level
	 */
	MasterElement,
	
	None,
}

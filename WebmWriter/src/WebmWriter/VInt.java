package WebmWriter;

public class VInt {
	
	private long value;
	
	private byte length;
	
	public VInt(long value, byte length) {
		this.value = value;
		this.length = length;
	}
	
	public byte getLength() {
		return length;
	}
	
	public long getValue() {
		return value;
	}
}

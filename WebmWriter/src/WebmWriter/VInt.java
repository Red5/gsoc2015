package WebmWriter;

public class VInt {
	
	private long value = 0;
	
	private byte length = 0;

	public VInt() {
	}
	
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

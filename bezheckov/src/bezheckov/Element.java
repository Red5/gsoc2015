package bezheckov;

public class Element {
	
	private VInt identifier;
	
	private VInt size;
	
	private Object payload;
	
	private ElementType type;
	
	public Element(VInt identifier, VInt size, Object payload, ElementType type) {
		this.identifier = identifier;
		this.size = size;
		this.payload = payload;
		this.type = type;
	}
	
	public VInt getIdentifier() {
		return identifier;
	}
	
	public VInt getSize() {
		return size; 
	}
	
	public Object getValue() {
		return payload;
	}
	
	public ElementType getType() {
		return type;
	}
}

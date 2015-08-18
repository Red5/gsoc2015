package org.red5.io.matroska.dtd;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Date;

import org.red5.io.matroska.ParserUtils;
import org.red5.io.matroska.VINT;

/**
 * http://matroska.org/technical/specs/index.html
 * Date - signed 8 octets integer in nanoseconds with 0 indicating the precise beginning of the millennium (at 2001-01-01T00:00:00,000000000 UTC)
 * 
 *
 */
public class DateTag extends UnsignedIntegerTag {
	public static final long NANO_MULTIPLIER = 1000;
	public static final long DELAY = 978285600000L; // 2001/01/01 00:00:00 UTC

	private Date value;
	
	public DateTag(String name, VINT id) {
		super(name, id);
	}

	public DateTag(String name, VINT id, VINT size) {
		super(name, id, size);
	}

	@Override
	public void parse(InputStream inputStream) throws IOException {
		long _val = ParserUtils.parseInteger(inputStream, (int) getSize());
		long val = _val / NANO_MULTIPLIER + DELAY;
		super.setValue(_val);
		value = new Date(val);
	}
	
	@Override
	protected void putValue(ByteBuffer bb) throws IOException {
		super.putValue(bb);
	}
	
	public DateTag setValue(final Date value) {
		this.value = value;
		super.setValue((value.getTime() - DELAY) * NANO_MULTIPLIER);
		return this;
	}

	public Date getDate() {
		return value;
	}

	@Override
	public String toString() {
		return (super.toString() + " = " + value);
	}
}

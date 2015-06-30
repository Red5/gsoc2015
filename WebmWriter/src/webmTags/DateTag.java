package webmTags;

import webm2flv.matroska.VINT;
import webmTags.UnsignedIntegerTag;

import java.io.IOException;
import java.util.Date;

public class DateTag extends UnsignedIntegerTag {
public static final long UnixEpochDelay = 978307200; // 2001/01/01 00:00:00 UTC

public DateTag(String name, VINT id) throws IOException {
	super(name, id);
}

public DateTag(String name, VINT id, VINT size) throws IOException {
	super(name, id, size);
}

public void setDate(final Date value)
{
  final long val = (value.getTime() - UnixEpochDelay) * 1000000000;
  setValue(val);
}

public Date getDate()
{
  long val = getValue();
  val = val / 1000000000 + UnixEpochDelay;
  return new Date(val);
}

}

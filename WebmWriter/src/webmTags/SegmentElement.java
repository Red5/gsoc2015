package webmTags;


import java.io.IOException;
import java.nio.ByteBuffer;

import webmTags.CompoundTag;

public class SegmentElement extends CompoundTag {
	protected boolean unknownSize = false;
	
	
    public ByteBuffer writeHeaderData() throws IOException
	{
	    int len = 0;

	    len += id.getLength();


	    final byte[] encodedSize;
	    if(unknownSize) {
	    	encodedSize = new byte[5];
	    	encodedSize[0] = (byte) (0xFF >>> (encodedSize.length - 1));
	        for (int i = 1; i < encodedSize.length; i++)
	        {
	        	encodedSize[i] = (byte) 0xFF;
	        }
	    } else {
	    	encodedSize = makeEbmlCodedSize(getSize(), 0);
	    }
	    len += encodedSize.length;
	    
	    len += getSize();
	    final ByteBuffer buf = ByteBuffer.allocate(len);
	    System.out.println("Id:" + id.getValue() + "Idl:" + id.getLength() + ", Length:" + len + "GetSize():" + getSize());
	    buf.put(convertToByteArray(id.getValue(), (int) id.getLength()));
	    buf.put(encodedSize);
	    buf.put(dataToByteArray());
	    buf.flip();

	    return buf;
	}	
    
    public void setUnknownSize(final boolean unknownSize)
    {
    	this.unknownSize = unknownSize;
    }
    
    public boolean getUnknownSize()
    {
    	return unknownSize;
    }

}


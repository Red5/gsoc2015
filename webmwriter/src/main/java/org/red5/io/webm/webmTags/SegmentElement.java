/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.red5.io.webm.webmTags;


import java.io.IOException;
import java.nio.ByteBuffer;

import org.red5.io.matroska.dtd.CompoundTag;

public class SegmentElement {
	/*
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
	    buf.put(toByteArray(id.getValue(), (int) id.getLength()));
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
	*/
}


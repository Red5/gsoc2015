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

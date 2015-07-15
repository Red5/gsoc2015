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
package org.red5.server.sctp;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

public class SctpServerChanneOverUDP extends SctpServerChannel {

	protected SctpServerChanneOverUDP(SelectorProvider provider) {
		super(provider);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SctpChannel accept() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SctpServerChannel bind(SocketAddress local, int backlog)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SctpServerChannel bindAddress(InetAddress address)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SocketAddress> getAllLocalAddresses() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void implCloseSelectableChannel() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void implConfigureBlocking(boolean block) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SctpServerChannel unbindAddress(InetAddress address)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> SctpServerChannel setOption(SctpSocketOption<T> name, T value)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SctpSocketOption<?>> supportedOptions() {
		// TODO Auto-generated method stub
		return null;
	}

}

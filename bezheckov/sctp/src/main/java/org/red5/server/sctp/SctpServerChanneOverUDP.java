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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.red5.server.sctp.packet.SctpPacket;

public class SctpServerChanneOverUDP extends SctpServerChannel implements IServerChannelControl {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private static final String MAC_ALGORITHM_NAME = "HmacSHA256";
	
	private static final int BUFFER_SIZE = 2048;
	
	private byte[] buffer = new byte[BUFFER_SIZE];
	
	private DatagramSocket serverSocket;
	
	private HashMap<SocketAddress, SctpChannel> pendingChannels;
	
	private int maxNumberOfPendingChannels;
	
	private Random random = new Random();
	
	private final Mac messageAuthenticationCode;

	protected SctpServerChanneOverUDP(SelectorProvider provider)
			throws NoSuchAlgorithmException, InvalidKeyException {
		super(provider);
		SecretKeySpec secretKey = new SecretKeySpec(UUID.randomUUID().toString().getBytes(), MAC_ALGORITHM_NAME);
		messageAuthenticationCode = Mac.getInstance(MAC_ALGORITHM_NAME);
		messageAuthenticationCode.init(secretKey);
	}

	@Override
	public SctpChannel accept() throws IOException, SctpException {
		logger.setLevel(Level.INFO);
		
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
			serverSocket.receive(receivePacket);
			SctpPacket packet = null;
			try {
				packet = new SctpPacket(buffer, 0, receivePacket.getLength());
			} catch (SctpException e) {
				logger.log(Level.WARNING, e.getMessage());
				continue;
			}
			
			
			SctpChannel channel = pendingChannels.get(receivePacket.getSocketAddress());
			if (channel == null && pendingChannels.size() < maxNumberOfPendingChannels) {
				SocketAddress address = receivePacket.getSocketAddress();
				channel = new SctpChannel(random, address);
				pendingChannels.put(address, channel);
			}
			else if (channel == null && pendingChannels.size() >= maxNumberOfPendingChannels) {
				logger.info("skip association");
				continue;
			}
			packet.apply(channel);
		}
	}

	@Override
	public SctpServerChannel bind(SocketAddress local, int backlog) throws IOException {
		maxNumberOfPendingChannels = backlog + 1;
		pendingChannels = new HashMap<>();
		if (serverSocket == null) {
			serverSocket = new DatagramSocket(local);
		}
		else {
			throw new IOException("already bound");
		}
		return this;
	}
	
	@Override
	public Mac getMac() {
		return messageAuthenticationCode;
	}
	
	@Override
	public void removePendingChannel(SocketAddress address) {
		// TODO Auto-generated method stub
	}

	@Override
	public void add(SctpChannel channel) {
		// TODO Auto-generated method stub
	}

	@Override
	public SctpServerChannel bindAddress(InetAddress address) throws IOException {
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
	public SctpServerChannel unbindAddress(InetAddress address) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> SctpServerChannel setOption(SctpSocketOption<T> name, T value) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<SctpSocketOption<?>> supportedOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}

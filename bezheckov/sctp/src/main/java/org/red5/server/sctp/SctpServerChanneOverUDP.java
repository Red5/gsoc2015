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
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.red5.server.sctp.packet.SctpPacket;
import org.red5.server.sctp.packet.chunks.Init;
import org.red5.server.sctp.packet.chunks.InitAck;

public class SctpServerChanneOverUDP extends SctpServerChannel {
	
	private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private static final int BUFFER_SIZE = 2048;
	
	private byte[] buffer = new byte[BUFFER_SIZE];
	
	private DatagramSocket serverSocket;
	
	private HashMap<Integer, SctpChannel> pendingChannels;
	
	private HashMap<Integer, Integer> verificationTagForChannel = new HashMap<>();
	
	private int maxNumberOfPendingChannels;
	
	private Random random = new Random();

	protected SctpServerChanneOverUDP(SelectorProvider provider) {
		super(provider);
	}

	@Override
	public SctpChannel accept() throws IOException {
		logger.setLevel(Level.INFO);
		
		while (true) {
			
			/*
			 * 1. wait INIT
			 * 2. send INIT_ACK
			 * 3. wait COOKIE_ECHO
			 * 4. send COOKIE_ACK
			 */
			
			DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
			serverSocket.receive(receivePacket);
			byte[] data = new byte[receivePacket.getLength()];
			System.arraycopy(receivePacket.getData(), 0, data, 0, data.length);
			SctpPacket packet = null;
			try {
				packet = new SctpPacket(data);
			} catch (SctpException e) {
				e.printStackTrace();
			}
			
			int tag = packet.getHeader().getVerificationTag();
			SctpChannel channel = pendingChannels.get(tag);
			if (channel != null) {
				handleAssociation(channel, packet, receivePacket);
			}
			else if (tag == 0 && pendingChannels.size() < maxNumberOfPendingChannels) {
				handleAssociation(channel, packet, receivePacket);
			}
			else if (pendingChannels.size() < maxNumberOfPendingChannels) {
				logger.info("skip association");
			}
			else {
				logger.info("bad association" + channel);
			}
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
	
	private void handleAssociation(SctpChannel channel, SctpPacket packet, DatagramPacket receivePacket)
			throws IOException {
		if (channel == null) {
			logger.info("create new association");
			
			Init init = (Init) packet.getChunks().get(0);
			channel = new SctpChannel(null, init.getInitiateTag());
			pendingChannels.put(init.getInitiateTag(), channel);
			int tag = random.nextInt();
			verificationTagForChannel.put(init.getInitiateTag(), tag);
			
			// send INIT_ACK
			int TSN = random.nextInt();
			InitAck initAck = new InitAck(tag, BUFFER_SIZE, (short)1, (short)1, TSN);
			byte[] data = initAck.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,
					receivePacket.getAddress(), receivePacket.getPort());
			serverSocket.send(sendPacket);
			
			return;
		}
		
		// TODO handle other packet
		
		return;
	}
}

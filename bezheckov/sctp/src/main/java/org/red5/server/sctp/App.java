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
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class App {
	
	private static final int SERVER_PORT = 65125;
	
	private static final int CLIENT_PORT = 6050;
	
    public static void main(String[] args) throws IOException, SctpException, InvalidKeyException, NoSuchAlgorithmException {
    	
    	if (args.length == 2) {
    		System.out.println("usage java -jar " + args[0] + " server/client");
    	}
    	
    	if ("server".equals(args[1])) {
        	// example server
        	SocketAddress serverSocketAddress = new InetSocketAddress(SERVER_PORT);
        	System.out.println("create and bind for sctp address");
        	SctpServerChannel sctpServerChannel = SctpServerChannel.open().bind(serverSocketAddress); 
        	System.out.println("address bind process finished successfully");
        	
        	SctpChannel sctpChannel = null;
        	while ((sctpChannel = sctpServerChannel.accept()) != null) {
        		System.out.println("client connection received");
            }
    	}
    	else if ("client".equals(args[1])) {
    		// example client
    		SocketAddress socketAddress = new InetSocketAddress(SERVER_PORT);
            SctpChannel sctpChannel = SctpChannel.open();
            sctpChannel.bind(new InetSocketAddress(CLIENT_PORT));
            sctpChannel.connect(socketAddress);
        }
    	else {
    		System.out.println("usage java -jar " + args[0] + " server/client");
    	}
    }
}
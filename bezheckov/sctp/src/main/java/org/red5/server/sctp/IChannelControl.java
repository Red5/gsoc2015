package org.red5.server.sctp;

import java.io.IOException;

import org.red5.server.sctp.packet.SctpPacket;

public interface IChannelControl {
	
	public static enum State {
		CLOSED,
		COOKIE_WAIT,
		COOKIE_ECHOED,
		ESTABLISHED
	}
	
	State getState();
	
	void setState(State state);
	
	void sendPacket(SctpPacket packet) throws IOException;
	
	int getVerificationTag();
}

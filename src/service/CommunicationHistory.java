package service;

import java.util.Date;

public class CommunicationHistory 
{
	public final Message msg;
	public final MessageReciever recv;
	public final int type;
	public final String personID;
	public final Date timeSent;
	
	public final static int HISTORY_TYPE_AUTO = 0;
	public final static int HISTORY_TYPE_MANUAL = 1;
	
	public CommunicationHistory(Message msg, MessageReciever recv, int type, String personID)
	{
		this.personID = personID;
		this.type = type;
		this.msg = msg;
		this.recv = recv;
		this.timeSent = new Date();
	}
}

package service.communication;

import java.util.Date;
import java.util.UUID;


public class CommunicationHistory 
{
	public final Message msg;
	public final MessageReciever recv;
	public final int type;
	public final String personID;
	public final Date timeSent;
	public final int status;
	public final UUID uuid;
	
	public final static int HISTORY_TYPE_AUTO = 0;
	public final static int HISTORY_TYPE_MANUAL = 1;
	
	public CommunicationHistory(Message msg, MessageReciever recv, int type, String personID, int status)
	{
		this.uuid = UUID.randomUUID();
		this.status = status;
		this.personID = personID;
		this.type = type;
		this.msg = msg;
		this.recv = recv;
		this.timeSent = new Date();
	}
}

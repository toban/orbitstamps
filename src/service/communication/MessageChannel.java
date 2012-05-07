package service.communication;

public interface MessageChannel 
{
	
	public static final int MSG_STATUS_FAILED = 0;
	public static final int MSG_STATUS_SENT = 1;
	public static final int MSG_STATUS_NO_COMPATIBLE_DEVICE = 2;
	public static final int MSG_STATUS_RECIEVED = 3;
	public int sendMessage(Message msg, MessageReciever recv);
	public boolean isAvaliable();
	public int getSignal();
	
}

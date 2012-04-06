package service;

public interface MessageChannel 
{
	public boolean sendMessage(Message msg, MessageReciever recv);
	public boolean isAvaliable();
	public int getSignal();
	
}

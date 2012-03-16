package service;

public interface MessageChannel 
{
	public boolean sendMessage(Message msg);
	public boolean isAvaliable();
	
}

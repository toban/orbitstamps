package service;

public abstract class MessageReciever 
{
	public String number;
	public abstract boolean isCompatibleWith(MessageChannel mch);
}

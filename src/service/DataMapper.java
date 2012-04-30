package service;

public abstract class DataMapper 
{
	public abstract void mapToModel(Object obj);
	
	public abstract boolean connect(String username, String userpass, String ip);
	public abstract boolean isConnected();
}

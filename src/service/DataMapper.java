package service;

public abstract class DataMapper 
{
	public abstract void mapToModel(Object obj);
	
	public abstract boolean connect();
	public abstract boolean isConnected();
}

package service;

public abstract class DataMapper 
{
	
	public final static int DATATYPE_ALL = 0;
	public final static int DATATYPE_STAMPS = 1;
	
	public abstract void mapToModel(int which);
	
	public abstract boolean connect();
	public abstract boolean disconnect();
	public abstract boolean isConnected();
}

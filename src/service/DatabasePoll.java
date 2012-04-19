package service;

import java.util.ArrayList;

import service.model.Timestamp;


public class DatabasePoll extends Thread
{
	public String serverIP;
	public String serverUserName;
	public String password;
	public boolean isActive = false;
	public long pollInterval = 1000;
	private DataMapper mapper;
	static public ArrayList<Timestamp> newTimestamps;
	
	public DatabasePoll(String ip, String usr, String pass, DataMapper dm)
	{
		mapper = dm;
		serverIP = ip;
		serverUserName = usr;
		password = pass;
		isActive = true;
	}
    public void run() {
    	
    	while(isActive)
    	{
        	OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Polling database ... ");
        	OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Adding new timestamps to room ... ");
        	OrbitStamps.processRoomTimestamps();
        	
            synchronized (this) 
            {
           		try 
        		{
    				wait(pollInterval);
    			} catch (InterruptedException e) 
    			{
    				e.printStackTrace();
    			}
            }
    	}
    	
    }
	public boolean connect()
	{
		return false;
	}
	public void getTimestamps()
	{
		
	}
}

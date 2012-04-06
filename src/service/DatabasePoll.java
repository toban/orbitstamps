package service;

import java.util.ArrayList;


public class DatabasePoll extends Thread
{
	public String serverIP;
	public String serverUserName;
	public String password;
	public boolean isActive = false;
	public long pollInterval = 1000;
	static public ArrayList<Timestamp> newTimestamps;
	
	public DatabasePoll(String ip, String usr, String pass)
	{
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

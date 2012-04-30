package service;

import java.util.ArrayList;

import service.model.Timestamp;


public class DatabasePoll extends Thread
{

	public boolean isActive = false;
	public long pollInterval = 1000;
	private DataMapper mapper;
	static public ArrayList<Timestamp> newTimestamps;
	
	public DatabasePoll(DataMapper dm)
	{
		mapper = dm;
		isActive = true;
	}
	public boolean debugConnect()
	{
		return mapper.connect();
	}
    public void run() {
    	
    	while(isActive)
    	{
        	//OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Polling database ... ");
        	//OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Adding new timestamps to room ... ");
    		mapper.mapToModel(null);
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
}

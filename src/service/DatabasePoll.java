package service;

import java.util.ArrayList;

import service.model.Timestamp;


public class DatabasePoll extends Thread
{

	public boolean isActive = false;
	public long pollInterval = 20000;
	private DataMapper mapper;
	static public ArrayList<Timestamp> newTimestamps;
	
	public DatabasePoll(DataMapper dm)
	{
		mapper = dm;
		isActive = true;
	}
	public void updateData(int which)
	{
		if(mapper.connect())
		{
			mapper.mapToModel(which);
			mapper.disconnect();
		}
	}
    public void run() {
    	
    	while(isActive)
    	{
        	//OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Polling database ... ");
        	//OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Adding new timestamps to room ... ");
    		//updateData(DataMapper.DATATYPE_STAMPS);
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

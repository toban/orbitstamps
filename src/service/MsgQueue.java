package service;

import java.util.ArrayList;
import java.util.LinkedList;

import service.communication.CommunicationHistory;
import service.model.Timestamp;


public class MsgQueue extends Thread
{

	public boolean isActive = true;
	public long pollInterval = 1000;
	
	static private LinkedList<MsgQueueItem> msgObjects;
	
	public MsgQueue()
	{
		msgObjects = new LinkedList<MsgQueueItem>();
	}
	public void add(MsgQueueItem msgItem)
	{
		msgObjects.push(msgItem);
	}
    public void run() {
    	
    	while(isActive)
    	{
    		if(msgObjects.size() > 0)
    		{
    		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Sending Message ... ");
    		MsgQueueItem item = msgObjects.pop();
    		
    		item.room.messageHistory.addFirst(new CommunicationHistory(item.msg, 
					item.person.devices.iterator().next(),
					item.msgType,
					item.person.ID));

    		item = null;

    		}
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

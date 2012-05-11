package service;

import java.util.ArrayList;
import java.util.LinkedList;

import service.communication.CommunicationHistory;
import service.communication.MessageChannel;
import service.communication.MessageReciever;
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
    		
    		MsgQueueItem item = msgObjects.pop();
    		
    		// parse dynamic elements
    		item.parseBody();
    		int status = MessageChannel.MSG_STATUS_FAILED;
    		
    		for(MessageChannel channel : OrbitStamps.channels)
    		{
    			MessageReciever useDevice = item.person.getDeviceCompatibleWith(channel);
    			
    			if(useDevice == null)
    				status = MessageChannel.MSG_STATUS_NO_COMPATIBLE_DEVICE;
    			else
    				status = 1;//channel.sendMessage(item.msg, useDevice);
    			
    		}
    		
    		// add history
    		item.room.messageHistory.addFirst(new CommunicationHistory(item.msg, 
					item.person.devices.iterator().next(),
					item.msgType,
					item.person.ID, status));
    		
    		
    		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Sending Message ... to= " + item.person.role.getRole() + " name=" + item.person.name + " body= " + item.msg.getBody());
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

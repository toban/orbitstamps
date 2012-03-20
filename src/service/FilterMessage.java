package service;

import java.util.ArrayList;
import java.util.List;

import roles.Role;
import timestamps.Timestamp;

public class FilterMessage 
{
	public ArrayList<Role> roles;
	public Timestamp stamp;
	public int maxDelay;
	public ArrayList<Integer> rooms;
	public Message msg;
	
	public FilterMessage(ArrayList<Role> roles, Timestamp stamp, int maxDelay, ArrayList<Integer> rooms, Message msg)
	{
		this.roles = roles;
		this.stamp = stamp;
		this.maxDelay = maxDelay;
		this.rooms = rooms;
		this.msg = msg;
		
	}
	public void printDebug()
	{
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "FilterMessage: DEBUG PRINT START");
		for(Role r : roles)
		{
			OrbitStamps.log(OrbitStamps.LOG_NOTICE, "role= " + r.roleID);
		}
		for(Integer r : rooms)
		{
			OrbitStamps.log(OrbitStamps.LOG_NOTICE, "room= " + r);
		}
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "stamp= " + stamp.stamp);
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "maxDelay= " + maxDelay);
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "msg.body= " + msg.getBody());
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "msg.signal= " + msg.getSignal());
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "msg.urgency= " + msg.getUrgency());
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "FilterMessage: DEBUG PRINT END");
	}
}

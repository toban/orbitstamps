package service;

import java.util.ArrayList;
import java.util.List;

import timestamps.Timestamp;

public class FilterMessage 
{
	public ArrayList<Role> roles;
	public Timestamp stamp;
	public int maxDelay;
	public ArrayList<String> rooms;
	public Message msg;
	
	public FilterMessage(ArrayList<Role> roles, Timestamp stamp, int maxDelay, ArrayList<String> rooms, Message msg)
	{
		this.roles = roles;
		this.stamp = stamp;
		this.maxDelay = maxDelay;
		this.rooms = rooms;
		this.msg = msg;
		
	}
	// check if the room's timestamp and role matches 
	public boolean match(Room r, Person p)
	{
		if(r.newStamps.contains(stamp))
		{
			if(rooms.contains("all") || rooms.contains(r.roomID))
			{
				for(Role filterRole : roles)
				{
					if(p.role.equals(filterRole))
						return true;
				}
			}
		}
		return false;
	}
	public void printDebug()
	{
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "FilterMessage: DEBUG PRINT START");
		for(Role r : roles)
		{
			OrbitStamps.log(OrbitStamps.LOG_NOTICE, "role= " + r.getRole());
		}
		for(String r : rooms)
		{
			OrbitStamps.log(OrbitStamps.LOG_NOTICE, "room= " + r);
		}
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "stamp= " + stamp.stamp);
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "maxDelay= " + maxDelay);
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "msg.body= " + msg.getBody());
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "msg.signal= " + msg.signal);
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "msg.urgency= " + msg.getUrgency());
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "FilterMessage: DEBUG PRINT END");
	}
}

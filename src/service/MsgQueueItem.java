package service;

import java.util.ArrayList;

import service.communication.Message;
import service.filter.FilterMessage;
import service.model.Person;
import service.model.Room;
import service.model.Timestamp;


public class MsgQueueItem
{
	public Room room;
	public Person person;
	public Message msg;
	public final int msgType;
	
	public MsgQueueItem(Room r, Person p, Message msg, int msgType)
	{
		room = r;
		person = p;
		this.msg = msg;
		this.msgType = msgType;
		
		
	}


}

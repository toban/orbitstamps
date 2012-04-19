package service.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import service.communication.MessageChannel;
import service.communication.MessageReciever;


public class Person
{
	public String name;
	public String ID;
	public Role role;
	public ArrayList<MessageReciever> devices;
	public Date dateAdded;
	
	public Person(String name, Role role, String ID)
	{
		this.ID = ID;
		this.name = name;
		this.role = role;
		this.devices = new ArrayList<MessageReciever>();
		dateAdded = new Date(); 
	}
	
	public MessageReciever getDeviceCompatibleWith(MessageChannel mch)
	{
		for(MessageReciever mr : devices)
		{
			if(mr.isCompatibleWith(mch))
				return mr;
		}
		return null;
	}
	
	public boolean equals(Object obj)
	{
		try
		{
			Person castObj = (Person) obj;
			return ID.equals(castObj.ID);
		}
		catch(Exception e)
		{
			return false;
		}
			
	}
	
	
}

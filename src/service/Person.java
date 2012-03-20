package service;

import java.util.ArrayList;

import roles.Role;

public class Person 
{
	public String name;
	public String ID;
	public Role role;
	public ArrayList<MessageReciever> devices;
	
	public Person(String name, Role role, String ID)
	{
		this.ID = ID;
		this.name = name;
		this.role = role;
		this.devices = new ArrayList<MessageReciever>();
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

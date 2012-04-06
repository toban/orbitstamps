package service;

import java.util.ArrayList;
import java.util.LinkedList;


public class Room 
{
	public String roomID;
	private ArrayList<Person> people;
	public ArrayList<Timestamp> newStamps;
	public LinkedList<CommunicationHistory> messageHistory;
	
	public Room(String roomID)
	{
		this.newStamps = new ArrayList<Timestamp>();
		this.messageHistory = new LinkedList<CommunicationHistory>();
		this.setPeople(new ArrayList<Person>());
		this.roomID = roomID;
	}
	public Person getPerson(String id)
	{
		for(Person p : people)
		{ 
			if(p.ID.equals(id))
				return p;
		}
			return null;
	}
	public boolean addPerson(Person p)
	{
		if(!getPeople().contains(p))
		{
			getPeople().add(p);
			return true;
		}
		else
		{
			OrbitStamps.log(OrbitStamps.LOG_ERROR, "DummyData: person="+p.ID+" already in room="+roomID);
			return false;
		}
	}
	public boolean equals(Object obj)
	{
		try
		{
			Room castObj = (Room) obj;
			return roomID.equals(castObj.roomID);
		}
		catch(Exception e)
		{
			return false;
		}
			
	}
	public final ArrayList<Person> getPeople() {
		return people;
	}
	public void setPeople(ArrayList<Person> people) {
		this.people = people;
	}
}

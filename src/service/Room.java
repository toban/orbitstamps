package service;

import java.util.ArrayList;

import timestamps.Timestamp;

public class Room 
{
	public String roomID;
	private ArrayList<Person> people;
	public ArrayList<Timestamp> newStamps;
	public ArrayList<CommunicationHistory> messageHistory;
	
	public Room(String roomID)
	{
		this.newStamps = new ArrayList<Timestamp>();
		this.messageHistory = new ArrayList<CommunicationHistory>();
		this.setPeople(new ArrayList<Person>());
		this.roomID = roomID;
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

package service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import service.OrbitStamps;
import service.communication.CommunicationHistory;


public class Room 
{
	public String roomID, roomName, locationName, locationID;
	private ArrayList<FunctionalPerson> persistantPersonal;
	public HashMap<String,Operation> operations;
	public LinkedList<CommunicationHistory> messageHistory;
	
	public Room(String roomID)
	{
		this.operations = new HashMap<String, Operation>();
		this.messageHistory = new LinkedList<CommunicationHistory>();
		this.setPersistantPersons(new ArrayList<FunctionalPerson>());
		this.roomID = roomID;
	}
	public Person getPersistantPerson(String id)
	{
		for(Person p : persistantPersonal)
		{ 
			if(p.ID.equals(id))
				return p;
		}
			return null;
	}
	public void deletePersistantPerson(Person p)
	{
			persistantPersonal.remove(p);
	}
	public boolean addPersistantPerson(FunctionalPerson p)
	{
		if(!getPersistantPersons().contains(p))
		{
			getPersistantPersons().add(p);
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
	public final ArrayList<FunctionalPerson> getPersistantPersons() {
		return persistantPersonal;
	}
	public void setPersistantPersons(ArrayList<FunctionalPerson> people) {
		this.persistantPersonal = people;
	}
}

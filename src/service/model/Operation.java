package service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import service.OrbitStamps;

public class Operation 
{
	public final String op_id;
	public final String opkort_id;
	public final String op_desc;
	public final Date op_start, op_end;
	
	public LinkedList<Timestamp> stamps;
	private ArrayList<Person> people;
	
	public Operation( String op_id, String opkort_id, Date op_start, Date op_end, String op_desc)
	{
		this.op_start = op_start;
		this.op_end = op_end;
		
		this.op_desc = op_desc;
		
		stamps = new LinkedList<Timestamp>();
		people = new ArrayList<Person>();
		
		this.opkort_id = opkort_id;
		this.op_id = op_id;
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
	public void deletePerson(Person p)
	{
			people.remove(p);
	}
	public final ArrayList<Person> getPeople() {
		return people;
	}
	public void setPeople(ArrayList<Person> people) {
		this.people = people;
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
			OrbitStamps.log(OrbitStamps.LOG_ERROR, "DummyData: person="+p.ID+" already in operation="+op_id);
			return false;
		}
	}
	public void debugPrint()
	{
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Operation: id = " + opkort_id + " opkort_id = " + opkort_id);
	}
	
}

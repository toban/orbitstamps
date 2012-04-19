package service.model;


public class FunctionalPerson extends Person
{
	public String roomID;
	
	public FunctionalPerson(String name, Role role, String ID, String roomID)
	{
		super(name, role, ID);
		this.roomID = roomID;
	}
	
	
}

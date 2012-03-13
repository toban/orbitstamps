package timestamps;

import java.util.Calendar;

import roles.Role;


public abstract class Timestamp 
{
	public Calendar time;
	public int stamp;
	
	public abstract boolean filter(Role r);
	
}

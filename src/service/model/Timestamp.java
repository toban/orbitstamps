package service.model;

import java.util.Calendar;



public class Timestamp 
{
	public Calendar time;
	public String stamp;
	
	public Timestamp(String s)
	{
		stamp = s;
	}
	public boolean equals(Object obj)
	{
		Timestamp castobj = (Timestamp)obj;
		//System.out.println("comparison");
		return stamp.equals(castobj.stamp);
	}
	
}

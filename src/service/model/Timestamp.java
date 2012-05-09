package service.model;

import java.util.Calendar;
import java.util.Date;



public class Timestamp 
{
	public Date time;
	public String stamp;
	public String stringStamp;
	
	public Timestamp(String s)
	{
		stamp = s;
	}
	public boolean equals(Object obj)
	{
		Timestamp castobj = (Timestamp)obj;
		return stamp.equals(castobj.stamp);
	}
	
}

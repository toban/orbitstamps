package service.model;

import java.util.LinkedList;

import service.OrbitStamps;

public class Operation 
{
	public final String op_id;
	public final String opkort_id;
	
	public LinkedList<Timestamp> stamps;
	
	public Operation( String op_id, String opkort_id)
	{
		this.opkort_id = opkort_id;
		this.op_id = op_id;
	}
	public void debugPrint()
	{
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Operation: id = " + opkort_id + " opkort_id = " + opkort_id);
	}
	
}

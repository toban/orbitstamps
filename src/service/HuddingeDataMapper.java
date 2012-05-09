package service;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;

import service.model.Operation;
import service.model.Room;
import service.model.Timestamp;

public class HuddingeDataMapper extends DataMapper {

	private Connection conn;
	
	@Override
	public void mapToModel(Object obj) 
	{
		
	}
	@Override
	public boolean connect() 
	{

	    try
	    {
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			String url = "jdbc:sqlserver://"+Config.DATABASE_IP+":"+Config.DATABASE_PORT+";databaseName=Narda";
	
			conn = DriverManager.getConnection(url, Config.DATABASE_USERNAME, Config.DATABASE_PASSWORD);
			
			
			
			
			// Get and populate rooms
			Statement stmnt = conn.createStatement();
			String strQuery = "select opsal_id, opsalnamn, platsnamn, plats_id from " + Config.DATABASE_OPERATION_VIEW;
			ResultSet res = stmnt.executeQuery(strQuery);
			
			while (res.next()) 
			{ 
				String roomID = res.getString("opsal_id");
				
				if(!OrbitStamps.operatingRooms.containsKey(roomID))
				{
					Room r = new Room(roomID);
					r.locationID = res.getString("plats_id");
					r.locationName = res.getString("platsnamn");
					r.roomName = res.getString("opsalnamn");
					OrbitStamps.operatingRooms.put(roomID, r);
				}
				getAndPopulateRoomWithOperation(roomID);
			}
			
			
			
			return true;
	    }
	    catch (ClassNotFoundException ex) {System.err.println(ex.getMessage());}
	    catch (IllegalAccessException ex) {System.err.println(ex.getMessage());}
	    catch (InstantiationException ex) {System.err.println(ex.getMessage());}
	    catch (SQLException ex)           {System.err.println(ex.getMessage());}
	 
		return false;
	}
	
	private void getAndPopulateEvents(String roomID)
	{
		// Get and populate event info
		
		Room r = OrbitStamps.operatingRooms.get(roomID);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Entry<String, Operation> opEntry : r.operations.entrySet())
		{
			Operation op = opEntry.getValue();
			Statement stmnt;
			Timestamp lastEvent = op.stamps.peekFirst();
			
			try {
				
				stmnt = conn.createStatement();
				String strQuery;
				
				if(lastEvent==null)
				{
					 strQuery = "select handelse, tidpunkt from " + Config.DATABASE_EVENT_VIEW + " WHERE optillfalle_id = " + op.op_id;
				}
				else
				{
					 strQuery = "select handelse, tidpunkt from " + Config.DATABASE_EVENT_VIEW + " WHERE optillfalle_id = " + op.op_id;
				}
				ResultSet res = stmnt.executeQuery(strQuery);


				while(res.next()) 
				{ 

					
			        try 
			        {
						String handelse = res.getString("handelse");
						String tidpunkt = res.getString("tidpunkt");
						
						Timestamp newStamp = new Timestamp(Integer.toString(OrbitStamps.stampStringToOrder.get(handelse)));
						newStamp.time = df.parse(tidpunkt);
						
						if(!op.stamps.contains(newStamp))
						{
							op.stamps.addFirst(newStamp);
						}
						
			        } catch (ParseException e) 
			        {
			            e.printStackTrace();
			        }
					

				}	
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
	}
	private void getAndPopulateRoomWithOperation(String roomID)
	{
		// Get and populate operatinginfo
		Statement stmnt;
		try {
			stmnt = conn.createStatement();
			String strQuery = "select optillfalle_id, opkort_id, iva, tid_forberedelse, tid_operation from " + Config.DATABASE_OPERATION_VIEW + " WHERE opsal_id = " + roomID;
			ResultSet res = stmnt.executeQuery(strQuery);
			
			while (res.next()) 
			{ 
				Room r  = OrbitStamps.operatingRooms.get(roomID);
				String op_id = res.getString("optillfalle_id");
				
				if(!r.operations.containsKey(op_id))
				{
					String opkort_id = res.getString("opkort_id");
					Operation operation = new Operation(op_id, opkort_id);
					r.operations.put(op_id, operation);
					operation.debugPrint();
				}
			}	
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	
	}
	private void getAndPopulatePersonal()
	{
		
	}

	@Override
	public boolean isConnected() 
	{
		return false;
	}
	@Override
	public boolean disconnect() {
		try 
		{
			conn.close();
			return true;
		}
		catch (SQLException ex)
		{
			System.err.println(ex.getMessage());
			return false;
		}
		
	}

}

package service;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map.Entry;

import service.communication.PagerReciever;
import service.model.Operation;
import service.model.Person;
import service.model.Role;
import service.model.Room;
import service.model.Timestamp;

public class HuddingeDataMapper extends DataMapper {

	private Connection conn;
	
	@Override
	public void mapToModel(int which) 
	{
		switch(which)
		{
		case DataMapper.DATATYPE_ALL:
			getAndPopulateRooms();
			
			for(Entry<String, Room> set : OrbitStamps.operatingRooms.entrySet())
			{
				String roomID = set.getValue().roomID;
				getAndPopulateRoomWithOperation(roomID);
				getAndPopulatePersonal(roomID);
				getAndPopulateEvents(roomID);
			}
			
			
			break;
		case DataMapper.DATATYPE_STAMPS:
			for(Entry<String, Room> set : OrbitStamps.operatingRooms.entrySet())
			{
				String roomID = set.getValue().roomID;
				getAndPopulateEvents(roomID);
			}
			break;
		}
	}
	@Override
	public boolean connect() 
	{
		
	    try
	    {
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			String url = "jdbc:sqlserver://"+Config.DATABASE_IP+":"+Config.DATABASE_PORT+";databaseName=Narda";
	
			conn = DriverManager.getConnection(url, Config.DATABASE_USERNAME, Config.DATABASE_PASSWORD);
			OrbitStamps.log(OrbitStamps.LOG_NOTICE, "HuddingeDataMapper: connection success!");
			return true;
	    }
	    catch (ClassNotFoundException ex) {System.err.println(ex.getMessage());}
	    catch (IllegalAccessException ex) {System.err.println(ex.getMessage());}
	    catch (InstantiationException ex) {System.err.println(ex.getMessage());}
	    catch (SQLException ex)           {System.err.println(ex.getMessage());}
	 
	    OrbitStamps.log(OrbitStamps.LOG_NOTICE, "HuddingeDataMapper: connection failed!");
		return false;
	}
	private void getAndPopulateRooms()
	{
		// Get and populate rooms
		try
		{
			Statement stmnt = conn.createStatement();
			String strQuery = "select top 1 opsal_id, opsalnamn, platsnamn, plats_id from " + Config.DATABASE_OPERATION_VIEW + " WHERE platsnamn = 'Huddinge'";
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
			}
		}
		catch (SQLException ex)
			{System.err.println(ex.getMessage());}
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

				OrbitStamps.log(OrbitStamps.LOG_NOTICE, "GetAndPopulateEvents: op stamps opid= " + op.op_id);
				while(res.next()) 
				{ 

					
			        try 
			        {
						String handelse = res.getString("handelse");
						String tidpunkt = res.getString("tidpunkt");

						
						Timestamp newStamp = new Timestamp(Integer.toString(OrbitStamps.stampStringToOrder.get(handelse)));
						OrbitStamps.log(OrbitStamps.LOG_NOTICE, tidpunkt);
						newStamp.time = Calendar.getInstance().getTime();//df.parse(tidpunkt);
						newStamp.stringStamp = handelse;
						OrbitStamps.log(OrbitStamps.LOG_NOTICE, "GetAndPopulateEvents: Found timestamp= " + handelse);
						if(!op.stamps.contains(newStamp))
						{
							op.stamps.addFirst(newStamp);
						}
						
			        } catch (/*ParseException e*/ Exception e) 
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
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Statement stmnt;
		try {
			stmnt = conn.createStatement();
			String strQuery = "select optillfalle_id, opkort_id, iva, start_tidpunkt, slut_tidpunkt, beskrivning, tid_forberedelse, tid_operation from " + Config.DATABASE_OPERATION_VIEW + " WHERE opsal_id = " + roomID + " ORDER BY start_tidpunkt ASC";
			ResultSet res = stmnt.executeQuery(strQuery);
			Room r  = OrbitStamps.operatingRooms.get(roomID);
			while (res.next()) 
			{ 

				String op_id = res.getString("optillfalle_id");
				
				if(!r.operations.containsKey(op_id))
				{
					String opkort_id = res.getString("opkort_id");
					String op_desc = (res.getString("beskrivning") != null) ? res.getString("beskrivning") : "Saknar beskrivning";
					java.util.Date op_start;
					java.util.Date op_end;
					try {
						op_start = df.parse(res.getString("start_tidpunkt"));
						op_end = df.parse(res.getString("slut_tidpunkt"));
					} catch (Exception e) {
						op_start = new Date(Calendar.getInstance().getTimeInMillis());
						op_end = new Date(Calendar.getInstance().getTimeInMillis());
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					Operation operation = new Operation(op_id, opkort_id, op_start, op_end, op_desc);
					r.operations.put(op_id, operation);
					operation.debugPrint();
				}
			}	
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
	
	}
	private void getAndPopulatePersonal(String roomID)
	{
		// Get and populate operatinginfo
		Statement stmnt;
		try {
			stmnt = conn.createStatement();
			String strQuery = "select personal_id, Roll, namn, streckkod, p.optillfalle_id, opsal_id from NarDa.dbo.v_OrbitNardaPersonal p, NarDa.dbo.v_OrbitNardaOperationsinfo op WHERE op.opsal_id = "+roomID+" AND op.optillfalle_id = p.optillfalle_id";
			ResultSet res = stmnt.executeQuery(strQuery);
			Room r  = OrbitStamps.operatingRooms.get(roomID);
			while (res.next()) 
			{ 
				String optillfalle_id = res.getString("optillfalle_id");
				Operation op = r.operations.get(optillfalle_id);
				
				if(op != null)
				{
					String namn = (res.getString("namn") != null) ? res.getString("namn") : "Inget namn angivet";
					String roll = (res.getString("Roll") != null) ? res.getString("Roll") : "Okänd roll";
					String personal_id = (res.getString("personal_id") != null) ? res.getString("personal_id") : "Inget ID angivet";
					
					Person p = new Person(namn, new Role(roll), personal_id);
					p.devices.add(new PagerReciever("1234"));
					op.addPerson(p);
				}

			}	
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
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

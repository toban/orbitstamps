package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.logging.Log;

import service.communication.AscomPagerMessageChannel;
import service.communication.CommunicationHistory;
import service.communication.Message;
import service.communication.MessageChannel;
import service.communication.PagerReciever;
import service.filter.FilterManager;
import service.filter.FilterMessage;
import service.model.FunctionalPerson;
import service.model.Operation;
import service.model.Person;
import service.model.Role;
import service.model.Room;
import service.model.Timestamp;
import service.model.Urgency;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;


public class OrbitStamps
{
	
	static private WebServer server;
	static public DatabasePoll poller;
	static public MsgQueue msgQueue;
	
	public static boolean LOG_TO_FILE = false;
	static private FileWriter fs;
	static private BufferedWriter out;
	static private Calendar cal;
	
	static public FilterManager filterManager;
	static public HashMap<String,Room> operatingRooms;
	
	static public MessageChannel DEFAULT_CHANNEL = new AscomPagerMessageChannel();
	
	static public ArrayList<MessageChannel> channels = new ArrayList<MessageChannel>();
	public static Map<String, Integer> stampStringToOrder = new HashMap<String, Integer>();
	
	public static final String DIR_XML_FILTERS = "filters/xml";
	static public final int LOG_ERROR = 0;
	static public final int LOG_NOTICE = 0;
	static public final String DATABASE_FILEPATH = "db/";
	static public final String DATABASE_PERSON_FILEPATH = DATABASE_FILEPATH + "person.db4o";
	static public final String DATABASE_ROOM_FILEPATH = DATABASE_FILEPATH + "room.db4o";
	
	static public final String[] STATIC_TEMPLATE_MESSAGES = {"Patienten har anlänt PREOP", 
															"Patienten har anlänt på sal",
															"Anestesi påbörjad",
															"Anestesi inledning klar",
															"Klart för operation",
															"Operation har påbörjats",
															"Operation avslutad",
															"Anestesi avslutad"};

	public static void main(String [] args)
	{	
		cal = Calendar.getInstance();
		String filename = "log_" + cal.get(Calendar.YEAR) + "" + cal.get(Calendar.MONTH) 
												+ "" + cal.get(Calendar.DATE)
												+ "_" + cal.get(Calendar.HOUR_OF_DAY)
												+ "" + cal.get(Calendar.MINUTE)
												+ "" + cal.get(Calendar.SECOND);
		stampStringToOrder.put("Ringt avd", 1);
		stampStringToOrder.put("Anlänt op", 2);
		stampStringToOrder.put("Patienttid -start", 3);
		stampStringToOrder.put("Förberedelser enl. WHO", 4);
		stampStringToOrder.put("Anestesi -start", 5);
		stampStringToOrder.put("Anestesiinledning klar", 6);
		stampStringToOrder.put("Klart för operatör", 7);
		stampStringToOrder.put("Time out", 8);
		stampStringToOrder.put("Operation -start", 9);
		stampStringToOrder.put("Operation -slut", 10);
		stampStringToOrder.put("Avslutning enl.WHO", 11);
		stampStringToOrder.put("Anestesi -slut", 12);
		stampStringToOrder.put("Patienttid -slut", 13);
		
		System.out.println(filename);
		
		if(LOG_TO_FILE)
		{
			try 
			{
				File file = new File("/home/tobias/skolarbete/EXJOBB2012/orbitstamps/log/" + filename);
				//File file = new File("log/" + filename);
				boolean exist = file.createNewFile();
				
				if (!exist)
				{
					System.out.println("not exists");
				}
				
				fs = new FileWriter(file);
				out = new BufferedWriter(fs);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG_TO_FILE = false;
				log(LOG_ERROR, "Could not create logfile");
			}
		}
		
		log(LOG_NOTICE, "Starting application " + cal.getTime());
		
		//LOAD CONFIG
		if(loadConfig())
		{
			
			channels.add(DEFAULT_CHANNEL);
			
		//INIT Rooms
		operatingRooms = new HashMap<String, Room>();
		
		// INIT POLLER
		poller = new DatabasePoll(new HuddingeDataMapper());
		poller.updateData(DataMapper.DATATYPE_ALL);
		//createDummyPeople(); // debug
		poller.start();
		
		// INIT MSG-QUEUE
		msgQueue = new MsgQueue();
		msgQueue.start();
		
		// Read filters
		filterManager = new FilterManager();
		for(FilterMessage fm : filterManager.filters)
				fm.printDebug();
		
		
		loadPersistantData();
		listAllPersistant();
		/*
		// DUMMY
		createDummyData();
		*/
		
		
		
		// INIT INTERFACE
		server = new WebServer();
		server.init(8080);
			
		//log(LOG_NOTICE,"connect = " + poller.debugConnect());
		}
		
	}
	
	//populate orbit data with persistant data
	private static void loadPersistantData()
	{
		// Setup DB4o 
		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DATABASE_PERSON_FILEPATH);
		try {

			ObjectSet<FunctionalPerson> result = db.queryByExample(FunctionalPerson.class); // load all rooms with persistant data
			for(FunctionalPerson function : result)
			{
				if(operatingRooms.containsKey(function.roomID))
				{
					if(operatingRooms.get(function.roomID).getPersistantPerson(function.ID) == null)
					{
						operatingRooms.get(function.roomID).addPersistantPerson(function);
					}
				}
			}
		}
		finally {
		    db.close();
		}
	}
	public static void deleteFunctionalPerson(String id, String roomID)
	{
		Room room = operatingRooms.get(roomID);
		if(room == null)
			return;
		
		Person p  = room.getPersistantPerson(id);
		if(p==null)
			return;
		
		if(p instanceof FunctionalPerson)
		{
			room.deletePersistantPerson(p);
			// Setup DB4o 
			ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DATABASE_PERSON_FILEPATH);
			try {
				ObjectSet<FunctionalPerson> result = db.queryByExample(FunctionalPerson.class);
				
				for(FunctionalPerson found : result)
				{
					if(found.ID.equals(id) && found.roomID.equals(roomID))
						db.delete(found);
				}
			}
			finally {
			    db.close();
			}
		}
	}
	public static void listAllPersistant()
	{
		// Setup DB4o 
		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DATABASE_PERSON_FILEPATH);
		try {
			ObjectSet<FunctionalPerson> result = db.queryByExample(FunctionalPerson.class); // load all rooms with persistant data
			for(FunctionalPerson function : result)
			{
				System.out.println("ID= " +function.ID + ", name=" + function.name + ", room=" + function.roomID + ", devices=" + function.devices.size());
			}
		}
		finally {
		    db.close();
		}
	}
	public static void savePersistantData()
	{
		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DATABASE_PERSON_FILEPATH);
		try
		{
			for(Entry<String,Room> entry : operatingRooms.entrySet())
			{
				Room room = entry.getValue();
				
				for(Person p : room.getPersistantPersons())
				{
					if(p instanceof FunctionalPerson)
					{
						ObjectSet<FunctionalPerson> result = db.queryByExample(FunctionalPerson.class);
						
						for(FunctionalPerson found : result)
						{
							if(found.ID.equals(p.ID) && found.roomID.equals(((FunctionalPerson)p).roomID))
								db.delete(found);
						}
						db.store(p);
					}
				}
				
				
			}
		}
		finally {
		    db.close();
		}
	}
	/*
	public static void createDummyData()
	{
		int numppl = 10;
		int numRooms = 10;
		int numStamps = 300;
		
		Random rand = new Random();
		
		for(int i = 0; i < numRooms; i++)
		{
			String rum = Integer.toString(i);
			operatingRooms.put(rum, new Room(rum));
			
			for(int y = 0; y < numppl; y++)
			{
				Person p = new Person("Test person" + Integer.toString(y), 
									new Role(Role.ROLE_STRING_ARRAY[rand.nextInt(Role.ROLE_STRING_ARRAY.length)]),
									Integer.toString(rand.nextInt(9999)));
				p.devices.add(new PagerReciever(Integer.toString(rand.nextInt(9999))));
				
				operatingRooms.get(rum).addPerson(p);
			}
			createDummyTimestamps(numStamps, rum, rand);
		}
			
		for(int i = 0; i < 1000; i++)
		{
			
		}
	}
	public static void createDummyTimestamps(int numStamps, String rum, Random rand)
	{
		for(int y = 0; y < numStamps; y++)
		{
			operatingRooms.get(rum).newStamps.add(new Timestamp(Integer.toString(rand.nextInt(11))));
		}
	}
	*/
	public static void createDummyPeople()
	{
		int numppl = 10;
		
		Random rand = new Random();
		
		for(Entry<String, Room> roomSet : operatingRooms.entrySet())
		{
			Room room = roomSet.getValue();
			for(Entry<String, Operation> opEntry : room.operations.entrySet())
			{
				Operation op = opEntry.getValue();
				for(int y = 0; y < numppl; y++)
				{
					Person p = new Person("Test person" + Integer.toString(y), 
										new Role(Role.ROLE_STRING_ARRAY[rand.nextInt(Role.ROLE_STRING_ARRAY.length)]),
										Integer.toString(rand.nextInt(9999)));
					p.devices.add(new PagerReciever(Integer.toString(rand.nextInt(9999))));
					
					op.addPerson(p);
				}
			}
		}
			
		for(int i = 0; i < 1000; i++)
		{
			
		}
	}
	public static void processRoomTimestamps()
	{
		// no need if it's empty
		if(filterManager != null && filterManager.filters.size() > 0)
		{
			for(Entry<String, Room> roomEntry : operatingRooms.entrySet())
			{
				Room room = roomEntry.getValue();
				
				// if we have any new stamps here
				for(Entry<String, Operation> entry : room.operations.entrySet())
				{
					Operation op = entry.getValue();
					for(Timestamp ts : op.stamps)
					{
						// if the stamp already is parsed, go next
						if(ts.parsed)
							continue;
						
						for(Person person : op.getPeople())
						{
							// no need if no devices bound to this person
							if(person.devices != null && person.devices.size() > 0) 
							{
								for(FilterMessage fm : filterManager.filters)
								{
									// if the room, person, and timestamp is ok, its a match
									if(fm.match(room, person, ts))
									{						
										msgQueue.add(new MsgQueueItem(room, person, fm.msg, CommunicationHistory.HISTORY_TYPE_AUTO));
										
										log(OrbitStamps.LOG_NOTICE, "!!! Match found " + person.name + " role= " + person.role.getRole() + " room= " + room.roomID);
									}
									else
									{
										log(OrbitStamps.LOG_NOTICE, "NO MATCH " + person.name + " role= " + person.role.getRole() + " room= " + room.roomID);
									}
								}
							}
						}
						
						// mark the timestamp as parsed
						ts.parsed = true;
						
					}
				}
			}
			
		}
	}
	public static boolean loadConfig()
	{
		ConfigLoader cl = new ConfigLoader();
		if(cl.readConfig())
		{
			log(LOG_NOTICE, "Loaded config!!");
			return true;
		}
		else
		{
			log(LOG_ERROR, "Could not load config!");
			return false;
		}
	}
	public static void log(int type, String s)
	{
		if(LOG_TO_FILE)
		{
			try
			{
				out = new BufferedWriter(fs);
				out.append("LOG: " + s);
			}
			catch (Exception e)
			{
				System.out.println("LOG: " + s);
			}
		}
		else
		{
			System.out.println("LOG: " + s);
		}
	}
}

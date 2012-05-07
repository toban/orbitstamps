package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.logging.Log;

import service.communication.AscomPagerMessageChannel;
import service.communication.CommunicationHistory;
import service.communication.Message;
import service.communication.MessageChannel;
import service.communication.PagerReciever;
import service.filter.FilterManager;
import service.filter.FilterMessage;
import service.model.FunctionalPerson;
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
			
		
		// INIT POLLER
		poller = new DatabasePoll(new HuddingeDataMapper());
		poller.start();
		
		// INIT MSG-QUEUE
		msgQueue = new MsgQueue();
		msgQueue.start();
		
		// Read filters
		filterManager = new FilterManager();
		for(FilterMessage fm : filterManager.filters)
				fm.printDebug();
		
		//INIT Rooms
		operatingRooms = new HashMap<String, Room>();
		loadPersistantData();
		listAllPersistant();
		// DUMMY
		createDummyData();
		// INIT INTERFACE
		server = new WebServer();
		server.init(8080);
		
		
		AscomPagerMessageChannel channel = new AscomPagerMessageChannel();
		PagerReciever pr = new PagerReciever("1234");
		Message msg = new Message("hello!", 1, 3);
		channel.sendMessage(msg, pr);
			
			
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
					if(operatingRooms.get(function.roomID).getPerson(function.ID) == null)
					{
						operatingRooms.get(function.roomID).addPerson(function);
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
		
		Person p  = room.getPerson(id);
		if(p==null)
			return;
		
		if(p instanceof FunctionalPerson)
		{
			room.deletePerson(p);
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
				
				for(Person p : room.getPeople())
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
	public static void processRoomTimestamps()
	{
		// no need if it's empty
		if(filterManager != null && filterManager.filters.size() > 0)
		{
			for(Entry<String, Room> roomEntry : operatingRooms.entrySet())
			{
				Room room = roomEntry.getValue();
				
				
				// if we have any new stamps here
				if(room.newStamps.size() > 0)
				{
					for(Person person : room.getPeople())
					{
						// no need if no devices bound to this person
						if(person.devices != null && person.devices.size() > 0) 
						{
							for(FilterMessage fm : filterManager.filters)
							{
								// if the room, person, and timestamp is ok, its a match
								if(fm.match(room, person))
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
					
					// move all the processed stuff to history.
					room.newStamps.clear();
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

package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Random;

import org.mortbay.jetty.servlet.Context;

import roles.Role;
import timestamps.Timestamp;


public class OrbitStamps
{
	
	static private WebServer server;
	static public DatabasePoll poller;
	
	public static boolean LOG_TO_FILE = false;
	static private FileWriter fs;
	static private BufferedWriter out;
	static private Calendar cal;
	
	static public FilterManager filterManager;
	static public HashMap<String,Room> operatingRooms;
	
	public static final String DIR_XML_FILTERS = "filters/xml";
	static public final int LOG_ERROR = 0;
	static public final int LOG_NOTICE = 0;
	
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
		
		
		// INIT POLLER
		poller = new DatabasePoll("ip", "usr", "pass");
		//poller.start();
		
		
		// Read filters
		filterManager = new FilterManager();
		
		for(FilterMessage fm : filterManager.filters)
		{
				//fm.printDebug();
		}
		
		//INIT Rooms
		operatingRooms = new HashMap<String, Room>();

		
		createDummyData();
		processTimestamps();
		
		// INIT INTERFACE
		server = new WebServer();
		server.init(8080);
		
		
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
			
			for(int y = 0; y < numStamps; y++)
			{
				operatingRooms.get(rum).newStamps.add(new Timestamp(Integer.toString(rand.nextInt(11))));
			}
		}
			
		for(int i = 0; i < 1000; i++)
		{
			
		}
	}
	public static void processTimestamps()
	{
		// no need if it's empty
		if(filterManager != null && filterManager.filters.size() > 0)
		{
			for(Entry<String, Room> roomEntry : operatingRooms.entrySet())
			{
				Room room = roomEntry.getValue();
				
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
								// TODO: should go through some interface ...
								room.messageHistory.add(new CommunicationHistory(fm.msg, 
																			person.devices.iterator().next(),
																			CommunicationHistory.HISTORY_TYPE_AUTO,
																			person.ID));
								
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
	public static void testMessage()
	{
		//Test Message
		AscomPagerMessageChannel channel = new AscomPagerMessageChannel();
		PagerReciever pager = new PagerReciever("7491");
		Message msg = new Message("hello world", Urgency.LEVEL_IMPORTANT, Message.MESSAGE_BEEP_SIGNAL);
		channel.sendMessage(msg,pager);
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

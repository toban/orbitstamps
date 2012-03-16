package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import org.mortbay.jetty.servlet.Context;


public class OrbitStamps
{
	static private WebServer server;
	static public DatabasePoll poller;
	public static boolean LOG_TO_FILE = false;
	static private FileWriter fs;
	static private BufferedWriter out;
	static private Calendar cal;
	
	static public final int LOG_ERROR = 0;
	static public final int LOG_NOTICE = 0;
	
	public static void main(String [] args)
	{	
		System.out.println("started");
		cal = Calendar.getInstance();
		String filename = "log_" + cal.get(Calendar.YEAR) + "" + cal.get(Calendar.MONTH) 
												+ "" + cal.get(Calendar.DATE)
												+ "_" + cal.get(Calendar.HOUR_OF_DAY)
												+ "" + cal.get(Calendar.MINUTE)
												+ "" + cal.get(Calendar.SECOND);
		System.out.println(filename);
		try {
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
		
		log(LOG_NOTICE, "Starting application " + cal.getTime());
		
		
		// INIT POLLER
		poller = new DatabasePoll("ip", "usr", "pass");
		//poller.start();
		
		// INIT INTERFACE
		server = new WebServer();
		server.init(8080);
		
	}
	public static void testMessage()
	{
		//Test Message
		AscomPagerMessageChannel channel = new AscomPagerMessageChannel();
		PagerReciever pager = new PagerReciever("7491");
		Message msg = new Message("hello world", Urgency.LEVEL_IMPORTANT, pager, Message.MESSAGE_BEEP_SIGNAL);
		channel.sendMessage(msg);
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

package service;

import com.sun.net.httpserver.*;

import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;

public class WebService
{
	static JettyServer server;
	public static void main(String [ ] args)
	{	
		server = new JettyServer();
	}
	public static void log(String s)
	{
		System.out.println("LOG: " + s);
	}
}

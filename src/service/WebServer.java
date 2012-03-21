package service;

import java.net.URL;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.webapp.WebAppContext;


public class WebServer {
	
	 public void init(int port)
	 {
		 
		 final String WEBAPPDIR = "web/www";
		 
		 final Server server = new Server(port);
		  
		  
		 final String CONTEXTPATH = "/";
		 
		 final URL warUrl = this.getClass().getClassLoader().getResource(WEBAPPDIR);
		 final String warUrlString = warUrl.toExternalForm();
		 Context context = new WebAppContext(warUrlString, CONTEXTPATH);
		 context.setResourceBase(warUrlString);
		 server.setHandler(context);

		  try {
		   server.start();
		   server.join();
		  } catch (Exception e) {
		   OrbitStamps.log(OrbitStamps.LOG_ERROR, "Failed to start server");
		  }
		 }
}

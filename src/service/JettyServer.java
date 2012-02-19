package service;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class JettyServer {
 public static void main(String[] args) {
  Server server = new Server(8080);

  WebAppContext context = new WebAppContext();
  context.setResourceBase("/home/tobias/workspace/exjobb/www");
  //context.setDescriptor("../../www/web.xml");
  context.setContextPath("/");
  context.setParentLoaderPriority(true);
  server.setHandler(context);

  try {
   server.start();
   server.join();
  } catch (Exception e) {
   e.printStackTrace();
  }
 }
}

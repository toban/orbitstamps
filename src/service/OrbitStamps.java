package service;

public class OrbitStamps
{
	static JettyServer server;
	public static void main(String [ ] args)
	{	
		server = new JettyServer();
		server.init();
		
		log("dicks" + server.toString());
	}
	public static void log(String s)
	{
		System.out.println("LOG: " + s);
	}
}

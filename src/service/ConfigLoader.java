package service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import service.OrbitStamps;
import service.communication.Message;
import service.model.Role;
import service.model.Timestamp;


public class ConfigLoader 
{
	
	public String getElementString(Element e, String tagName)
	{
		NodeList element = e.getElementsByTagName(tagName);
		String ret = null;
		if(element != null && element.getLength() > 0)
		{
			Node n = element.item(0).getFirstChild();
			if(n!=null) // has value
				ret = n.getNodeValue();
			
		}
		return (ret != null) ? ret : "";
	}
	public ArrayList<String> getElementStringArray(Element e, String tagName)
	{
		NodeList elements = e.getElementsByTagName(tagName);

		ArrayList<String> strings = new ArrayList<String>();
		if(elements != null && elements.getLength() > 0)
		{
			for(int i = 0 ; i < elements.getLength(); i++)
			{
				strings.add(elements.item(i).getFirstChild().getNodeValue());
			}
			
		}
		return strings;
	}
	public boolean parseDom(Document dom)
	{	
		Element root = dom.getDocumentElement();
		
		// get the message part
		NodeList dataNode = root.getElementsByTagName("database");
		
		if(dataNode != null && dataNode.getLength() > 0) 
		{
			// TODO: Error-handling;
			Element dataEl = (Element)dataNode.item(0);
			
			try
			{
				Config.DATABASE_IP = getElementString(dataEl,"ip");
				Config.DATABASE_NAME = getElementString(dataEl,"name");
				Config.DATABASE_USERNAME = getElementString(dataEl,"user");
				Config.DATABASE_PASSWORD = getElementString(dataEl,"password");
				Config.DATABASE_PORT = getElementString(dataEl,"port");
				
				OrbitStamps.log(OrbitStamps.LOG_NOTICE, "ConfigLoader: db: " + Config.DATABASE_NAME);
				OrbitStamps.log(OrbitStamps.LOG_NOTICE, "ConfigLoader: db-ip: " + Config.DATABASE_IP);
				OrbitStamps.log(OrbitStamps.LOG_NOTICE, "ConfigLoader: db-username: " + Config.DATABASE_USERNAME);
				OrbitStamps.log(OrbitStamps.LOG_NOTICE, "ConfigLoader: db-port: " + Config.DATABASE_PORT);
				return true;
			}
			catch(Exception e)
			{
				OrbitStamps.log(OrbitStamps.LOG_ERROR, "ConfigLoader: Error while parsing config ... ");
				return false;
			}
		}
		return false;
	}
	public boolean readConfig()
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		// validation
		//dbf.setValidating(true);
		//dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		//dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource",xmlURL+"/message.dtd");
		
		try 
		{
			Document dom;
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.parse("config.xml");
			
			if(dom!=null)
			{
				return parseDom(dom);
			}
			else
			{
				OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Config: Config was not found ...");
			}
		}
		catch(ParserConfigurationException pce) 
		{
			pce.printStackTrace();
		}
		catch(SAXException se) {
			se.printStackTrace();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return false;
	}
	
}

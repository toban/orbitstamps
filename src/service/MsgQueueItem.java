package service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import service.communication.Message;
import service.filter.FilterMessage;
import service.model.Person;
import service.model.Room;
import service.model.Timestamp;


public class MsgQueueItem
{
	public Room room;
	public Person person;
	public Message msg;
	public final int msgType;
	
	public MsgQueueItem(Room r, Person p, Message msg, int msgType)
	{
		room = r;
		person = p;
		this.msg = msg;
		this.msgType = msgType;
		parseBody();
	}
	
	public void parseXML(Document dom)
	{
		StringBuilder sb = new StringBuilder();
		Element root = dom.getDocumentElement();
		NodeList list = root.getChildNodes();
		
		for(int i = 0; i < list.getLength(); i++)
		{
			String nodeName = list.item(i).getNodeName();
			if(nodeName == "#text")
				sb.append(list.item(i).getNodeValue());
			else if(nodeName == "room-name")
			{
				sb.append(room.roomID);
			}
		}
		
		msg.setBody(sb.toString());
		
	}
	
	public boolean parseBody()
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		// validation
		//dbf.setValidating(true);
		//dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		//dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource",xmlURL+"/message.dtd");
		
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "MsgQueueItem: Parsing body ...");

		try 
		{
			Document dom;
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringReader reader = new StringReader("<body>"+ msg.getBody() + "</body>");
			InputSource inputSource = new InputSource(reader);
			dom = db.parse(inputSource);
			
			if(dom!=null)
			{
				parseXML(dom);
			}
			else
			{
				OrbitStamps.log(OrbitStamps.LOG_NOTICE, "MsgQueueItem: body was null");
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
			return true;
	}


}

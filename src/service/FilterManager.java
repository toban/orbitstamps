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
import org.xml.sax.SAXException;

import roles.Role;
import timestamps.Timestamp;

public class FilterManager 
{
	
	public ArrayList<FilterMessage> filters;
	
	public FilterManager()
	{
		filters = new ArrayList<FilterMessage>();
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "FilterManager Created!");
		readFilters();
	}
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
	// parse dom and return a filter
	public FilterMessage parseDom(Document dom)
	{
		int maxDelay = 0;
		ArrayList<String> rooms = new ArrayList<String>(); 
		ArrayList<Role> roles = new ArrayList<Role>();
		Timestamp stamp = null;
		String body,callback;
		int urgency,signal;
		Message msg = null;
		
		Element root = dom.getDocumentElement();
		
		// get the message part
		NodeList dataNode = root.getElementsByTagName("data");
		
		if(dataNode != null && dataNode.getLength() > 0) 
		{
			// TODO: Error-handling;
			Element dataEl = (Element)dataNode.item(0);
			
			try
			{
				body = getElementString(dataEl,"body");
				callback = getElementString(dataEl,"callback");
				signal = Integer.parseInt(getElementString(dataEl,"signal"));
				urgency = Integer.parseInt(getElementString(dataEl,"urgency"));
				msg = new Message(body, urgency, signal);
			}
			catch(Exception e)
			{
				OrbitStamps.log(OrbitStamps.LOG_ERROR, "FilterManager: Error while parsing message ... ");
			}
		}
		
		// get the filter part
		NodeList filterNodes = root.getElementsByTagName("filter");
		
		if(filterNodes != null && filterNodes.getLength() > 0) 
		{
				Element filterElement = (Element)filterNodes.item(0);
				stamp = new Timestamp(filterElement.getAttribute("timestamp"));
				
				// get the rooms
				for(String s : getElementStringArray(filterElement, "room"))
				{
					rooms.add(s);
				}
				
				// get the roles
				for(String s : getElementStringArray(filterElement, "role"))
				{
					Role r = new Role(s);
					roles.add(r);
			}
		}
		
		
		// check if not broken.
		if(stamp != null && msg != null)
		{
			OrbitStamps.log(OrbitStamps.LOG_NOTICE, "FilterManager: Parsed a filter ... ");
			FilterMessage f = new FilterMessage(roles, stamp, maxDelay, rooms, msg);
			return f; 
		}
		else
		{
			return null;
		}
	}
	public void readFilters()
	{
		final URL warUrl = this.getClass().getClassLoader().getResource(OrbitStamps.DIR_XML_FILTERS);
		final String xmlURL = warUrl.toExternalForm();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		// validation
		//dbf.setValidating(true);
		//dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		//dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource",xmlURL+"/message.dtd");

		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "FilterManager: Reading filters ...");
		
		File folder = new File(warUrl.getFile());
		File[] listOfFiles = folder.listFiles();
		for(File f : listOfFiles)
		{
			
			String filePath = f.getPath();
			String fileName = f.getName();
			int index = fileName.lastIndexOf(".") != -1 ? fileName.lastIndexOf(".") : 0;
			String ext = fileName.substring(index,fileName.length());
			if(f.isFile() && ext.equals(".xml"))
			{
				
				try 
				{
					Document dom;
					DocumentBuilder db = dbf.newDocumentBuilder();
					
					dom = db.parse(filePath);
				
					if(dom!=null)
					{
						filters.add(parseDom(dom));
					}
					else
					{
						OrbitStamps.log(OrbitStamps.LOG_NOTICE, "FilterManager: Filter was null ...");
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
			}
		}
	}
}

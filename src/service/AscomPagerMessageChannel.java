package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class AscomPagerMessageChannel implements MessageChannel {

	public final String formURL;
	
	public AscomPagerMessageChannel()
	{
		formURL = "http://10.48.64.26/cgi-bin/npcgi";
	}
	@Override
	public boolean sendMessage(Message msg, MessageReciever recv)
	{
		OrbitStamps.log(OrbitStamps.LOG_NOTICE, "Sending message to " + recv.number);
		try{
		
			String charset = "UTF-8";
			
			// setup query
			String query = String.format("pri=7&bp=%s&url=&ack=0&no=%s&msg=%s",
									 URLEncoder.encode(Integer.toString(msg.getSignal()), charset),
									 URLEncoder.encode(recv.number.toString(), charset), 
									 URLEncoder.encode(msg.getMessage(), charset));
			
			URL url = new URL(formURL+"?"+query);
			HttpURLConnection conn = null;
			HttpURLConnection.setFollowRedirects(false);
			conn = (HttpURLConnection) url.openConnection();
			
		
			
			//System.out.println(url.toString());
			conn.connect();
			Map<String,List<String>> map = conn.getHeaderFields();
			conn.disconnect();
			
			
			
			for(String val : map.keySet())
			{
				System.out.println(val + ": " + map.get(val));
			}
			return true;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			System.out.println("made it here");
			return false;
		}
	}

	@Override
	public boolean isAvaliable() 
	{
		return false;
	}

	
}

package service.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import service.OrbitStamps;

public class AscomPagerMessageChannel implements MessageChannel {

	
	/**
	 * 
	 * Signal BP
	 * 7 =
	 * 6 = 
	 * 5 = telephone-ish HIGHER
	 * 4 = telehpone-ish
	 * 3 = (standard ramp up) beep beep beep
	 * 2 = beep beep
	 * 1 = beep
	 * @author tobias
	 *
	 */
	public static final int MESSAGE_DEFAULT_SIGNAL = 3;
	public static final int MESSAGE_BEEP_SIGNAL = 1;
	public static final int MESSAGE_2BEEP_SIGNAL = 2;
	public static final int MESSAGE_3BEEP_UP_SIGNAL = 3;
	public static final int MESSAGE_PHONE_SIGNAL = 4;
	public static final int MESSAGE_PHONE_HIGH_SIGNAL = 5;
	
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
									 URLEncoder.encode(Integer.toString(getSignal()), charset),
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
			return false;
		}
	}

	@Override
	public boolean isAvaliable() 
	{
		return false;
	}
	@Override
	public int getSignal() 
	{
			return MESSAGE_DEFAULT_SIGNAL;
	}

	
}

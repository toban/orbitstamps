package service;

public abstract class Urgency
{
	public static final int LEVEL_INFO = 0;
	public static final int LEVEL_IMPORTANT = 1;
	public static final String LEVEL_IMPORTANT_STRING = "VIKTIGT";
	public static final String LEVEL_INFO_STRING = "INFO";
	
	public static String getUrgencyString(Message msg)
	{
		String level = new String();
		switch(msg.getUrgency())
		{
		
			default:
				level = LEVEL_INFO_STRING;
				
			case LEVEL_INFO:
				level = LEVEL_INFO_STRING;
				
			case LEVEL_IMPORTANT:
				level = LEVEL_IMPORTANT_STRING;
			
		}

		// compress
		if((msg.getBody()+LEVEL_INFO_STRING).length() > 120)
			level = level.substring(0,1);
		
		return level;
	}
}

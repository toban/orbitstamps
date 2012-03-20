package service;

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
public class Message 
{
	public static final int MESSAGE_DEFAULT_SIGNAL = 3;
	public static final int MESSAGE_BEEP_SIGNAL = 1;
	public static final int MESSAGE_2BEEP_SIGNAL = 2;
	public static final int MESSAGE_3BEEP_UP_SIGNAL = 3;
	public static final int MESSAGE_PHONE_SIGNAL = 4;
	public static final int MESSAGE_PHONE_HIGH_SIGNAL = 5;
	
	private String body;
	private int urgency;
	private String callback;
	private int signal;
	
	public Message(String body, int urgency, int signal)
	{
		this.body = body;
		this.urgency = urgency;
		this.signal = signal;
	}
	public int getSignal()
	{
		if(signal!=0)
			return signal;
		else
			return MESSAGE_DEFAULT_SIGNAL;
	}
	public String getMessage()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(Urgency.getUrgencyString(this) + ": ");
		sb.append(body);
		
		if(sb.length() > 120)
			return sb.substring(0,120);
		else
			return sb.toString();
	}
	
	public int getUrgency() {
		return urgency;
	}

	public void setUrgency(int urgency) 
	{
		this.urgency = urgency;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}
}

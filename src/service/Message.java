package service;

public class Message 
{	
	private String body;
	private int urgency;
	private String callback;
	public int signal;
	
	public Message(String body, int urgency, int signal)
	{
		this.body = body;
		this.urgency = urgency;
		this.signal = signal;
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

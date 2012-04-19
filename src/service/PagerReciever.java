package service;



public class PagerReciever extends MessageReciever {

	public PagerReciever(String number)
	{
		this.number = number;
	}

	@Override
	public boolean isCompatibleWith(MessageChannel mch) 
	{
		if(mch instanceof AscomPagerMessageChannel)
		{
			return true;
		}
		return false;
	}
}

package service;

public class HuddingeDataMapper extends DataMapper {

	@Override
	public void mapToModel(Object obj) 
	{

	}
	@Override
	public boolean connect(String username, String userpass, String ip) 
	{
		return false;
	}
	@Override
	public boolean isConnected() 
	{
		return false;
	}

}

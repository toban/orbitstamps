package service;

import java.sql.*;

public class HuddingeDataMapper extends DataMapper {

	
	@Override
	public void mapToModel(Object obj) 
	{
		
	}
	@Override
	public boolean connect() 
	{

	    try
	    {
			Connection conn;
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			String url = "jdbc:sqlserver://"+Config.DATABASE_IP+":"+Config.DATABASE_PORT+";databaseName=Narda";
	
			conn = DriverManager.getConnection(url, Config.DATABASE_USERNAME, Config.DATABASE_PASSWORD);
			
			Statement stmnt = conn.createStatement();
			String strQuery = "select top 10 * from NarDa.dbo.v_OrbitNardaSms";
			ResultSet res = stmnt.executeQuery(strQuery);

			while (res.next()) 
			{ 
				String val = res.getString(8);
				//res.get
				System.out.println("namn = " + val);
			}
			
			conn.close();
			return true;
	    }
	    catch (ClassNotFoundException ex) {System.err.println(ex.getMessage());}
	    catch (IllegalAccessException ex) {System.err.println(ex.getMessage());}
	    catch (InstantiationException ex) {System.err.println(ex.getMessage());}
	    catch (SQLException ex)           {System.err.println(ex.getMessage());}
	 
		return false;
	}

	@Override
	public boolean isConnected() 
	{
		return false;
	}

}

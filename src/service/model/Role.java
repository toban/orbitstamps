package service.model;

public class Role 
{
	public static final String ROLE_SSK_OP = "Ssk op";
	public static final String ROLE_USK_OP = "Usk op";
	
	public static final String ROLE_SSK_ANE = "Ssk ane";
	public static final String ROLE_USK_ANE = "Usk ane";
	
	public static final String ROLE_FUNKTION = "Funktion";
	public static final String ROLE_FUNKTION_CLEANING = "Funktion städning";
	public static final String ROLE_FUNKTION_CARRY = "Funktion lyft";
	
	public static final String[] ROLE_STRING_ARRAY = {ROLE_SSK_OP, ROLE_USK_OP, ROLE_SSK_ANE,ROLE_USK_ANE};
	public static final String[] ROLE_FUNCTION_ARRAY = {ROLE_FUNKTION_CLEANING, ROLE_FUNKTION_CARRY};
	
	private String roleID;
	
	public Role(String roleID)
	{
		this.roleID = roleID;
	}
	public boolean equals(Role p)
	{
		return this.roleID.equals(p.getRole());
	}
	public Role(int roleID)
	{
		switch(roleID)
		{
		case 27:
			this.roleID = ROLE_SSK_OP;
			break;
		case 25:
			this.roleID = ROLE_USK_OP;
			break;
		case 26: 
			this.roleID = ROLE_SSK_ANE;
		break;
		}
	}
	public String getRole() {return roleID;};
}

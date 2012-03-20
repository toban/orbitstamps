package roles;

public class Role 
{
	public static final String ROLE_AN_DOCTOR = "Lakare";
	public String roleID;
	public Role(String roleID)
	{
		this.roleID = roleID;
	}
	public String getRole() {return roleID;};
}

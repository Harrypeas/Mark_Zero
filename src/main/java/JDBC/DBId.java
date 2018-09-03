package JDBC;

public class DBId {
	private String db_name = null;
	
	public DBId(String name) {
		// TODO Auto-generated constructor stub
		db_name = name;
	}
	
	public String getDBName()
	{
		return db_name;
	}
	
	public void setDBName(String name)
	{
		db_name = name;
	}
}

package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Conn {
	private static Connection conn = null;
	private static ArrayList<DBId> dBList = new ArrayList<>();
	private static DBId current_db = null;
	
	/**
	 * 
	 * @return jdbc connection
	 */
	public static Connection getConn()
	{
		if (conn == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost/Harrypeas","root","950323qq");
				current_db = new DBId("Harrypeas");
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "The connection is not availabel now, please try again later!");
				e.printStackTrace();
				return null;
			}
		}
		return conn;
	}
	
	/**
	 * close the connection
	 */
	public static void shutdown()
	{
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * show all names of existing databases
	 */
	public static void getDBs()
	{
		if (conn != null) {
			try {
				Statement statement = conn.createStatement();
				String sql = "show databases;";
				ResultSet rSet = statement.executeQuery(sql);
				dBList.clear();
				while(rSet.next())
				{
					System.out.println(rSet.getString(1));
					dBList.add(new DBId(rSet.getString(1)));
				}
				statement.close();
				new JDBC_Choice(dBList);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else 
		{
			System.out.println("Error! Connection is not exits!");
		}
	}
	
	public static void changeDB(String db_name)
	{
		if (current_db.equals(db_name)) {
			return;
		}
		
		try {
			Statement statement = conn.createStatement();
			String sql = "use " + db_name +";";
			current_db.setDBName(db_name);
			JDBC_Choice.set_current_text(current_db.getDBName());
			statement.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "Database Changed");
		JDBC_Choice.Refresh();
	}
	
	public static String getCurrentDBName()
	{
		return current_db.getDBName();
	}
}

package JDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

public class jdbc_test {
	public static void main(String[] args)
	{
		Connection connection = Conn.getConn();
		Statement statement;
		try {
			statement = (Statement) connection.createStatement();
			String sql = "select * from ZhangDan;";
			ResultSet rSet = statement.executeQuery(sql);
			
			ResultSetMetaData rMetaData = (ResultSetMetaData) rSet.getMetaData();
			System.out.println(rMetaData.getColumnName(1) + "|" + rMetaData.getColumnName(2) + "|" + rMetaData.getColumnName(3));
			while(rSet.next())
			{
				System.out.println(rSet.getString(1) + "|" + rSet.getString(2) + "|" + rSet.getString(3));
			}
			statement.close();
			Conn.getDBs();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

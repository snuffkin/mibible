package com.googlecode.mibible.browser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MibInfoDao
{
	
	private static MibInfoDao dao;
	private static final String INSERT = "INSERT INTO MIB VALUES(?, ?)";
	private static final String SELECT_BY_NAME = "SELECT oid, name FROM MIB WHERE name LIKE ?";
	
	private MibInfoDao()
	{
		try {
			Class.forName("org.h2.Driver");
			Connection conn = getConnection();
			Statement statement = conn.createStatement();
			statement.execute(
		        "DROP TABLE IF EXISTS MIB");
			statement.execute(
			    "CREATE TABLE MIB(oid VARCHAR(255) PRIMARY KEY, name VARCHAR(255))");
			statement.close();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static MibInfoDao getInstance()
	{
		if (dao == null)
		{
			dao = new MibInfoDao();
		}
		return dao;
	}
	private Connection getConnection() throws SQLException
	{
		return DriverManager.getConnection("jdbc:h2:~/mibible", "sa", "");
	}
	
	public void deleteAll()
	{
		try {
			Connection conn = getConnection();
			conn.setAutoCommit(false);
			Statement statement = conn.createStatement();
			statement.execute("DELETE FROM MIB");
			statement.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insert(List<MibInfo> list)
	{
		try {
			Connection conn = getConnection();
			conn.setAutoCommit(false);
			PreparedStatement statement  = conn.prepareStatement(INSERT);
			
			for (MibInfo info : list)
			{
				statement.setString(1, info.getOid());
				statement.setString(2, info.getName());
				statement.execute();
			}
			
			statement.close();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<MibInfo> selectByName(String name)
	{
		List<MibInfo> list = new ArrayList<MibInfo>();
		
		try {
			Connection conn = getConnection();
			conn.setAutoCommit(false);
			PreparedStatement statement  = conn.prepareStatement(SELECT_BY_NAME);
			statement.setString(1, "%" + name + "%");
			
			ResultSet result = statement.executeQuery();
			
			while (result.next())
			{
				String infoOid = result.getString("oid");
				String infoName = result.getString("name");
				list.add(new MibInfo(infoOid, infoName));
			}
			
			statement.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}

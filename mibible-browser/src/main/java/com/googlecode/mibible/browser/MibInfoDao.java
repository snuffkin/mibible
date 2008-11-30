package com.googlecode.mibible.browser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MibInfoDao {
	
	private static MibInfoDao dao;
	private static final String CONN_URL ="jdbc:h2:mem:mibible;DB_CLOSE_DELAY=-1";
	private static final String CONN_USER ="sa";	
	private static final String CONN_PASS ="";	
	private static final String CREATE
    	= "CREATE TABLE MIB(" +
    			" mib_name VARCHAR(50)," +
    			" oid VARCHAR(50) PRIMARY KEY," +
    			" name VARCHAR(50)," +
    			" upper_name VARCHAR(50))";
	private static final String DROP = "DROP TABLE IF EXISTS MIB";
	private static final String DELETE_ALL = "DELETE FROM MIB";
	private static final String INSERT = "INSERT INTO MIB VALUES(?, ?, ?, ?)";
	private static final String SELECT_BY_OID = "SELECT oid, name FROM MIB WHERE oid LIKE ?";
	private static final String SELECT_BY_NAME = "SELECT oid, name FROM MIB WHERE name LIKE ?";
	private static final String SELECT_BY_UPPER_NAME = "SELECT oid, name FROM MIB WHERE upper_name LIKE ?";
	
	private MibInfoDao() {
		try {
			Class.forName("org.h2.Driver");
			Connection conn = getConnection();
			conn.setAutoCommit(false);
			Statement statement = conn.createStatement();
			statement.execute(DROP);
			statement.execute(CREATE);
			statement.close();
			conn.commit();
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static MibInfoDao getInstance() {
		if (dao == null) {
			dao = new MibInfoDao();
		}
		return dao;
	}
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(CONN_URL, CONN_USER, CONN_PASS);
	}
	
	public void deleteAll() {
		try {
			Connection conn = getConnection();
			conn.setAutoCommit(false);
			Statement statement = conn.createStatement();
			statement.execute(DELETE_ALL);
			statement.close();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insert(String mibName, List<MibInfo> list) {
		try {
			Connection conn = getConnection();
			conn.setAutoCommit(false);
			PreparedStatement statement  = conn.prepareStatement(INSERT);
			
			for (MibInfo info : list) {
				statement.setString(1, mibName);
				statement.setString(2, info.getOid());
				statement.setString(3, info.getName());
				statement.setString(4, info.getName().toUpperCase());
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
	public List<MibInfo> selectByOid(String oid) {
		List<MibInfo> list = new ArrayList<MibInfo>();
		
		try {
			Connection conn = getConnection();
			conn.setAutoCommit(false);
			PreparedStatement statement  = conn.prepareStatement(SELECT_BY_OID);
			statement.setString(1, "%" + oid + "%");
			
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				String infoOid = result.getString("oid");
				String infoName = result.getString("name");
				list.add(new MibInfo(infoOid, infoName));
			}
			
			statement.close();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public List<MibInfo> selectByName(String name) {
		List<MibInfo> list = new ArrayList<MibInfo>();
		
		try {
			Connection conn = getConnection();
			conn.setAutoCommit(false);
			PreparedStatement statement  = conn.prepareStatement(SELECT_BY_NAME);
			statement.setString(1, "%" + name + "%");
			
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				String infoOid = result.getString("oid");
				String infoName = result.getString("name");
				list.add(new MibInfo(infoOid, infoName));
			}
			
			statement.close();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public List<MibInfo> selectByUpperName(String name) {
		List<MibInfo> list = new ArrayList<MibInfo>();
		
		try {
			Connection conn = getConnection();
			conn.setAutoCommit(false);
			PreparedStatement statement  = conn.prepareStatement(SELECT_BY_UPPER_NAME);
			statement.setString(1, "%" + name.toUpperCase() + "%");
			
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				String infoOid = result.getString("oid");
				String infoName = result.getString("name");
				list.add(new MibInfo(infoOid, infoName));
			}
			
			statement.close();
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}

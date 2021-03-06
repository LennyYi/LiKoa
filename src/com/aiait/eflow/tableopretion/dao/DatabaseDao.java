package com.aiait.eflow.tableopretion.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aiait.framework.db.BaseDAOImpl;
import com.aiait.framework.db.IDBManager;
import com.aiait.framework.exception.DAOException;
import com.aiait.framework.vo.BaseVO;

import net.sf.json.JSON;
import net.sf.json.JSONArray;

public class DatabaseDao extends BaseDAOImpl{

	public DatabaseDao(IDBManager dbManager) {
		super(dbManager);
		// TODO Auto-generated constructor stub
	}

	Connection conn = dbManager.getJDBCConnection();
	// 取所有表名
	public Object getTableName() {
		List<String> list = new ArrayList<String>();
		String[] para = new String[1];
		para[0] = "TABLE";
		Statement stm = null;
		
		try {
			stm = conn.createStatement();
			ResultSet rs = conn.getMetaData().getTables(null, null,	null, para);
			while (rs.next()) {
				list.add(rs.getString("TABLE_NAME"));
			}
			String[] arr = new String[list.size()];
			if (list != null && list.size() > 0) {
				for (int i = 0; i < arr.length; i++) {
					arr[i] = list.get(i);
				}
			}
			// 关闭连接
			rs.close();
			stm.close();
			conn.close();
			return arr;
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return null;
		}
	}

	// 取列名
	public List<String> getColumnsName(String table) {
		// table = "users";
		List<String> list = new ArrayList<String>();
		String cloname = "SELECT * FROM sys.columns WHERE object_id = object_id('"
				+ table + "')";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(cloname);
			while (rs.next()) {
				list.add(rs.getString(2).trim());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 取总记录数
	public int getRecords(String table) {
		String sql = "select count(*) from " + table;
		int i = 0;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String records = rs.getString(1);
				i = Integer.parseInt(records);
			}
			System.out.println("----->>" + i);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}

		return i;
	}

	// 取列数
	public int getColCount(String table) {
		String sql = "SELECT * FROM " + table;
		int colcount = 0;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			colcount = rs.getMetaData().getColumnCount();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return colcount;
	}

	// 取数据库中表的内容
	public JSON getDataTable(String table) {
		List<String> list = new ArrayList<String>();
		try {
			String strCmd = "SELECT * FROM " + table;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(strCmd);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colnum = rs.getMetaData().getColumnCount();
			String[] arr = null;
			JSONArray jsons = new JSONArray();
			Map<String, String> map = new HashMap<String, String>();
			while (rs.next()) {
				for (int i = 1; i <= colnum; i++) {
					map.put(rsmd.getColumnName(i), rs.getString(i).trim());
				}
				jsons.add(map);
			}
			// 关闭连接
			rs.close();
			stmt.close();
			conn.close();
			return jsons;

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return null;
		}
	}


	public void add(String table,String keyStr,String valStr) {
		String sql = "insert into "+table+" ( "+keyStr+" ) values ( " +valStr+ " )";
		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getPrinKey(String table){
		String sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME = '"
				+ table + "'";
		String keyword = "";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				keyword = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return keyword;
	}
	
	public void delById(String table,String prinKey,List list ) {
		String strCmd = "DELETE FROM "+ table+" WHERE "+prinKey+" IN "+list.toString().replace("[", "(").replace("]", ")");//DELETE FROM Person WHERE LastName = 'Wilson' 
		try {
		Statement stmt = conn.createStatement();
	    stmt.executeQuery(strCmd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updata(String table, Map map,String updateStr) {
	
		// 主键不可改
		String sql1 = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME = '"
				+ table + "'";
		String keyword = "";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql1);
			while (rs.next()) {
				keyword = rs.getString(1);
				//System.out.println("主键是："+keyword);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String[] value = (String[]) map.get(keyword);
		String sql = "update " + table + " set " + updateStr
				+ " where "+keyword+" = '"+value[0]+"'";
		try {
			//System.out.println("查询语句："+sql);
			Statement stmt = conn.createStatement();
			stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public int delete(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int save(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(BaseVO arg0) throws DAOException {
		// TODO Auto-generated method stub
		return 0;
	}
}

package api.rest.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import oracle.jdbc.OracleTypes;

public class DatabaseConnection {

	String IP = "jdbc:oracle:thin:@//10.5.3.205:1521/MERCURY";
	String username = "mercury",password="mercury123";
	Statement stmt=null;
	Connection con =null;
	public void createConnection()
	{
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			con=DriverManager.getConnection(  
					IP,username,password);    
			stmt=con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Object> executeQuery(String query,int colsize)
	{
		ArrayList<Object> arr = new ArrayList<Object>();
		try {
			ResultSet rs=stmt.executeQuery(query);  
			while(rs.next())  
			{
				for(int j =1;j<=colsize;j++)
				{
					try {
						if(rs.getObject(j)!=null)
						{
							arr.add(rs.getObject(j));
							System.out.println(rs.getObject(j));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	public ArrayList<Object> getColumnName(String tableName)
	{
		ArrayList<Object> colName = new ArrayList<Object>();
		try {
			DatabaseMetaData md =con.getMetaData();
			  ResultSet rsColumns = md.getColumns(null, null, tableName, null);
			    while (rsColumns.next()) {
			    	colName.add(rsColumns.getString("COLUMN_NAME"));
			      System.out.println(rsColumns.getString("COLUMN_NAME"));
			    }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return colName;
	}
	
	public int getColumnSize()
	{
		ArrayList<Object> colName = new ArrayList<Object>();
		try {
			DatabaseMetaData md =con.getMetaData();
			  ResultSet rsColumns = md.getColumns(null, null, "FUNCTIONAL_GROUPS", null);
			    while (rsColumns.next()) {
			    	colName.add(rsColumns.getString("COLUMN_NAME"));
			    }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return colName.size();
	}
	
	public void closeConnection()
	{
		try {
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void executeProcedure()
	{
		Connection dbConnection = null;
		CallableStatement callableStatement = null;
		String getDBUSERByUserIdSql = "{call PROC_VIEW_DETAILS( 1200004,   1400004,   1500146,   1000,   1600634,   1100001,   1,   1300006 ,  ?)}";
		try {
			dbConnection = con;
			callableStatement = dbConnection.prepareCall(getDBUSERByUserIdSql);
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			
			// execute getDBUSERByUserId store procedure
			callableStatement.execute();
			ResultSet result = (ResultSet)callableStatement.getObject(1);
			ResultSetMetaData data = result.getMetaData();
			while(result.next()){
				for (int i = 1 ; i <=data.getColumnCount() ; i ++)
				System.out.println(i+". "+result.getString(i));
				//break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Object> getDataBaseData(String query,int colSize)
	{
		createConnection();
		ArrayList<Object> arr = executeQuery(query,colSize);
		ArrayList<Object> arr1 = new ArrayList<Object>();
		closeConnection();
		ListMultimap<String, String> hm = ArrayListMultimap.create();
		for(int k=0;k<=arr.size()-1;k++)
		{
			try {
				hm.put("KeyData", arr.get(k).toString());
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		Set<String> s = hm.keySet();
		for(String k : s)
		{
			String[] sp = null;
			try {
				sp = hm.get(k).toString().split(",");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String spp = "";
			for(int i =0;i<=sp.length-1;i++)
			{
				try {
					spp = sp[i].replace("[", "").replace("]", "");
					arr1.add(spp);
				} catch (Exception e) {
				}
			}
		}
		return arr1;
	}
	
	
	
	public static void main(String args[]){  
		//"select * from FUNCTIONAL_GROUPS"
		DatabaseConnection obj = new DatabaseConnection();
		obj.createConnection();
		obj.executeQuery("select SUB_LOB_NAME from SUB_LOB",1);
		//obj.executeProcedure();
		//obj.getColumnName("SUB_LOB");
		//obj.getColumnSize();
		obj.closeConnection();

	}  
}

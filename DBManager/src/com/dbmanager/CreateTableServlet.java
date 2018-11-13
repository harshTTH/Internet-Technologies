package com.dbmanager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class CreateTableServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	Connection conn = null;
	Statement sm= null;
	private final String mysqlConnUrl = "jdbc:mysql://localhost:3306/DBMANAGER";
	private final String userName = "dbman";
	private final String pass = "123456";
	PrintWriter out = null;
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(mysqlConnUrl, userName, pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String tableName = req.getParameter("tabName");
		String colName[] = req.getParameterValues("colName");
		String colType[] = req.getParameterValues("colType");
		String colSize[] = req.getParameterValues("colSize");
		String primary[] = req.getParameterValues("primary");
		out = resp.getWriter();
	
		if(createTable(tableName,colName,colSize,colType,primary)) {
			out.print("<h4>Table "+tableName+" Created Successfully</h4>");
		}else out.print(false);
	}

	private boolean createTable(String tableName, String[] colName, String[] colSize, String[] colType,String[] primary){
		// TODO Auto-generated method stub
		if(tableName == null || colName == null || colSize == null || colType == null|| primary == null )return false;
		String query = "CREATE TABLE "+tableName;
		String cols = " ( ";
		for(int i = 0; i < colName.length; i++ ) {
			cols = cols + colName[i] + " " + colType[i] + "(" + colSize[i] + ")" + (!primary[i].equals("")?" "+primary[i]+" ":" ");
		}
		cols = cols.substring(0,cols.length()-1);
		cols += ")";
		query += cols + ";";
		
		try {
			sm = conn.createStatement();
			sm.execute(query);
		} catch (SQLException e) {
			return false;
		}
		System.out.println(query);
		return true;
	}
	

}

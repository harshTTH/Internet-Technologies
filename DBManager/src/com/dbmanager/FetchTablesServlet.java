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

public class FetchTablesServlet extends HttpServlet {
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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			sm = conn.createStatement();
			out = resp.getWriter();
			ResultSet rs = sm.executeQuery("SHOW TABLES;");
			String tables = "";
			while(rs.next()) {
				tables += rs.getString("Tables_in_DBMANAGER") + "-";
			}
			tables = tables.substring(0,tables.length()-1);
			out.print(tables);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

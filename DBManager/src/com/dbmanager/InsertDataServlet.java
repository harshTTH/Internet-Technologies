package com.dbmanager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InsertDataServlet
 */
@WebServlet("/InsertDataServlet")
public class InsertDataServlet extends HttpServlet {
	private static final long serialVersionUID = -2743177556305581614L;
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		out = response.getWriter();
		String[] rowData = request.getParameterValues("row");
		String tableName = request.getParameter("tableName");
		
		String query = "INSERT INTO "+tableName+" VALUES (";
		int noOfRows,noOfCol;
		try {
			sm = conn.createStatement();
			ResultSet rs = sm.executeQuery("SELECT * FROM "+ tableName);
			java.sql.ResultSetMetaData resultSetMetaData = rs.getMetaData();
			noOfCol = resultSetMetaData.getColumnCount();
			noOfRows = rowData.length/noOfCol;
			
			int k = 0;
			for(int i = 0;i<noOfRows;i++) {
				String[] temp = Arrays.copyOfRange(rowData, k, k +noOfCol );
				
				for(int col = 0;col<noOfCol;col++) {
					if(resultSetMetaData.getColumnTypeName(col+1).equals("VARCHAR"))temp[col] = "\""+temp[col]+"\"";
				}
				k = k + noOfCol;
				String values = String.join(",",temp );
				String tempQuery = query + values + ");";
				System.out.println(tempQuery);
				sm.execute(tempQuery);
			}
			out.print("<h3>Data Insertion Successful</h3>");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			out.print(false);
			e.printStackTrace();
		}
	}

}

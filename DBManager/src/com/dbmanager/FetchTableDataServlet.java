package com.dbmanager;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

/**
 * Servlet implementation class FetchTableDataServlet
 */
@WebServlet("/fetchTableData")
public class FetchTableDataServlet extends HttpServlet {

	private static final long serialVersionUID = 3603962041200462743L;

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
		String tableName = request.getParameter("tableName");
		out = response.getWriter();
		try {
			sm = conn.createStatement();
			ResultSet rs = sm.executeQuery("SELECT * FROM "+ tableName);
			java.sql.ResultSetMetaData resultSetMetaData = rs.getMetaData();
			int col = resultSetMetaData.getColumnCount();
			String colNames = "";
			for(int k = 0; k < col; k++) {
				colNames += resultSetMetaData.getColumnName(k+1) + "|";
			}
			colNames = colNames.substring(0,colNames.length()-1);
			out.println(colNames);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

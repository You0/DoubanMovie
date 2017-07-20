package com.douban.save;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.douban.spider.Movie;
import com.mysql.jdbc.Connection;

public class Save2DB {
	final static String uri = "jdbc:mysql://localhost:3306/new_jav";	
	static Connection connection;
	public static void save(String url, Movie entity) throws SQLException {
		if(connection == null){
			connection = (Connection) DriverManager.getConnection(uri,"root","9562");
		}
		
		
	}
}

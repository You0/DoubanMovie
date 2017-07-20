package com.douban.save;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.douban.spider.Movie;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class Save2DB {
	final static String uri = "jdbc:mysql://localhost:3306/new_douban";	
	static Connection connection;
	public static void save(String url, Movie entity) throws SQLException {
		if(connection == null ||connection.isClosed()){
			connection = (Connection) DriverManager.getConnection(uri,"root","9562");
		}
		
		if(entity==null){
			return;
		}
		
		PreparedStatement ps = (PreparedStatement) connection.prepareStatement(
				"INSERT into movie(title,type,rating,actor,cover,imgs,video,fh,company,series,director,time,title_fc,c_date) values (?, ?, ?,?,?,?,?,?,?,?,?,?,?,?)");

		ps.setObject(1, entity.getTitle());
		ps.setObject(2, entity.getType());
		ps.setObject(3, entity.getRating());
		ps.setObject(4, entity.getActor());
		ps.setObject(5, entity.getCover());
		ps.setObject(6, entity.getImgs());
		ps.setObject(7, entity.getVideo());
		ps.setObject(8, entity.getFh());
		ps.setObject(9, entity.getCompany());
		ps.setObject(10, entity.getSeries());
		ps.setObject(11, entity.getDirector());
		ps.setObject(12, entity.getTime());
		ps.setObject(13, "");
		ps.setObject(14, entity.getDate());

		//System.out.println(ps.toString());
		ps.execute();
		
		
	}
}

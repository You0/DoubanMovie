package com.douban.spider;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.douban.save.Save2DB;
import com.me.http.MapperCallBack;
import com.me.http.MapperCallBack.Parse;
import com.me.http.MapperCallBack.Save;
import com.me.spider.Crawl;
import com.me.utils.SpiderUtil;
import com.mysql.jdbc.Connection;

public class Example {
	
	public static void main(String[] args) throws ClassNotFoundException {
			
		Class.forName("com.mysql.jdbc.Driver");		
		
		
		
		
		
		
		
		
		// 使用案例

		Crawl crawl = SpiderUtil.Steup();

		// 这里传入正则，比如我要只想要爬取煎蛋网则可以这样写：
		crawl.setMatchRegex("https://movie.douban.com/.*");
		crawl.setBlackReg(new String[]{"ticket"});
		// 这里传入起始url，将这些url当做起始点，BFS广度搜索进行发散爬取
		crawl.setStartUrls(new String[] { "https://movie.douban.com/" });

		crawl.setSleepTime(1);
		
		// 这里的范型bean是自己创建的，随意什么类型主要是方便你自己把html解析之后序列化成java对象，然后存入数据库用的
		MapperCallBack<Movie> callBack = SpiderUtil.getMapperCallBack();
		// callback预留的接口，在这里自己编写解析数据的过程
		callBack.setParseListener(new Parse<Movie>() {
			public Movie parse(String url, String body) {
				return com.douban.parse.Parse.parse(url, body);
			}
		});

		// callback预留的接口，在这里自己编写数据持久化的过程
		callBack.setSaveListener(new Save<Movie>() {
			@Override
			public void save(String url, Movie entity) {
				// TODO Auto-generated method stub
				try {
					Save2DB.save(url,entity);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		// 把接口设置进去
		crawl.setListener(callBack);

		// 好了配置完成愉快的等爬取完成吧！
		crawl.start();

	}
}

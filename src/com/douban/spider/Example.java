package com.douban.spider;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;

import org.springframework.web.context.request.NativeWebRequest;

import com.douban.save.Save2DB;
import com.me.http.MapperCallBack;
import com.me.http.MapperCallBack.Parse;
import com.me.http.MapperCallBack.Save;
import com.me.redis.RedisPool;
import com.me.spider.Crawl;
import com.me.utils.SpiderUtil;
import com.mysql.jdbc.Connection;

public class Example {
	
	public static void main(String[] args) throws ClassNotFoundException {
			
		Class.forName("com.mysql.jdbc.Driver");		
		
		
		
		
		
		
		
		
		// 使用案例
		RedisPool.SetRedisServer("115.159.159.65", "6379");
		Crawl crawl = SpiderUtil.Steup();

		
		
		// 这里传入正则，比如我要只想要爬取煎蛋网则可以这样写：
		crawl.setMatchRegex("https://movie.douban.com/.*");
		crawl.setBlackReg(new String[]{"ticket","celebrity","photo",""});
		// 这里传入起始url，将这些url当做起始点，BFS广度搜索进行发散爬取
		crawl.setStartUrls(new String[] { "https://movie.douban.com/" });

		crawl.setSleepTime(2);
		HashMap<String, String> header = new HashMap<>();
		//header.put("Cookie", "bid=CsFhteC2At4; gr_user_id=e39f8f4d-b232-4106-9111-95cb217115be; viewed=\"26652876\"; ll=\"118183\"; td_cookie=18446744072389879053; __yadk_uid=JCiwQ4UMx4u0SVgKciMrddR7WJW0kQFY; ct=y; ap=1; ps=y; __utma=30149280.1074924522.1496306336.1500599905.1500606936.11; __utmc=30149280; __utmz=30149280.1500606936.11.9.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/accounts/login; __utma=223695111.537922636.1498525514.1500599905.1500606936.6; __utmc=223695111; __utmz=223695111.1500606936.6.5.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/accounts/login; _vwo_uuid_v2=1BEE92F2000C3B3B88878BFCEDC843B0|f0cf6b8f4d607a086f1f9bbcd59f8e1d; ue=\"1109245765@qq.com\"; dbcl2=\"88174845:yS96wfr11/s\"; ck=Lbn4; _pk_ref.100001.4cf6=%5B%22%22%2C%22%22%2C1500616728%2C%22https%3A%2F%2Fwww.douban.com%2Faccounts%2Flogin%3Fredir%3Dhttps%253A%2F%2Fmovie.douban.com%2Fticket%2F292210967%2F%22%5D; _pk_id.100001.4cf6=da249ea7a99271d5.1498525516.10.1500616734.1500614911.; _pk_ses.100001.4cf6=*; push_noty_num=0; push_doumail_num=0; __ads_session=K4D6pnHC8Qj8h84G6wA=");
		crawl.setHeaders(header);
		// 这里的范型bean是自己创建的，随意什么类型主要是方便你自己把html解析之后序列化成java对象，然后存入数据库用的
		MapperCallBack<Movie> callBack = SpiderUtil.getMapperCallBack();
		// callback预留的接口，在这里自己编写解析数据的过程
		callBack.setParseListener(new Parse<Movie>() {
			public Movie parse(String url, String body) {
				Movie movie = null;
				try {
					movie = com.douban.parse.Parse.parse(url, body);
				} catch (ParseException e) {
					//e.printStackTrace();
				}
				return movie;
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
					//e.printStackTrace();
				}
			}
		});
		// 把接口设置进去
		crawl.setListener(callBack);

		// 好了配置完成愉快的等爬取完成吧！
		crawl.start();

	}
}

package com.douban.parse;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.text.TextAction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.douban.spider.Movie;

public class Parse {
	public static Movie parse(String url, String body) throws ParseException {
		Movie movie = new Movie();
		
		// 如果是电影的页面则对他进行爬取
		if (url.contains("subject")) {
			System.out.println("解析");
			
			
			
			
			Document doc = Jsoup.parse(body);
			
			Elements elements = doc.getElementsByClass("subjectwrap");
			
			Elements bg = doc.getElementsByClass("nbgnbg");
			
			String cover = "";
			for(Element b:bg){
				//System.out.println(b.toString());
				 Elements imgs=b.getElementsByTag("img");
			        for(Element element : imgs) {
			            String imgSrc=element.attr("src");
			            cover = imgSrc;
			        }
				
			}
			
			
			
			for (Element e : elements) {
				//System.out.println(e.text());
				String info = e.text();
				int index= info.indexOf("编剧");
				String direct = info.substring(4, index);
				info = info.substring(index);
				index = info.indexOf("主演");
				String bz = info.substring(4, index);
				info = info.substring(index);
				index = info.indexOf("类型");
				String actor = info.substring(4, index);
				info = info.substring(index);
				index = info.indexOf("制片国家/地区");
				String type = info.substring(4, index);
				info = info.substring(index);
				index = info.indexOf("上映日期");
				String company = info.substring(9, index);
				info = info.substring(index);
				index = info.indexOf("片长");
				String c_date = info.substring(6, index);
				info = info.substring(index);
				index = info.indexOf("又名");
				String time = info.substring(4, index);
				info = info.substring(index);
				index = info.indexOf("IMDb链接");
				info = info.substring(index);
				index = info.indexOf("豆瓣评分");
				String fh = info.substring(8, index);
				info = info.substring(index);
				
				direct = split(direct);
				bz = split(bz);
				actor = split(actor);
				type = split(type);
				company = split(company);
				
				index = c_date.indexOf("(");
				
				c_date = c_date.substring(0,index);
				time = split(time);
				fh = split(fh);
				info = info.substring(8,11);
				Float rating = Float.valueOf(info);
				
				System.out.println(direct);
				System.out.println(bz);
				System.out.println(actor);
				System.out.println(type);
				System.out.println(company);
				System.out.println(c_date);
				System.out.println(time);
				System.out.println(fh);
				System.out.println(rating);
				
				
				movie.setActor(actor);
				movie.setCompany(company);
				movie.setCover(cover);
				SimpleDateFormat simpleDateFormat;
				if(c_date.length()==10){
					simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				}else{
					simpleDateFormat = new SimpleDateFormat("yyyy-MM");
				}
				movie.setDate(simpleDateFormat.parse(c_date));
				movie.setDirector(direct);
				movie.setFh(fh);
				movie.setTime(time);
				movie.setRating(rating);
				movie.setType(type);
				
				
			}
			String imgs="";
			String video="";
			
			elements = doc.getElementsByClass("related-pic-bd");
			for (Element e : elements) {
				Elements href = e.getElementsByAttribute("href");
				int i=0;
				for(Element h:href){
					String text = h.toString();
					if(i==0){
						i++;
						video = "http://movie.douban.com/trailer/video_url?tid="+text.substring(68,74);

					}else{

						imgs += "https://img3.doubanio.com/view/photo/photo/public/p"+ text.substring(47,57)+".jpg ";
					}
					
				}
				
			}
			System.out.println(video);
			System.out.println(imgs);
			
			
			
			
			
		}
		return null;
	}
	
	
	
	private static String split(String str){
		String[] strings = str.split("/");
		String strs="";
		for(int i=0;i<strings.length;i++){
			strs += strings[i].substring(0,strings[i].length()-1);
		}
		return strs;
	}
	
	
	
}
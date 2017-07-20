package com.douban.parse;

import javax.swing.text.TextAction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.douban.spider.Movie;

public class Parse {
	public static Movie parse(String url, String body) {
		// 如果是电影的页面则对他进行爬取
		if (url.contains("subject")) {
			System.out.println("解析");
			Document doc = Jsoup.parse(body);
			Elements elements = doc.getElementsByClass("subjectwrap");
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
				
				
				
				System.out.println(direct);
				System.out.println(bz);
				System.out.println(actor);
				System.out.println(type);
				System.out.println(company);
				System.out.println(c_date);
				System.out.println(time);
				System.out.println(fh);
				System.out.println(info);
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
}
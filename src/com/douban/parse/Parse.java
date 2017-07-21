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
		
		// 如果是电影的页面则对他进行爬取
		if (url.contains("subject")&&!url.contains("cinema")) {
			
			Movie movie = new Movie();

			// System.out.println(body);

			Document doc = Jsoup.parse(body);

			Elements elements = doc.getElementsByClass("subjectwrap");

			Elements bg = doc.getElementsByClass("nbgnbg");

			String cover = "";
			for (Element b : bg) {
				// System.out.println(b.toString());
				Elements imgs = b.getElementsByTag("img");
				for (Element element : imgs) {
					String imgSrc = element.attr("src");
					cover = imgSrc;
				}

			}

			for (Element e : elements) {
				// System.out.println(e.text());
				String info = e.text();
				
//				System.out.println(info);
//				System.out.println(url);
				String direct = "";
				String bz = "";
				String actor = "";
				String type = "";
				String company = "";
				String c_date = "";
				String time = "";
				String fh = "";

				
				int index = info.indexOf("编剧");
				if (index != -1) {
					if(info.contains("更新描述")){
						direct = info.substring(12, index);
					}else{
						direct = info.substring(4,index);
					}
					 
					info = info.substring(index);
				}

				index = info.indexOf("主演");
				if (index != -1) {
					if(info.indexOf("编剧")==-1){
						if(info.contains("更新描述")){
							direct = info.substring(12, index);
						}else{
							direct = info.substring(4,index);
						}
					}else{
						bz = info.substring(4, index);
					}
					 
					info = info.substring(index);
				}

				
				index = info.indexOf("类型");

				if (index != -1) {
					 actor = info.substring(4, index);
					info = info.substring(index);
				}
				
				index = info.indexOf("制片国家/地区");

				if (index != -1) {
					 type = info.substring(4, index);
					info = info.substring(index);
				}
				
				index = info.indexOf("上映日期");
				if (index != -1) {
					 company = info.substring(9, index);
					info = info.substring(index);
				}
				
				index = info.indexOf("片长");
				if (index != -1) {
					 c_date = info.substring(6, index);
					info = info.substring(index);
				}
				
				index = info.indexOf("又名");
				if (index != -1) {
					 time = info.substring(4, index);
					info = info.substring(index);
				}
				
				index = info.indexOf("IMDb链接");
				if (index != -1) {
					info = info.substring(index);
					index = info.indexOf("豆瓣评分");
				}
				
				if (index != -1) {
					 fh = info.substring(8, index);
					info = info.substring(index);
				}
				
				info = info.substring(8, 11);
//				 System.out.println("导演:"+direct);
//				 System.out.println("编剧:"+bz);
//				 System.out.println("演员:"+actor);
//				 System.out.println("类型:"+type);
//				 System.out.println("公司："+company);
//				 System.out.println("上映日期:"+c_date);
//				 System.out.println("长度:"+time);
//				 System.out.println(fh);
//				 System.out.println(info);
				

				direct = split(direct);
				bz = split(bz);
				actor = split(actor);
				type = split(type);
				company = split(company);
				try{
					index = c_date.indexOf("(");
				}catch (Exception e2) {
					// TODO: handle exception
				}
				index = c_date.length();

				c_date = c_date.substring(0, index);
				time = split(time);
				fh = split(fh);
				Float rating;
				try{
					 rating = Float.valueOf(info);
				}catch (Exception ee) {
					// TODO: handle exception
					rating = 0f;
				}
				

				

				movie.setActor(actor);
				movie.setCompany(company);
				movie.setCover(cover);
				SimpleDateFormat simpleDateFormat;
				if (c_date.length() == 10) {
					simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				} else {
					simpleDateFormat = new SimpleDateFormat("yyyy-MM");
				}
				movie.setDate(simpleDateFormat.parse(c_date));
				movie.setDirector(direct);
				movie.setFh(fh);
				movie.setTime(time);
				movie.setRating(rating);
				movie.setType(type);

			}
			String imgs = "";
			String video = "";

			elements = doc.getElementsByClass("related-pic-bd");
			for (Element e : elements) {
				Elements href = e.getElementsByAttribute("href");
				int i = 0;
				for (Element h : href) {
					String text = h.toString();
					if (i == 0) {
						i++;
						video = "http://movie.douban.com/trailer/video_url?tid=" + text.substring(68, 74);

					} else {

						imgs += "https://img3.doubanio.com/view/photo/photo/public/p" + text.substring(47, 57)
								+ ".jpg ";
					}

				}

			}

			movie.setVideo(video);
			movie.setImgs(imgs);

			// System.out.println(video);
			// System.out.println(imgs);

			// v:itemreviewed
			String title1 = "";
			Elements titile = doc.getElementsByAttributeValue("property", "v:itemreviewed");
			for (Element e : titile) {
				title1 = e.text();
			}
			movie.setTitle(title1);

			String summary = "";
			Elements summary1 = doc.getElementsByAttributeValue("property", "v:summary");
			for (Element e : summary1) {
				summary = e.text();
			}

//			System.out.println(summary);
			movie.setSeries(summary);
			return movie;
		}
		return null;

	}

	private static String split(String str) {
		if(str==null){
			return "";
		}
		String[] strings = str.split("/");
		String strs = "";
		try{
			for (int i = 0; i < strings.length; i++) {
				strs += strings[i].substring(0, strings[i].length() - 1);
			}
		}catch (Exception e) {
			strs="";
		}
		
		return strs;
	}

}
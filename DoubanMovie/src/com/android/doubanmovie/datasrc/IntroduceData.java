package com.android.doubanmovie.datasrc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IntroduceData extends Data {
	public IntroduceData() {
		rating = new HashMap<String, String>();
		popular_comments = new ArrayList<Map<String, String>>();
		writers = new ArrayList<Map<String, String>>();
		casts = new ArrayList<Map<String, String>>();
		directors = new ArrayList<Map<String, String>>();
	}

	// 星级，评分
	public Map<String, String> rating;
	// 电影名称
	public String title;
	// 大陆上映日期
	public String mainland_pubdate;
	// 图片，该处决定取中图
	public String images;
	// 短评信息，需要取的内容有：rating中的value
	// author中的name,具体的内容
	// content,有用数 useful_count
	public List<Map<String, String>> popular_comments;
	// 影人信息，里面需要取的数据为：
	// name.name_en.avatars头像取中图
	public List<Map<String, String>> writers;
	// 电影类型
	public String genres;
	// 预告片URL，只取了一个
	public String trailer_urls;
	// 制作国家
	public String countries;
	// 主角，取name,name_en,avatars
	public List<Map<String, String>> casts;
	// 简介
	public String summary;
	// 导演,取name,name_en,avatars
	public List<Map<String, String>> directors;
	// 评分人数
	public long ratings_count;

	@Override
	public String toString() {
		return "IntroduceData [rating=" + rating + ", title=" + title
				+ ", mainland_pubdate=" + mainland_pubdate + ", images="
				+ images + ", popular_comments=" + popular_comments
				+ ", writers=" + writers + ", genres=" + genres
				+ ", trailer_urls=" + trailer_urls + ", countries=" + countries
				+ ", casts=" + casts + ", summary=" + summary + ", directors="
				+ directors + ", ratings_count=" + ratings_count + "]";
	}

}

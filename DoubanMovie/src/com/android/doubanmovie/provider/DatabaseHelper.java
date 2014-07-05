package com.android.doubanmovie.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "mydb.db";
	private static final int VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	// // 星级，评分
	// public Map<String, String> rating;
	// // 电影名称
	// public String title;
	// // 大陆上映日期
	// public String mainland_pubdate;
	// // 图片，该处决定取中图
	// public String images;
	// // 短评信息，需要取的内容有：rating中的value
	// // author中的name,具体的内容
	// // content3样
	// public List<Map<String, String>> popular_comments;
	// // 影人信息，里面需要取的数据为：
	// // name.name_en.avatars头像取中图
	// public List<Map<String, String>> writers;
	// // 电影类型
	// public String genres;
	// // 预告片URL，只取了一个
	// public String trailer_urls;
	// // 制作国家
	// public String countries;
	// // 主角，取name,name_en,avatars
	// public List<Map<String, String>> casts;
	// // 简介
	// public String summary;
	// // 导演,取name,name_en,avatars
	// public List<Map<String, String>> directors;
	// // 评分人数
	// public long ratings_count;
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 主界面的表，用来存放主界面的数据
		String SQLInitShow = "create table show (_id integer primary key autoincrement,"
				+ "title varchar(64),"
				+ "average varchar(64),"
				+ "stars varchar(64),"
				+ "small varchar(64),"
				+ "id varchar(32))";
		String SQLInitIntroduce = "create table introduce (_id integer primary key autoincrement,"
				+ "stars varchar(10),"
				+ "average varchar(20),"
				+ "ratings_count varchar(20),"
				+ "mainland_pubdate varchar(40),"
				+ "genres varchar(40),"
				+ "countries varchar(40),"
				+ "summary varchar(400),"
				+ "images varchar(100)," + "connection integer)";
		String SQLInitWriters = "create table writers(_id integer primary key autoincrement,"
				+ "connection integer,"
				+ "name varchar(40),"
				+ "name_en varchar(40)," + "avatars varchar(100))";
		String SQLInitCasts = "create table casts(_id integer primary key autoincrement,"
				+ "connection integer,"
				+ "name varchar(40),"
				+ "name_en varchar(40)," + "avatars varchar(100))";
		String SQLInitdirectors = "create table directors(_id integer primary key autoincrement,"
				+ "connection integer,"
				+ "name varchar(40),"
				+ "name_en varchar(40)," + "avatars varchar(100))";
		String SQLInitPopular_comments = "create table comments(_id integer primary key autoincrement,"
				+ "connection integer,"
				+ "name varchar(40),"
				+ "value varchar(40),"
				+ "useful_count varchar(20),"
				+ "content varchar(500))";
		db.execSQL(SQLInitShow);
		db.execSQL(SQLInitIntroduce);
		db.execSQL(SQLInitWriters);
		db.execSQL(SQLInitCasts);
		db.execSQL(SQLInitdirectors);
		db.execSQL(SQLInitPopular_comments);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}

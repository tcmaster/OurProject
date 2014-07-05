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

	// // �Ǽ�������
	// public Map<String, String> rating;
	// // ��Ӱ����
	// public String title;
	// // ��½��ӳ����
	// public String mainland_pubdate;
	// // ͼƬ���ô�����ȡ��ͼ
	// public String images;
	// // ������Ϣ����Ҫȡ�������У�rating�е�value
	// // author�е�name,���������
	// // content3��
	// public List<Map<String, String>> popular_comments;
	// // Ӱ����Ϣ��������Ҫȡ������Ϊ��
	// // name.name_en.avatarsͷ��ȡ��ͼ
	// public List<Map<String, String>> writers;
	// // ��Ӱ����
	// public String genres;
	// // Ԥ��ƬURL��ֻȡ��һ��
	// public String trailer_urls;
	// // ��������
	// public String countries;
	// // ���ǣ�ȡname,name_en,avatars
	// public List<Map<String, String>> casts;
	// // ���
	// public String summary;
	// // ����,ȡname,name_en,avatars
	// public List<Map<String, String>> directors;
	// // ��������
	// public long ratings_count;
	@Override
	public void onCreate(SQLiteDatabase db) {
		// ������ı�������������������
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

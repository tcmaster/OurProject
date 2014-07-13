package com.android.joocola.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String DB_NAME = "joocola.db";
	public static final int VERSION = 1;
	private static DatabaseHelper dBHelper;

	/**
	 * 返回DatabaseHelper实例
	 */
	public static synchronized DatabaseHelper getDatabaseHelper(Context context) {
		if (dBHelper == null) {
			dBHelper = new DatabaseHelper(context);
		}
		return dBHelper;
	}

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}

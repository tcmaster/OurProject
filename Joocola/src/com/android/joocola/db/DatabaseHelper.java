package com.android.joocola.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.joocola.dbtable.BaseDatainfoTable;

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
		creatBaseDataTable(db);
	}

	private void creatBaseDataTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + BaseDatainfoTable.TABLENAME + " ("
				+ BaseDatainfoTable.BASEDATA_PID + " INTEGER PRIMARY KEY,"
				+ BaseDatainfoTable.BASEDATA_ITEMNAME + " TEXT,"
				+ BaseDatainfoTable.BASEDATA_TypeName + " TEXT,"
				+ BaseDatainfoTable.BASEDATA_SORTNO + " INTEGER" + ");";
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}

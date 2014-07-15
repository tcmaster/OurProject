package com.android.joocola.dbmanger;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.joocola.db.DatabaseHelper;
import com.android.joocola.dbtable.BaseDatainfoTable;
import com.android.joocola.entity.BaseDataInfo;

public class BaseDataInfoManger {
	private static BaseDataInfoManger mInstance;
	private DatabaseHelper mDbHelper;

	public BaseDataInfoManger(Context ctx) {
		mDbHelper = DatabaseHelper.getDatabaseHelper(ctx);
	}

	public static BaseDataInfoManger getBaseDataInfoManger(Context ctx) {
		if (mInstance == null) {
			synchronized (BaseDataInfoManger.class) {
				if (mInstance == null)
					mInstance = new BaseDataInfoManger(ctx);
			}
		}
		return mInstance;
	}

	/*
	 * 返回所有数据
	 */
	public ArrayList<BaseDataInfo> getAllChannel() {
		Cursor all = getAllStored();
		ArrayList<BaseDataInfo> arrayList = new ArrayList<BaseDataInfo>();
		while (all.moveToNext()) {
			BaseDataInfo mBaseDataInfo = new BaseDataInfo();
			mBaseDataInfo.setPID(all.getInt(all
					.getColumnIndex(BaseDatainfoTable.BASEDATA_PID)));
			mBaseDataInfo.setSortNo(all.getInt(all
					.getColumnIndex(BaseDatainfoTable.BASEDATA_SORTNO)));
			mBaseDataInfo.setItemName(all.getString(all
					.getColumnIndex(BaseDatainfoTable.BASEDATA_ITEMNAME)));
			mBaseDataInfo.setTypeName(all.getString(all
					.getColumnIndex(BaseDatainfoTable.BASEDATA_TypeName)));
			arrayList.add(mBaseDataInfo);
		}

		return arrayList;
	}

	public void saveAllChannel(ArrayList<BaseDataInfo> baseDatainfos) {

		if (baseDatainfos.size() != 0)
			for (int i = 0; i < baseDatainfos.size(); i++) {
				BaseDataInfo baseDataInfo = baseDatainfos.get(i);
				add(baseDataInfo);
			}

	}
	private void add(BaseDataInfo mBaseDataInfo) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		String sql = "insert into basedata(pid,SortNo,ItemName,TypeName) VALUES(?,?,?,?);";
		db.execSQL(
				sql,
				new Object[] { mBaseDataInfo.getPID(),
						mBaseDataInfo.getSortNo(), mBaseDataInfo.getItemName(),
						mBaseDataInfo.getTypeName() });

	}
	private Cursor getAllStored() {
		return mDbHelper.getWritableDatabase()
				.query(BaseDatainfoTable.TABLENAME, null, null, null, null,
						null, null);
	}

	public void clearTable() {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		String clearSql = "DELETE FROM basedata";
		db.execSQL(clearSql);

	}
}

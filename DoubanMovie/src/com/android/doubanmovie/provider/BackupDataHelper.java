package com.android.doubanmovie.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.android.doubanmovie.datasrc.Data;
import com.android.doubanmovie.datasrc.IntroduceData;
import com.android.doubanmovie.datasrc.ShowData;

//该类帮助备份结构化的文本数据以及图片数据
public class BackupDataHelper {
	/**
	 * @return true 表示备份已经成功，false代表数据已经存在，暂时无需备份
	 */
	@SuppressWarnings("unchecked")
	public static boolean BackupData(Data data, Context context, Uri url) {
		// 备份之前，删掉所有旧数据
		context.getContentResolver().delete(url, null, null);
		if (data instanceof ShowData) {
			ShowData sdata = (ShowData) data;
			List<Map<String, Object>> subjects = sdata.getSubjects();
			ContentValues values = new ContentValues();
			for (int i = 0; i < subjects.size(); i++) {
				values.put("title", (String) subjects.get(i).get("title"));
				values.put("average", ((Map<String, String>) subjects.get(i)
						.get("rating")).get("average"));
				values.put("stars",
						((Map<String, String>) subjects.get(i).get("rating"))
								.get("stars"));
				String imgString = ((Map<String, String>) subjects.get(i).get(
						"images")).get("small");
				values.put("small", imgString);
				values.put("id", (String) subjects.get(i).get("id"));
				// 将文本内容存入数据库
				context.getContentResolver().insert(url, values);
			}
			return true;
		} else if (data instanceof IntroduceData) {
			// IntroduceData iData = (IntroduceData) data;
			return true;
		}
		return false;
	}

	// 从数据库中取出Show表备份的文本数据
	public static ShowData getShowData(Context context, Uri url) {
		ShowData data = new ShowData();
		data.setSubjects();
		List<Map<String, Object>> subjects = data.getSubjects();
		Cursor cursor = context.getContentResolver().query(url, null, null,
				null, null);
		if (cursor == null || cursor.getCount() < 1) {
			cursor.close();
			return null;
		}
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", cursor.getString(cursor.getColumnIndex("title")));
			Map<String, String> map2 = new HashMap<String, String>();
			map2.put("average",
					cursor.getString(cursor.getColumnIndex("average")));
			map2.put("stars", cursor.getString(cursor.getColumnIndex("stars")));
			map.put("rating", map2);
			Map<String, String> map3 = new HashMap<String, String>();
			map3.put("small", cursor.getString(cursor.getColumnIndex("small")));
			map.put("images", map3);
			map.put("id", cursor.getString(cursor.getColumnIndex("id")));
			subjects.add(map);
		}
		cursor.close();
		return data;
	}
}

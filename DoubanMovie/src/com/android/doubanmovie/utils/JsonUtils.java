package com.android.doubanmovie.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.doubanmovie.datasrc.ImageData;
import com.android.doubanmovie.datasrc.IntroduceData;
import com.android.doubanmovie.datasrc.ShowData;

public class JsonUtils {
	public static ShowData parseShowJSON(String jsonStr) {
		ShowData data = new ShowData();
		data.setSubjects();
		Log.v("test", jsonStr);
		try {
			JSONObject object1 = new JSONObject(jsonStr);
			data.setCount(object1.getInt("count"));
			data.setStart(object1.getInt("start"));
			data.setTotal(object1.getInt("total"));
			JSONArray array1 = object1.getJSONArray("subjects");
			for (int i = 0; i < array1.length(); i++) {
				JSONObject object2 = array1.getJSONObject(i);
				Map<String, Object> map1 = new HashMap<String, Object>();
				JSONObject object3 = object2.getJSONObject("rating");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("max", object3.getString("max"));
				map2.put("average", object3.getString("average"));
				map2.put("stars", object3.getString("stars"));
				map2.put("min", object3.getString("min"));
				map1.put("rating", map2);
				map1.put("title", object2.getString("title"));
				map1.put("mainland_pubdate",
						object2.getString("mainland_pubdate"));
				JSONObject object4 = object2.getJSONObject("images");
				Map<String, String> map3 = new HashMap<String, String>();
				map3.put("small", object4.getString("small"));
				map3.put("large", object4.getString("large"));
				map3.put("medium", object4.getString("medium"));
				map1.put("images", map3);
				map1.put("collect_count", object2.getString("collect_count"));
				map1.put("original_title", object2.getString("original_title"));
				map1.put("subtype", object2.getString("subtype"));
				map1.put("year", object2.getString("year"));
				JSONArray array2 = object2.getJSONArray("pubdates");
				List<String> list = new ArrayList<String>();
				for (int j = 0; j < array2.length(); j++) {
					list.add(array2.getString(j));
				}
				map1.put("pubdates", list);
				map1.put("alt", object2.getString("alt"));
				map1.put("id", object2.getString("id"));
				data.getSubjects().add(map1);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static IntroduceData parseIntroduceJSON(String jsonStr) {
		IntroduceData data = new IntroduceData();
		try {
			JSONObject object1 = new JSONObject(jsonStr);
			JSONObject ratingObject = object1.getJSONObject("rating");
			data.rating.put("max", ratingObject.getString("max"));
			data.rating.put("average", ratingObject.getString("average"));
			data.rating.put("stars", ratingObject.getString("stars"));
			data.rating.put("min", ratingObject.getString("min"));
			data.title = object1.getString("title");
			data.mainland_pubdate = object1.getString("mainland_pubdate");
			data.images = object1.getJSONObject("images").getString("medium");
			JSONArray popular = object1.getJSONArray("popular_comments");
			for (int i = 0; i < popular.length(); i++) {
				JSONObject object2 = popular.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("value",
						object2.getJSONObject("rating").getString("value"));
				map.put("name",
						object2.getJSONObject("author").getString("name"));
				map.put("content", object2.getString("content"));
				map.put("useful_count", object2.getString("useful_count"));
				data.popular_comments.add(map);
			}
			JSONArray writers = object1.getJSONArray("writers");
			for (int i = 0; i < writers.length(); i++) {
				JSONObject object2 = writers.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", object2.getString("name") + "(±à¾ç)");
				map.put("name_en", object2.getString("name_en"));
				map.put("avatars",
						object2.getJSONObject("avatars").getString("medium"));
				data.writers.add(map);
			}
			JSONArray genres = object1.getJSONArray("genres");
			data.genres = "";
			for (int i = 0; i < genres.length(); i++) {
				data.genres += genres.getString(i);
				if (i < genres.length() - 1) {
					data.genres += "/";
				}
			}
			data.trailer_urls = object1.getJSONArray("trailer_urls").getString(
					0);
			data.countries = object1.getJSONArray("countries").getString(0);
			JSONArray casts = object1.getJSONArray("casts");
			for (int i = 0; i < casts.length(); i++) {
				JSONObject object2 = casts.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", object2.getString("name") + "(Ö÷ÑÝ)");
				map.put("name_en", object2.getString("name_en"));
				map.put("avatars",
						object2.getJSONObject("avatars").getString("medium"));
				data.casts.add(map);
			}
			data.summary = object1.getString("summary");
			JSONArray directors = object1.getJSONArray("directors");
			for (int i = 0; i < directors.length(); i++) {
				JSONObject object2 = directors.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", object2.getString("name") + "(µ¼ÑÝ)");
				map.put("name_en", object2.getString("name_en"));
				map.put("avatars",
						object2.getJSONObject("avatars").getString("medium"));
				data.directors.add(map);
			}
			data.ratings_count = object1.getLong("ratings_count");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static List<ImageData> parseImageJSON(JSONObject jsonObject) {
		List<ImageData> datas = new ArrayList<ImageData>();
		try {
			JSONArray array = jsonObject.getJSONArray("photos");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				ImageData data = new ImageData();
				data.album_id = object.getString("album_id");
				data.desc = object.getString("desc");
				data.icon = object.getString("icon");
				data.id = object.getString("id");
				data.image = object.getString("image");
				data.next_photo = object.getString("next_photo");
				data.prev_photo = object.getString("prev_photo");
				data.thumb = object.getString("thumb");
				datas.add(data);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datas;
	}

	public static List<Map<String, String>> parseSCJSON(String jsonStr) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		try {
			JSONArray array = new JSONObject(jsonStr).getJSONArray("comments");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("useful_count", "" + object.getInt("useful_count"));
				map.put("value",
						"" + object.getJSONObject("rating").getInt("value"));
				map.put("name", object.getJSONObject("author")
						.getString("name"));
				map.put("content", object.getString("content"));
				data.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static List<Map<String, String>> parseCDJSON(String jsonStr) {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		try {
			JSONArray array = new JSONObject(jsonStr).getJSONArray("reviews");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("value",
						object.getJSONObject("rating").getString("value"));
				map.put("useful_count", object.getString("useful_count"));
				map.put("title", object.getString("title"));
				map.put("created_at", object.getString("created_at"));
				map.put("name", object.getJSONObject("author")
						.getString("name"));
				map.put("summary", object.getString("summary"));
				map.put("content", object.getString("content"));
				data.add(map);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
}

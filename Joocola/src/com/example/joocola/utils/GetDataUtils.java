package com.example.joocola.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

/*
 * 用 get 方式请求
 */
public class GetDataUtils {
	protected static final String url_a = "http://byw00031.my3w.com/Controller/";
	protected static final String md5 = "sign";

	// http://192.168.1.104:8099/Controller/Sys.UserController.AppLogon.ashx?pwd=amituofo&userName=admin&sign=d4e0512026d993af4803c16c27b7eb17
	public static void doGetData(String url, HashMap<String, String> mHashMap,
			RequestQueue queue) {
		// 给mHashMap 按key排序
		Object[] key = mHashMap.keySet().toArray();
		Arrays.sort(key);
		String md5String = "";
		for (int i = 0; i < key.length; i++) {
			md5String = md5String + key[i].toString() + "="
					+ mHashMap.get(key[i].toString());
			if (i != key.length - 1) {
				md5String += "&";
			}
		}
		md5String = MD5Utils.md5s(md5String);
		Log.e("~~~~", md5String);
		mHashMap.put(md5, md5String);

		// 开始正式调用
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(url_a);
		urlBuilder.append(url);
		urlBuilder.append("?");
		Iterator iter = mHashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			urlBuilder.append(entry.getKey().toString());
			urlBuilder.append("=");
			urlBuilder.append(entry.getValue().toString());
			urlBuilder.append("&");
		}
		urlBuilder.deleteCharAt(urlBuilder.length() - 1);
		Log.e("get的地址", urlBuilder.toString());
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET,
				urlBuilder.toString(), null, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						Log.e("Get方式", arg0.toString());
					}
				}, null);
		queue.add(jsonObjectRequest);
	}

	public static HashMap<String, String> getParmas() {
		return new HashMap<String, String>();
	}
}

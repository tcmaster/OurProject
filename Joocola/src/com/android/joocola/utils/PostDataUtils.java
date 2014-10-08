package com.android.joocola.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/*
 * Volley post
 */
public class PostDataUtils {
	private HashMap<String, String> map = new HashMap<String, String>();

	public void addParams(String key, String value) {
		map.put(key, value);
	}

	public void postNewRequest(String url, RequestQueue queue,
			final VolleyPostCallBack volleyPostCallBack) {
		StringRequest sr = new StringRequest(Request.Method.POST,
				Constants.MAIN_URL + url, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						JSONObject jsonObject;

						try {
							jsonObject = new JSONObject(response);
							boolean flag = jsonObject.getBoolean("result");
							if (flag) {
								volleyPostCallBack
										.VolleyPostResolveData(jsonObject
												.getString("data"));
							} else {
								Log.e("VolleyPost", "出错");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("Post error", error.toString());
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				String md5String = "";
				Object[] key = map.keySet().toArray();
				Arrays.sort(key);
				for (int i = 0; i < key.length; i++) {
					md5String = md5String + key[i].toString() + "="
							+ map.get(key[i].toString());
					if (i != key.length - 1) {
						md5String += "&";
					}
				}
				md5String = MD5Utils.md5s(md5String);
				map.put("sign", md5String);
				return map;
			}

		};
		queue.add(sr);
	}

	public interface VolleyPostCallBack {
		public void VolleyPostResolveData(String result);
	}
}

package com.android.joocola.utils;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

/*
 * Volley post
 */
public class PostDataUtils {
	protected static final String url_a = "http://192.168.1.104:8099/Controller/";

	public static void postNewRequest(String url, RequestQueue queue) {
		StringRequest sr = new StringRequest(Request.Method.POST, url_a + url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.e("Post", response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("Post error", error.toString());
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("userName", "admin");
				params.put("pwd", "amituofo");
				params.put("sign", "d4e0512026d993af4803c16c27b7eb17");

				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				return params;
			}
		};
		queue.add(sr);
	}
}

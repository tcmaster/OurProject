package com.android.joocola.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpPostInterface {
	private HashMap<String, String> map = new HashMap<String, String>();

	public void addParma(String key, String value) {
		map.put(key, value);
	}

	public void getData(final String url,
			final HttpPostCallBack mHttpPostCallBack) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				Object[] key = map.keySet().toArray();
				Arrays.sort(key);
				String md5String = "";
				for (int i = 0; i < key.length; i++) {
					md5String = md5String + key[i].toString() + "="
							+ map.get(key[i].toString());
					if (i != key.length - 1) {
						md5String += "&";
					}
				}
				md5String = MD5Utils.md5s(md5String);

				HttpPost httpPost = new HttpPost(Constans.MAIN_URL + url);

				// 设置HTTP POST请求参数必须用NameValuePair对象
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					params.add(new BasicNameValuePair(
							entry.getKey().toString(), entry.getValue()
									.toString()));
				}
				params.add(new BasicNameValuePair("sign", md5String));
				HttpResponse httpResponse = null;
				try {
					// 设置httpPost请求参数
					httpPost.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					httpResponse = new DefaultHttpClient().execute(httpPost);
					Log.e("post的code", httpResponse.getStatusLine()
							.getStatusCode() + "");
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						// 第三步，使用getEntity方法活得返回结果
						String result = EntityUtils.toString(httpResponse
								.getEntity());
						Log.e("post的data", result);

						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(result);
							boolean flag = jsonObject.getBoolean("result");
							if (flag) {
								mHttpPostCallBack
										.httpPostResolveData(jsonObject
												.getString("data"));
							} else {
								Log.e("httpPost", "出错了");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public interface HttpPostCallBack {
		public void httpPostResolveData(String result);
	}
}

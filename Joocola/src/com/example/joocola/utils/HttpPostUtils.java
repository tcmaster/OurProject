package com.example.joocola.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

/*
 * 源生POST
 */
public class HttpPostUtils {
	public static void httpPost(final HttpPostCallBack mHttpPostCallBack) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpPost httpPost = new HttpPost(
						"http://192.168.1.104:8099/Controller/Sys.UserController.AppLogon2.ashx");

				// 设置HTTP POST请求参数必须用NameValuePair对象
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("userName", "admin"));
				params.add(new BasicNameValuePair("pwd", "amituofo"));
				params.add(new BasicNameValuePair("sign",
						"d4e0512026d993af4803c16c27b7eb17"));

				HttpResponse httpResponse = null;
				try {
					// 设置httpPost请求参数
					httpPost.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					httpResponse = new DefaultHttpClient().execute(httpPost);
					// System.out.println(httpResponse.getStatusLine().getStatusCode());
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						// 第三步，使用getEntity方法活得返回结果
						String result = EntityUtils.toString(httpResponse
								.getEntity());

						JSONObject jsonObject = new JSONObject(result);
						boolean flag = jsonObject.getBoolean("result");
						if (flag) {
							mHttpPostCallBack.httpPostResolveData(jsonObject
									.getString("data"));
						} else {
							Log.e("httpPost", "出错了");
						}

					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public interface HttpPostCallBack {
		public void httpPostResolveData(String result);
	}
}

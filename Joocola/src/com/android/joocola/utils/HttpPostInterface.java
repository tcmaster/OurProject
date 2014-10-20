package com.android.joocola.utils;

import java.io.File;
import java.io.FileNotFoundException;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.android.joocola.app.JoocolaApplication;

public class HttpPostInterface {

	private HashMap<String, String> map = new HashMap<String, String>();
	private static Handler handler = new Handler();

	public void addParams(String key, String value) {
		map.put(key, value);
	}

	public void getData(final String url, final HttpPostCallBack mHttpPostCallBack) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				String md5String = "";
				Object[] key = map.keySet().toArray();
				Arrays.sort(key);
				for (int i = 0; i < key.length; i++) {
					md5String = md5String + key[i].toString() + "=" + map.get(key[i].toString());
					if (i != key.length - 1) {
						md5String += "&";
					}
				}
				md5String = MD5Utils.md5s(md5String);

				// 设置HTTP POST请求参数必须用NameValuePair对象
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				Iterator iter = map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					params.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
				}
				params.add(new BasicNameValuePair("sign", md5String));
				postDirect(url, mHttpPostCallBack, params);
			}
		}).start();
	}

	public void uploadImageData(final File file, final HttpPostCallBack mHttpPostCallBack) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(Constants.UPLOADIMGURL);
				MultipartEntity entity = new MultipartEntity();
				try {
					FileBody fileBody = new FileBody(file);
					entity.addPart("Filedata", fileBody);
					post.setEntity(entity);
					HttpResponse response = null;
					if (!Utils.isNetConn(JoocolaApplication.getInstance())) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								mHttpPostCallBack.onNetWorkError();
							}
						});
						return;
					}
					response = client.execute(post);
					if (response.getStatusLine().getStatusCode() == 200) {
						String result = EntityUtils.toString(response.getEntity());
						Log.e("post的data", result);
						mHttpPostCallBack.httpPostResolveData(result);
					} else {
						Log.e("获取失败", "上传图片时获取返回值失败");
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 发送post请求，获取结果
	 * 
	 * @author: LiXiaosong
	 * @date:2014-10-14
	 */
	public void postDirect(String url, final HttpPostCallBack mHttpPostCallBack, List<NameValuePair> params) {
		HttpPost httpPost = null;
		httpPost = new HttpPost(Constants.MAIN_URL + url);
		HttpResponse httpResponse = null;
		try {
			if (params != null)
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			if (!Utils.isNetConn(JoocolaApplication.getInstance())) {// 当前网络断开
				handler.post(new Runnable() {

					@Override
					public void run() {
						// 调用网络断开接口
						mHttpPostCallBack.onNetWorkError();
					}
				});
				return;
			}
			httpResponse = new DefaultHttpClient().execute(httpPost);
			Log.e("post的code", httpResponse.getStatusLine().getStatusCode() + "");
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 第三步，使用getEntity方法活得返回结果
				String result = EntityUtils.toString(httpResponse.getEntity());
				Log.e("post的data", result);
				final JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					boolean flag = jsonObject.getBoolean("result");
					if (flag) {
						if (jsonObject.getString("data") != null) {
							handler.post(new Runnable() {

								@Override
								public void run() {
									try {
										mHttpPostCallBack.httpPostResolveData(jsonObject.getString("data"));
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
						} else {
							handler.post(new Runnable() {

								@Override
								public void run() {
									mHttpPostCallBack.httpPostResolveData("");
								}
							});
						}
					} else {
						Log.e("httpPost", "出错了");
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				mHttpPostCallBack.httpPostResolveData(null);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 网络接口回调
	 * 
	 * @author:LiXiaoSong
	 * @copyright © joocola.com
	 * @Date:2014-10-20
	 */
	public interface HttpPostCallBack {

		/**
		 * 网络正常，可以成功返回数据
		 * 
		 * @param result
		 * @author: LiXiaosong
		 * @date:2014-10-20
		 */
		public void httpPostResolveData(String result);

		/**
		 * 网络异常，需要进行自定义处理
		 * 
		 * @author: LiXiaosong
		 * @date:2014-10-20
		 */
		public void onNetWorkError();
	}
}

package com.android.doubanmovie.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtils {
	private static final String TAG = "HttpUtils";

	public static String getData(String path, Map<String, String> m,
			String encode) {
		String result = null;
		StringBuffer pathString = new StringBuffer();
		pathString.append(path);
		if (m != null && !m.isEmpty()) {
			pathString.append("?");
			for (Map.Entry<String, String> e : m.entrySet()) {
				pathString.append(e.getKey() + "=" + e.getValue() + "&");
			}
			// 去掉最后一个&
			pathString.setLength(pathString.length() - 1);
		}
		Log.v(TAG, "you want to connect to" + path);
		HttpURLConnection connection = null;
		InputStream inputStream = null;
		ByteArrayOutputStream bAO = new ByteArrayOutputStream();
		try {
			URL url = new URL(pathString.toString());
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = connection.getInputStream();
				byte[] b = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(b)) != -1) {
					bAO.write(b, 0, len);
				}
			}
			result = new String(bAO.toByteArray(), encode);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null)
				connection.disconnect();
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	public static byte[] getImgData(String path) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(path);
		HttpResponse httpResponse = null;
		try {
			httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				Log.i(TAG, "we get the image entity");
				return EntityUtils.toByteArray(httpResponse.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}
}

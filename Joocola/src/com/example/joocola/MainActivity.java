package com.example.joocola;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.joocola.utils.GetDataUtils;
import com.example.joocola.utils.HttpPostUtils;
import com.example.joocola.utils.HttpPostUtils.HttpPostCallBack;

public class MainActivity extends Activity {

	protected String url_b = "Sys.UserController.AppLogon2.ashx";
	private RequestQueue queue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final HashMap<String, String> mHashMap = GetDataUtils.getParmas();
		mHashMap.put("userName", "admin");
		mHashMap.put("pwd", "amituofo");
		queue = Volley.newRequestQueue(MainActivity.this);
		GetDataUtils.doGetData(url_b, mHashMap, queue);
		// PostDataUtils.postNewRequest(url_b, queue);
		HttpPostUtils.httpPost(new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Log.e("data", result);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

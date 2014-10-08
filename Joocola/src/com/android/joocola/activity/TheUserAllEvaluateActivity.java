package com.android.joocola.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.joocola.R;
import com.android.joocola.adapter.ShowEvaluateAdapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.AppointScoreEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.utils.Utils;

public class TheUserAllEvaluateActivity extends BaseActivity {

	private String mUserId;
	private String mName;
	private ListView mListView;
	private ArrayList<AppointScoreEntity> mList;
	private ShowEvaluateAdapter mAdapter;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_userevaluate);
		Intent intent = getIntent();
		mUserId = intent.getStringExtra("RelateUserID");
		Log.e("bb->id", mUserId);
		mName = intent.getStringExtra("name");
		initListview();
		initActionBar();
		initData();
	}

	/**
	 * 加载Actionbar
	 */
	private void initActionBar() {
		useCustomerActionBar();
		getActionBarRight().setVisibility(View.INVISIBLE);
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarleft().setText(mName + "获得的评价");
	}

	/**
	 * 加载listview
	 * 
	 * @see:
	 * @since:
	 * @author: bb
	 */
	private void initListview() {
		mListView = (ListView) this.findViewById(R.id.userevaluate_listview);
		mList = new ArrayList<AppointScoreEntity>();
		BitmapCache bitmapCache = JoocolaApplication.getInstance().getBitmapCache();
		mAdapter = new ShowEvaluateAdapter(mList, this, bitmapCache);
		mListView.setAdapter(mAdapter);
	}

	private void initData() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		if (TextUtils.isEmpty(mUserId)) {
			Utils.toast(TheUserAllEvaluateActivity.this, "获取id错误");
			return;
		}
		httpPostInterface.addParams("RelateUserID", mUserId);
		httpPostInterface.addParams("NeedAppointInfo", true + "");
		httpPostInterface.getData(Constants.QUERY_APPOINT_SCORE, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(final String result) {
				mHandler.post(new Runnable() {

					@Override
					public void run() {
						resoloveJson(result);
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		});
	}

	private void resoloveJson(String Json) {
		try {
			JSONObject jsonObject = new JSONObject(Json);
			JSONArray jsonArray = jsonObject.getJSONArray("Entities");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				AppointScoreEntity appointScoreEntity = JsonUtils.getAppointScoreEntity(object);
				mList.add(appointScoreEntity);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.SimpleApllyUserAdapter;
import com.android.joocola.entity.SimpleUserInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.view.AutoListView;

public class ApllyMangerActivity extends BaseActivity {
	private final String applyUrl = "Sys.UserController.GetUserSimpleInfos.ashx";
	private String issue_pid;// 该邀约id.
	private String ReserveDate;// 到期时间
	private AutoListView joinListView, unJoinListView;
	private List<SimpleUserInfo> joinList = new ArrayList<SimpleUserInfo>();
	private List<SimpleUserInfo> unJoinList = new ArrayList<SimpleUserInfo>();
	private TextView reserveDateTextView;
	private SimpleApllyUserAdapter joinAdapter, unJoinAdapter;
	private BitmapCache bitmapCache;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				joinList = resoloveUnjoinJson(result);
				joinAdapter.setmUsers(joinList);
				joinListView.setAdapter(joinAdapter);
				break;
			/**
			 * 申请加入的
			 */
			case 2:
				String result1 = (String) msg.obj;
				Log.e("222222222222", result1);
				unJoinList = resoloveUnjoinJson(result1);
				unJoinAdapter.setmUsers(unJoinList);
				unJoinListView.setAdapter(unJoinAdapter);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_applymanger);
		Intent intent = getIntent();
		issue_pid = intent.getStringExtra("issue_pid");
		ReserveDate = intent.getStringExtra("ReserveDate");
		initActionbar();
		initView();
	}

	private void initView() {
		bitmapCache = new BitmapCache();
		reserveDateTextView = (TextView) this.findViewById(R.id.apply_time);
		reserveDateTextView.setText(ReserveDate);
		joinListView = (AutoListView) this.findViewById(R.id.join_userlistview);
		unJoinListView = (AutoListView) this
				.findViewById(R.id.unjoin_userlistview);
		joinAdapter = new SimpleApllyUserAdapter(this, bitmapCache);
		// joinAdapter.setmUsers(joinList);
		unJoinAdapter = new SimpleApllyUserAdapter(this, bitmapCache);
		// unJoinAdapter.setmUsers(unJoinList);
		// joinListView.setAdapter(joinAdapter);
		// unJoinListView.setAdapter(unJoinAdapter);
		initJoinList();
		initUnJoinList();

	}

	/**
	 * 加载Actionbar
	 */
	private void initActionbar() {
		useCustomerActionBar();
		getActionBarleft().setText("报名管理");
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
	}

	private void initJoinList() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("AppointUserState", 30 + "");
		httpPostInterface.addParams("AppointID", issue_pid);
		httpPostInterface.getData(applyUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 1;
				message.obj = result;
				mHandler.sendMessage(message);
			}
		});
	}

	private void initUnJoinList() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("AppointUserState", 10 + "");
		httpPostInterface.addParams("AppointID", issue_pid);
		httpPostInterface.getData(applyUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 2;
				message.obj = result;
				mHandler.sendMessage(message);
			}
		});
	}

	private List<SimpleUserInfo> resoloveUnjoinJson(String json) {
		List<SimpleUserInfo> list = new ArrayList<SimpleUserInfo>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("Entities");
			for (int i = 0; i < jsonArray.length(); i++) {
				SimpleUserInfo simpleUserInfo = new SimpleUserInfo();
				JSONObject simpleJsonObject = jsonArray.getJSONObject(i);
				simpleUserInfo.setPhotoUrl(simpleJsonObject
						.getString("PhotoUrl"));
				simpleUserInfo.setPid(simpleJsonObject.getInt("PID"));
				simpleUserInfo.setSignature(simpleJsonObject
						.getString("Signature"));
				simpleUserInfo.setUserName(simpleJsonObject
						.getString("UserName"));
				list.add(simpleUserInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;

	}
}

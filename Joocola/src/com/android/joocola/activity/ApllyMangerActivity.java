package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.SimpleApllyUserAdapter;
import com.android.joocola.entity.SimpleUserInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.android.joocola.view.AutoListView;

public class ApllyMangerActivity extends BaseActivity {
	private final String applyUrl = "Sys.UserController.GetUserSimpleInfos.ashx";
	private String issue_pid;// 该邀约id.
	private String user_pid;// 操纵者id
	private String ReserveDate;// 到期时间
	private AutoListView joinListView, unJoinListView;
	private List<SimpleUserInfo> joinList = new ArrayList<SimpleUserInfo>();
	private List<SimpleUserInfo> unJoinList = new ArrayList<SimpleUserInfo>();
	private TextView reserveDateTextView;
	private SimpleApllyUserAdapter joinAdapter, unJoinAdapter;
	private BitmapCache bitmapCache;
	private SharedPreferences sharedPreferences;
	private TextView join_count, unjoin_count;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				joinList = resoloveAlljoinJson(result);
				join_count.setText("已加入(" + joinList.size() + ")");
				joinAdapter.setmUsers(joinList);
				joinListView.setAdapter(joinAdapter);
				break;
			/**
			 * 申请加入的
			 */
			case 2:
				String result1 = (String) msg.obj;
				unJoinList = resoloveAlljoinJson(result1);
				unjoin_count.setText("未加入(" + unJoinList.size() + ")");
				unJoinAdapter.setmUsers(unJoinList);
				unJoinListView.setAdapter(unJoinAdapter);
				break;
			/**
			 * 点击批准加入以后。
			 */
			case 3:
				String result2 = (String) msg.obj;
				resoloveApproveJson(result2);
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
		sharedPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE,
				Context.MODE_PRIVATE);
		user_pid = sharedPreferences.getString(Constans.LOGIN_PID, "0");
		Intent intent = getIntent();
		issue_pid = intent.getStringExtra("issue_pid");
		ReserveDate = intent.getStringExtra("ReserveDate");
		initActionbar();
		initView();
	}

	private void initView() {
		bitmapCache = new BitmapCache();
		join_count = (TextView) this.findViewById(R.id.join_count);
		unjoin_count = (TextView) this.findViewById(R.id.unjoin_count);
		reserveDateTextView = (TextView) this.findViewById(R.id.apply_time);
		reserveDateTextView.setText("请在" + ReserveDate + "前做出选择");
		joinListView = (AutoListView) this.findViewById(R.id.join_userlistview);
		joinListView.setOnRefreshListener(null);
		joinListView.setOnLoadListener(null);
		unJoinListView = (AutoListView) this
				.findViewById(R.id.unjoin_userlistview);
		unJoinListView.setOnRefreshListener(null);
		unJoinListView.setOnLoadListener(null);
		joinAdapter = new SimpleApllyUserAdapter(this, bitmapCache, mHandler);
		joinAdapter.setState(30);
		unJoinAdapter = new SimpleApllyUserAdapter(this, bitmapCache, mHandler);
		unJoinAdapter.setState(10);
		unJoinAdapter.setIssue_pid(issue_pid);
		unJoinAdapter.setPublish_id(user_pid);
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

	/**
	 * 解析申请加入邀约的用户json
	 * 
	 * @param json
	 * @return
	 */
	private List<SimpleUserInfo> resoloveAlljoinJson(String json) {
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

	/**
	 * 解析点击审核通过按钮后的json
	 * 
	 * @param result
	 */
	private void resoloveApproveJson(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			boolean isTrue = jsonObject.getBoolean("Item1");
			String error = jsonObject.getString("Item2");
			if (isTrue) {
				Utils.toast(ApllyMangerActivity.this, "审批完成");
				initJoinList();
				initUnJoinList();
			} else {
				Utils.toast(ApllyMangerActivity.this, error);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

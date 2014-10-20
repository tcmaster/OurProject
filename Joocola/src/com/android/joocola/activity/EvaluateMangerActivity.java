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
import android.widget.ListView;

import com.android.joocola.R;
import com.android.joocola.adapter.EvaluateItemAdapter;
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.utils.Utils;

/**
 * 该界面是评价管理界面。 是在一个邀约报名结束后 可以进入的界面， 1评价管理。 进入页面后 加载该邀约下所有成功加入的用户列表 AppointID 邀约的ID CurUserID 当前登录用户的ID
 * AppointUserOnlyJoined true 已加入的 ItemsPerPage 100 可以拿到所有加入的用户名单 2 判断当前用户是不是发布者 如果是 加载所有的 如果不是 只加载发布者和自己
 * 始终不显示自己。
 * 
 * 3 第一行始终显示状态 第二行始终显示按钮 当AppointScoreStateID=0（尚未评价）或者20（对方已评）此处显示 按钮文本为评价。 否则 显示为查看评价
 * 
 * 4 查看评价时 首先读取 已有评价 判断当前用户是不是发布者 如果是 RelateUserID=当前被查看的用户的ID。 否则RelateUserID = 当前登录用户的ID.
 * 
 * @author bb
 * 
 */
public class EvaluateMangerActivity extends BaseActivity {

	private ListView mListView;
	private String user_id; // 当前用户的ID
	private boolean isPublish; // 是否是该邀约的发布者
	private EvaluateItemAdapter mEvaluateItemAdapter;
	private BitmapCache bitmapCache;
	private String issue_pid;
	private List<UserInfo> mInfos = new ArrayList<UserInfo>();
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			/**
			 * 加载完所有的用户以后
			 */
			case 0:
				String result = (String) msg.obj;
				Log.e("bb", result);
				resolveJson(result);
				mEvaluateItemAdapter.notifyDataSetChanged();
				break;
			/**
			 * 点击评价后的toast
			 */
			case 1:
				Utils.toast(EvaluateMangerActivity.this, "您已评价成功");
				initListViewData();
				break;
			case 2:
				String result1 = (String) msg.obj;
				Utils.toast(EvaluateMangerActivity.this, result1);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate);
		bitmapCache = new BitmapCache();
		initActionBar();
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("value");
		user_id = bundle.getString("user_id");
		isPublish = bundle.getBoolean("ispublish");
		issue_pid = bundle.getInt("issue_pid") + "";
		initListview();
	}

	/**
	 * 加载Listview
	 */
	private void initListview() {
		mListView = (ListView) this.findViewById(R.id.evaluate_listview);
		mEvaluateItemAdapter = new EvaluateItemAdapter(mInfos, this, bitmapCache);
		mEvaluateItemAdapter.setmAppointID(issue_pid);
		mEvaluateItemAdapter.setmUserPid(user_id);
		mEvaluateItemAdapter.setHandler(mHandler);
		mEvaluateItemAdapter.setPublish(isPublish);
		mListView.setAdapter(mEvaluateItemAdapter);
		initListViewData();
	}

	/**
	 * 加载Actionbar
	 */
	private void initActionBar() {
		useCustomerActionBar();
		getActionBarRight().setVisibility(View.INVISIBLE);
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarleft().setText("评价管理");
	}

	/**
	 * 加载所有的用户
	 */
	private void initListViewData() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("AppointID", issue_pid);
		httpPostInterface.addParams("CurUserID", user_id);
		httpPostInterface.addParams("AppointUserOnlyJoined", "true");
		httpPostInterface.addParams("ItemsPerPage", "200");
		httpPostInterface.getData(Constants.USERINFOURL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 0;
				message.obj = result;
				mHandler.sendMessage(message);
			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 解析json
	 */
	private void resolveJson(String json) {
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("Entities");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject userObject = jsonArray.getJSONObject(i);
				UserInfo userInfo = new UserInfo();
				userInfo = JsonUtils.getUserInfo(userObject, userInfo);
				mInfos.add(userInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

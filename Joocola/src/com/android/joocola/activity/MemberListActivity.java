package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import com.android.joocola.R;
import com.android.joocola.R.id;
import com.android.joocola.R.layout;
import com.android.joocola.adapter.NearByPersonAdapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MemberListActivity extends BaseActivity {
	@ViewInject(R.id.memberList)
	private ListView member_lv;
	private String issueID;
	private Handler handler;
	private NearByPersonAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_list);
		issueID = getIntent().getStringExtra("issueID");
		handler = new Handler(getMainLooper());
		ViewUtils.inject(this);
		initActionBar();
		initData();
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
		getActionBarleft().setVisibility(View.VISIBLE);
		getActionBarleft().setText("群聊信息(" + ")人");
	}

	private void initData() {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("AppointID", issueID);
		interface1.addParams("AppointUserOnlyJoined", "true");
		interface1.addParams("CurUserID", JoocolaApplication.getInstance()
				.getPID());
		interface1.getData(Constans.USERINFOURL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result == null || result.equals("")) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(MemberListActivity.this,
									"获取用户资料失败,请检查网络");
						}
					});
				} else {
					JSONObject object;
					try {
						object = new JSONObject(result);
						JSONArray array = object.getJSONArray("Entities");
						final List<UserInfo> infos = new ArrayList<UserInfo>();
						for (int i = 0; i < array.length(); i++) {
							final JSONObject userObject = array
									.getJSONObject(i);
							infos.add(JsonUtils.getUserInfo(userObject,
									new UserInfo()));
						}
						handler.post(new Runnable() {

							@Override
							public void run() {
								adapter = new NearByPersonAdapter(infos,
										MemberListActivity.this,
										new BitmapCache());
								member_lv.setAdapter(adapter);
								getActionBarleft().setText(
										"群聊信息(" + infos.size() + ")人");
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}

}

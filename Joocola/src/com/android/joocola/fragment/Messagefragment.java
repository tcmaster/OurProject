package com.android.joocola.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.joocola.R;
import com.android.joocola.activity.ChatActivity;
import com.android.joocola.adapter.Fg_Chat_List_Adapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.MyChatInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 
 * @author bb，lixiaosong
 * 
 */
public class Messagefragment extends Fragment {

	/**
	 * 消息列表的listView
	 */
	@ViewInject(R.id.chatlist)
	private ListView lv_message_list;
	/**
	 * 数据库
	 */
	private DbUtils db;
	/**
	 * handler
	 */
	private Fg_Chat_List_Adapter adapter;
	/**
	 * 发送广播
	 */
	private MyReceiver receiver;
	/**
	 * handler
	 */
	private Handler handler;
	/**
	 * 是否第一次显示界面
	 */
	private boolean isFirst = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化数据库
		db = JoocolaApplication.getInstance().getDB();
		// 初始化适配器
		adapter = new Fg_Chat_List_Adapter(getActivity(), new ArrayList<MyChatInfo>());
		// 初始化handler
		handler = new Handler(getActivity().getMainLooper());
		// 注册接收消息的广播
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter(Constans.CHAT_ACTION);
		getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, container, false);
		ViewUtils.inject(this, view);
		if (isFirst)
			isFirst = false;
		lv_message_list.setAdapter(adapter);
		return view;
	}

	private void updateData() {
		List<MyChatInfo> tResult;
		try {
			tResult = db.findAll(MyChatInfo.class);
			HttpPostInterface interface1 = new HttpPostInterface();
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < tResult.size(); i++) {
				if (i == tResult.size() - 1)
					;
				// builder.append(tResult[i].split("-")[1].substring(1));
				else
					;
				// builder.append(tResult[i].split("-")[1].substring(1) + ",");
			}
			interface1.addParams("UserIDs", builder.toString());
			interface1.getData(Constans.USERINFOURL, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					if (result == null || result.equals("")) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								Utils.toast(getActivity(), "获取用户资料失败,请检查网络");
							}
						});
					} else {
						JSONObject object;
						try {
							object = new JSONObject(result);
							JSONArray array = object.getJSONArray("Entities");
							for (int i = 0; i < array.length(); i++) {
								final JSONObject tempData = array.getJSONObject(i);
								handler.post(new Runnable() {

									@Override
									public void run() {
										try {
											adapter.addName("u" + tempData.getString("PID"), tempData.getString("NickName"));
											adapter.addPhotos("u" + tempData.getString("PID"), tempData.getString("PhotoUrl"));
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								});
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}
			});
		} catch (DbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@OnItemClick(R.id.chatlist)
	public void onlistItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("nickName", "");
		intent.putExtra("userId", "");
		intent.putExtra("userNickName", "");
		intent.putExtra("isSingle", true);
		startActivity(intent);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (isVisibleToUser && !isFirst) {
			updateData();
		}
	}

	@Override
	public void onDestroy() {
		getActivity().unregisterReceiver(receiver);
		super.onDestroy();
	}

	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateData();
		}

	}
}

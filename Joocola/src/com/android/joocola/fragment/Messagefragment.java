package com.android.joocola.fragment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.activity.ChatActivity;
import com.android.joocola.adapter.Fg_Chat_List_Adapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.ChatOfflineInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 
 * @author bb，lixiaosong
 * 
 */
public class Messagefragment extends Fragment {
	@ViewInject(R.id.chatlist)
	private ListView lv_message_list;
	private DbUtils db;
	private List<ChatOfflineInfo> infos;
	private Set<String> listInfo;
	private Fg_Chat_List_Adapter adapter;
	private boolean isFirst = true;
	private MyReceiver receiver;
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = JoocolaApplication.getInstance().getDB();
		IntentFilter filter = new IntentFilter(Constans.CHAT_ACTION);
		receiver = new MyReceiver();
		handler = new Handler(getActivity().getMainLooper());
		getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		ViewUtils.inject(getActivity());
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, container,
				false);
		ViewUtils.inject(this, view);
		// updateData();
		if (isFirst)
			isFirst = !isFirst;
		return view;
	}

	private void updateData() {
		listInfo = new HashSet<String>();
		if (db == null)
			db = JoocolaApplication.getInstance().getDB();
		try {
			infos = db.findAll(Selector.from(ChatOfflineInfo.class).where(
					"user", "=",
					"u" + JoocolaApplication.getInstance().getPID()));
			if (infos != null) {
				for (int i = 0; i < infos.size(); i++) {
					/**
					 * 过滤重复的数据
					 */
					listInfo.add(infos.get(i).getKey());
				}
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		String[] tResult = listInfo.toArray(new String[] {});
		if (adapter == null) {
			adapter = new Fg_Chat_List_Adapter(getActivity(), tResult);
			lv_message_list.setAdapter(adapter);
		} else {
			adapter.bindData(tResult);
			lv_message_list.setAdapter(adapter);
		}
		HttpPostInterface interface1 = new HttpPostInterface();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < tResult.length; i++) {
			if (i == tResult.length - 1)
				builder.append(tResult[i].split("-")[1].substring(1));
			else
				builder.append(tResult[i].split("-")[1].substring(1) + ",");
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
										adapter.addName(
												"u" + tempData.getString("PID"),
												tempData.getString("NickName"));
										adapter.addPhotos(
												"u" + tempData.getString("PID"),
												tempData.getString("PhotoUrl"));
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
	}

	@OnItemClick(R.id.chatlist)
	public void onlistItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("nickName",
				listInfo.toArray(new String[] {})[position].split("-")[1]);
		intent.putExtra("userId", listInfo.toArray(new String[] {})[position]
				.split("-")[1].substring(1));
		intent.putExtra("userNickName", ((TextView) view
				.findViewById(R.id.ml_nickName_tv)).getText().toString());
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

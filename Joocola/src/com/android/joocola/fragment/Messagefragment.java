package com.android.joocola.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.activity.BaseActivity;
import com.android.joocola.activity.ChatActivity;
import com.android.joocola.activity.IssueDynamicActivity;
import com.android.joocola.adapter.Fg_Chat_List_Adapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.MyChatInfo;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 聊天列表界面（单聊，群聊）
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
	 * 邀约动态
	 */
	@ViewInject(R.id.issue_News)
	private RelativeLayout rl_issue_news;
	/**
	 * 系统消息
	 */
	@ViewInject(R.id.system_message)
	private RelativeLayout rl_system_message;
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
	// 本页面的数据源
	private List<MyChatInfo> tResult;
	/**
	 * log 开关
	 */
	private boolean DEBUG = true;

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
		IntentFilter filter = new IntentFilter(Constants.CHAT_ACTION);
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
		if (isFirst) {
			isFirst = false;
			if (DEBUG)
				LogUtils.v("第一次的状态改变");
		}
		initView();
		lv_message_list.setAdapter(adapter);
		return view;
	}

	/**
	 * 该方法主要用来初始化邀约和系统消息的视图
	 * 
	 * @author: LiXiaosong
	 * @date:2014-10-13
	 */
	private void initView() {
		ImageView ml_iv_in = (ImageView) rl_issue_news.findViewById(R.id.ml_iv);
		ImageView rp_iv_in = (ImageView) rl_issue_news.findViewById(R.id.redPoint);
		TextView ml_nickName_in = (TextView) rl_issue_news.findViewById(R.id.ml_nickName_tv);
		TextView ml_chatInfo_in = (TextView) rl_issue_news.findViewById(R.id.ml_chatInfo_tv);
		TextView ml_date_in = (TextView) rl_issue_news.findViewById(R.id.ml_date_tv);
		ImageView ml_iv_sm = (ImageView) rl_system_message.findViewById(R.id.ml_iv);
		ImageView rp_iv_sm = (ImageView) rl_system_message.findViewById(R.id.redPoint);
		TextView ml_nickName_sm = (TextView) rl_system_message.findViewById(R.id.ml_nickName_tv);
		TextView ml_chatInfo_sm = (TextView) rl_system_message.findViewById(R.id.ml_chatInfo_tv);
		TextView ml_date_sm = (TextView) rl_system_message.findViewById(R.id.ml_date_tv);
		rp_iv_in.setVisibility(View.INVISIBLE);
		rp_iv_sm.setVisibility(View.INVISIBLE);
		ml_nickName_sm.setText("系统消息");
		ml_nickName_in.setText("邀约动态");
		ml_chatInfo_in.setText("");
		ml_chatInfo_sm.setText("");
		ml_date_in.setText("");
		ml_date_sm.setText("");

	}

	private void updateData() {

		try {
			tResult = db.findAll(Selector.from(MyChatInfo.class).where("PID", "=", JoocolaApplication.getInstance().getPID()));
			// 绑定最新数据
			if (tResult != null) {
				adapter.bindData(tResult);
				// 单聊的ids
				StringBuilder sBuilder = new StringBuilder();
				// 群聊的ids
				StringBuilder mBuilder = new StringBuilder();
				for (int i = 0; i < tResult.size(); i++) {
					// 若为群聊，则将ids存入邀约id列表
					if (tResult.get(i).chatType == Constants.CHAT_TYPE_MULTI) {
						mBuilder.append(tResult.get(i).user + ",");
						continue;
					}
					// 若为单聊，将ids存入用户id列表
					sBuilder.append(tResult.get(i).user.substring(1, tResult.get(i).user.length()) + ",");
				}
				// 下面这段是为了获得当前的用户昵称,用户头像(仅限单聊)
				Map<String, String> sMap = new HashMap<String, String>();
				if (sBuilder.length() > 0) {
					sMap.put("UserIDs", sBuilder.substring(0, sBuilder.length() - 1));
					((BaseActivity) getActivity()).getHttpResult(sMap, Constants.USERINFOURL, new HttpPostCallBack() {

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
				}
				// 下面这段是为了得到群聊的照片和昵称
				if (mBuilder.length() > 0) {
					Map<String, String> mMap = new HashMap<String, String>();
					mMap.put("RoomIDs", mBuilder.substring(0, mBuilder.length() - 1));
					((BaseActivity) getActivity()).getHttpResult(mMap, Constants.GET_QUERY_APPOINT, new HttpPostCallBack() {

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
													adapter.addName(tempData.getString("RoomID"), tempData.getString("Title"));
													adapter.addPhotos(tempData.getString("RoomID"), tempData.getString("RoomPhotoUrl"));
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
			}
		} catch (DbException e1) {
			e1.printStackTrace();
		}
	}

	@OnItemClick(R.id.chatlist)
	public void onlistItemClick(AdapterView<?> parent, View view, int position, long id) {
		TextView nickName_tv = (TextView) view.findViewById(R.id.ml_nickName_tv);
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		int chatType = tResult.get(position).chatType;
		LogUtils.v(tResult.get(position).user);
		LogUtils.v(tResult.get(position).chatType + "");
		if (chatType == Constants.CHAT_TYPE_MULTI) {
			intent.putExtra("userId", tResult.get(position).user);
			EMGroup group;
			try {
				group = EMGroupManager.getInstance().getGroupFromServer(tResult.get(position).user);
				ArrayList<String> list = new ArrayList<String>();
				List<String> members = group.getMembers();
				for (int i = 0; i < members.size(); i++) {
					if (members.get(i).length() <= 1)
						continue;
					list.add(members.get(i).substring(1));

				}
				intent.putStringArrayListExtra("users", list);
			} catch (EaseMobException e) {
				e.printStackTrace();
			}

		} else if (chatType == Constants.CHAT_TYPE_SINGLE) {
			intent.putExtra("userId", tResult.get(position).user.substring(1));
		}
		intent.putExtra("userNickName", nickName_tv.getText().toString());
		// 返回该聊天是否为多人聊天
		intent.putExtra("isSingle", chatType == Constants.CHAT_TYPE_MULTI ? false : true);
		startActivity(intent);
	}

	@OnClick({ R.id.issue_News, R.id.system_message })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.issue_News:
			Intent intent = new Intent();
			intent.setClass(getActivity(), IssueDynamicActivity.class);
			startActivity(intent);
			break;
		case R.id.system_message:

			break;
		default:
			break;
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		if (DEBUG)
			LogUtils.v("进入setUserVisibleHint");
		if (isVisibleToUser && !isFirst) {
			if (DEBUG)
				LogUtils.v("执行setUserVisibleHint");
			updateData();
		}
	}

	@Override
	public void onResume() {
		if (DEBUG)
			LogUtils.v("执行onResume");
		updateData();
		super.onResume();
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

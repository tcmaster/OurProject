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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.joocola.MainTabActivity;
import com.android.joocola.R;
import com.android.joocola.activity.ChatActivity;
import com.android.joocola.activity.IssueDynamicActivity;
import com.android.joocola.activity.SystemMessageActivity;
import com.android.joocola.adapter.Fg_Chat_List_Adapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.MyChatInfo;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.android.joocola.view.MyListView;
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
	private MyListView lv_message_list;
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
	 * 接收聊天消息的广播
	 */
	private MyReceiver receiver;
	/**
	 * 接收系统消息的广播
	 */
	private MySystemReceiver systemReceiver;
	/**
	 * 接收邀约消息的广播
	 */
	private MyIssueReceiver issueReceiver;
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
	/**
	 * 本fragment所对应的activity
	 */
	private MainTabActivity activity;
	/**
	 * 邀约动态的红点
	 */
	private ImageView rp_issue;
	/**
	 * 系统消息的红点
	 */
	private ImageView rp_system;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化activity
		activity = (MainTabActivity) getActivity();
		// 初始化数据库
		db = JoocolaApplication.getInstance().getDB();
		// 初始化适配器
		adapter = new Fg_Chat_List_Adapter(activity, new ArrayList<MyChatInfo>());
		// 初始化handler
		handler = new Handler(activity.getMainLooper());
		// 需放到这里，为了保证广播能及时被接收
		initData();
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
	 * 初始化,注册本界面需要注册的广播
	 */
	private void initData() {
		// 注册接收消息的广播
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter(Constants.CHAT_ACTION);
		activity.registerReceiver(receiver, filter);
		// 注册接收系统消息的广播
		systemReceiver = new MySystemReceiver();
		IntentFilter filter2 = new IntentFilter(Constants.CHAT_ADMIN_ACTION);
		activity.registerReceiver(systemReceiver, filter2);
		// 注册接收邀约消息的广播
		issueReceiver = new MyIssueReceiver();
		IntentFilter filter3 = new IntentFilter(Constants.CHAT_ISSUE_ACTION);
		activity.registerReceiver(issueReceiver, filter3);
	}

	/**
	 * 该方法主要用来初始化邀约和系统消息的视图
	 * 
	 * @author: LiXiaosong
	 * @date:2014-10-13
	 */
	private void initView() {
		ImageView ml_iv_in = (ImageView) rl_issue_news.findViewById(R.id.ml_iv);
		rp_issue = (ImageView) rl_issue_news.findViewById(R.id.redPoint);
		TextView ml_nickName_in = (TextView) rl_issue_news.findViewById(R.id.ml_nickName_tv);
		TextView ml_chatInfo_in = (TextView) rl_issue_news.findViewById(R.id.ml_chatInfo_tv);
		TextView ml_date_in = (TextView) rl_issue_news.findViewById(R.id.ml_date_tv);
		ImageView ml_iv_sm = (ImageView) rl_system_message.findViewById(R.id.ml_iv);
		rp_system = (ImageView) rl_system_message.findViewById(R.id.redPoint);
		TextView ml_nickName_sm = (TextView) rl_system_message.findViewById(R.id.ml_nickName_tv);
		TextView ml_chatInfo_sm = (TextView) rl_system_message.findViewById(R.id.ml_chatInfo_tv);
		TextView ml_date_sm = (TextView) rl_system_message.findViewById(R.id.ml_date_tv);
		rp_issue.setVisibility(View.INVISIBLE);
		rp_system.setVisibility(View.INVISIBLE);
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
				// 记录当前是否有未读消息
				int count = 0;
				for (int i = 0; i < tResult.size(); i++) {
					// 若为群聊，则将ids存入邀约id列表
					if (tResult.get(i).isRead) {
						count++;
					}
					if (tResult.get(i).chatType == Constants.CHAT_TYPE_MULTI) {
						mBuilder.append(tResult.get(i).user + ",");
						continue;
					}
					// 若为单聊，将ids存入用户id列表
					sBuilder.append(tResult.get(i).user.substring(1, tResult.get(i).user.length()) + ",");
				}
				if (rp_issue.getVisibility() == View.INVISIBLE)
					count++;
				if (rp_system.getVisibility() == View.INVISIBLE)
					count++;
				if (count < tResult.size() + 2)
					// 说明有未读消息
					activity.setRedPointVisible(true);
				else
					activity.setRedPointVisible(false);

				// 下面这段是为了获得当前的用户昵称,用户头像(仅限单聊)
				Map<String, String> sMap = new HashMap<String, String>();
				if (sBuilder.length() > 0) {
					sMap.put("UserIDs", sBuilder.substring(0, sBuilder.length() - 1));
					activity.getHttpResult(sMap, Constants.USERINFOURL, new HttpPostCallBack() {

						@Override
						public void httpPostResolveData(String result) {
							if (result == null || result.equals("")) {
								handler.post(new Runnable() {

									@Override
									public void run() {
										Utils.toast(activity, "获取用户资料失败,请检查网络");
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
					activity.getHttpResult(mMap, Constants.GET_QUERY_APPOINT, new HttpPostCallBack() {

						@Override
						public void httpPostResolveData(String result) {
							if (result == null || result.equals("")) {
								handler.post(new Runnable() {

									@Override
									public void run() {
										Utils.toast(activity, "获取用户资料失败,请检查网络");
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
		Intent intent = new Intent(activity, ChatActivity.class);
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
		Intent intent = new Intent();
		int requestCode = 0;
		switch (v.getId()) {
		case R.id.issue_News:
			intent.setClass(activity, IssueDynamicActivity.class);
			rp_issue.setVisibility(View.INVISIBLE);
			requestCode = activity.REQUEST_ISSUE_MSG;
			break;
		case R.id.system_message:
			intent.setClass(activity, SystemMessageActivity.class);
			rp_system.setVisibility(View.INVISIBLE);
			requestCode = activity.REQUEST_SYSTEM_MSG;
			break;
		default:
			break;
		}
		startActivityForResult(intent, requestCode);
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
		activity.unregisterReceiver(receiver);
		activity.unregisterReceiver(systemReceiver);
		activity.unregisterReceiver(issueReceiver);
		super.onDestroy();
	}

	/**
	 * 接收聊天消息的广播
	 * 
	 * @author:LiXiaoSong
	 * @copyright © joocola.com
	 * @Date:2014-10-16
	 */
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateData();
		}
	}

	/**
	 * 接收系统消息的广播，主要为了控制红点
	 */
	private class MySystemReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			new Thread(new Runnable() {

				@Override
				public void run() {
					// 为了防止界面报空
					while (rp_system != null)
						;
					handler.post(new Runnable() {

						@Override
						public void run() {
							rp_system.setVisibility(View.VISIBLE);
						}
					});
				}
			}).start();
		}
	}

	/**
	 * 接收邀约消息的广播，主要为了控制红点
	 */
	private class MyIssueReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// 为了防止界面报空
					while (rp_issue != null)
						;
					handler.post(new Runnable() {

						@Override
						public void run() {
							rp_issue.setVisibility(View.VISIBLE);
						}
					});
				}
			}).start();
		}

	}
}

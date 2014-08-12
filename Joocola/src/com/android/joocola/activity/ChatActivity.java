package com.android.joocola.activity;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.SingleChatAdapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.chat.SingleChat;
import com.android.joocola.chat.XMPPChat;
import com.android.joocola.entity.ChatOfflineInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 聊天界面 该界面用到了XUtils
 * 
 * @author lixiaosong
 * 
 */
public class ChatActivity extends BaseActivity {
	/**
	 * 聊天窗口
	 */
	@ViewInject(R.id.chat_container_lv)
	private ListView lv_container;
	/**
	 * 发送消息的内容
	 */
	@ViewInject(R.id.add_content)
	private EditText et_content;
	/**
	 * 消息发送按钮
	 */
	@ViewInject(R.id.send_btn)
	private Button btn_send;
	/**
	 * 点击查看历史记录
	 */
	@ViewInject(R.id.chat_showHistory_tv)
	private TextView tv_showHistoty;

	private ChatReceiver receiver;

	/**
	 * 判断该窗口是单人聊天窗口还是多人聊天窗口,默认是单人
	 */
	private boolean isSingle = true;
	/**
	 * 单人聊天时的聊天对象
	 */
	private String nickName = "bb";
	/**
	 * 单人聊天时的用户id
	 */
	private String userId = "userId";
	private SingleChatAdapter adapter;
	private Handler handler;
	/**
	 * 显示还是隐藏历史记录，true为当前显示，false为当前隐藏
	 */
	private boolean isShowHistory = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		ViewUtils.inject(this);
		nickName = getIntent().getStringExtra("nickName");
		userId = getIntent().getStringExtra("userId");
		initActionBar();
		handler = new Handler(getMainLooper());
		if (isSingle) {
			IntentFilter filter = new IntentFilter(Constans.CHAT_ACTION);
			receiver = new ChatReceiver();
			registerReceiver(receiver, filter);
			adapter = new SingleChatAdapter(this, nickName);
			getUserImgUrl(new String[] { userId,
					JoocolaApplication.getInstance().getUserInfo().getPID() });
			adapter.updateNoReadData();
			lv_container.setAdapter(adapter);
			scrollBottom();
		}
	}

	@Override
	protected void onResume() {
		if (XMPPChat.getInstance().getConnection().isConnected()) {
			XMPPChat.getInstance().setPresence(XMPPChat.ONLINE);
		}
		super.onResume();
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarleft().setText("聊天");
		getActionBarTitle().setText(nickName);
		getActionBarRight().setText("");

	}

	public class ChatReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			adapter.updateNoReadData();
			scrollBottom();
		}
	}

	@OnClick({ R.id.send_btn, R.id.chat_showHistory_tv })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_btn:
			String content = et_content.getText().toString();
			if (content == null || content.equals("")) {
				Utils.toast(this, "输入内容不能为空");
				return;
			}
			// 得到会话
			Chat chat = SingleChat.getInstance().getFriendChat(nickName, null);
			try {
				chat.sendMessage(content);
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			ChatOfflineInfo info = new ChatOfflineInfo();
			info.setContent(content);
			info.setIsFrom(XMPPChat.getInstance().getConnection().getUser()
					.split("@")[0]);
			info.setIsTo(nickName);
			info.setIsRead(0);
			info.setKey(info.getIsFrom() + "-" + info.getIsTo());
			info.setUser(JoocolaApplication.getInstance().getUserInfo()
					.getUserName());
			try {
				JoocolaApplication.getInstance().getDB().save(info);
			} catch (DbException e) {
				e.printStackTrace();
			}
			adapter.updateNoReadData();
			scrollBottom();
			et_content.setText("");
			break;
		case R.id.chat_showHistory_tv:
			if (isShowHistory == false) {
				adapter.showHistory();
				tv_showHistoty.setText(getResources().getString(
						R.string.hideHistory));

			} else {
				adapter.hideHistory();
				tv_showHistoty.setText(getResources().getString(
						R.string.showHistory));
			}
			isShowHistory = !isShowHistory;
			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		if (receiver != null)
			unregisterReceiver(receiver);
		// 将数据库中的未读信息变为已读信息
		adapter.saveHistory();
		super.onDestroy();
	}

	/**
	 * listview滑动到底部
	 */
	public void scrollBottom() {
		lv_container.post(new Runnable() {

			@Override
			public void run() {
				lv_container.setSelection(lv_container.getBottom());
			}
		});
	}

	/**
	 * 根据用户的PID查询用户的头像地址
	 */
	public void getUserImgUrl(String[] userPIDs) {
		HttpPostInterface interface1 = new HttpPostInterface();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < userPIDs.length; i++) {
			if (i == userPIDs.length - 1)
				builder.append(userPIDs[i]);
			else
				builder.append(userPIDs[i] + ",");
		}
		interface1.addParams("UserIDs", builder.toString());
		interface1.getData(Constans.USERINFOURL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result == null || result.equals("")) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(ChatActivity.this, "获取用户头像资料失败");
						}
					});
				} else {
					JSONObject object;
					try {
						object = new JSONObject(result);
						JSONArray array = object.getJSONArray("Entities");
						for (int i = 0; i < array.length(); i++) {
							final JSONObject userObject = array
									.getJSONObject(i);
							handler.post(new Runnable() {

								@Override
								public void run() {
									try {
										adapter.addPhotos(
												userObject
														.getString("NickName")
														.toLowerCase(),
												userObject
														.getString("PhotoUrl"));
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

package com.android.joocola.activity;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.chat.SingleChat;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 聊天界面 该界面用到了XUtils
 * 
 * @author lixiaosong
 * 
 */
public class ChatActivity extends Activity {
	/**
	 * 聊天窗口
	 */
	@ViewInject(R.id.chat_window_ll)
	private LinearLayout ll_chatWindow;
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
	 * 聊天窗口的滚动视图
	 */
	@ViewInject(R.id.sv_chat)
	private ScrollView sv_chat;
	private ChatReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		ViewUtils.inject(this);
		IntentFilter filter = new IntentFilter(Constans.CHAT_ACTION);
		receiver = new ChatReceiver();
		registerReceiver(receiver, filter);
	}

	public class ChatReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String from = intent.getStringExtra("from");
			// String to = intent.getStringExtra("to");
			String content = intent.getStringExtra("content");
			RelativeLayout layout = (RelativeLayout) LayoutInflater.from(
					ChatActivity.this).inflate(R.layout.item_chat_other, null);
			TextView name_tv = (TextView) layout.findViewById(R.id.chat_name);
			TextView content_tv = (TextView) layout
					.findViewById(R.id.chat_content);
			name_tv.setText(from);
			content_tv.setText(content);
			ll_chatWindow.addView(layout);
			scrollToBottom();

		}
	}

	@OnClick(R.id.send_btn)
	public void onClick(View v) {
		String content = et_content.getText().toString();
		if (content == null || content.equals("")) {
			Utils.toast(this, "输入内容不能为空");
			return;
		}
		// 得到会话
		Chat chat = SingleChat.getInstance().getFriendChat("test1", null);
		try {
			chat.sendMessage(content);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		// 增加一条发送成功的消息到本地窗口
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(
				ChatActivity.this).inflate(R.layout.item_chat_me, null);
		TextView name_tv = (TextView) layout.findViewById(R.id.chat_name);
		TextView content_tv = (TextView) layout.findViewById(R.id.chat_content);
		name_tv.setText(JoocolaApplication.getInstance().getUserInfo()
				.getNickName());
		content_tv.setText(content);
		ll_chatWindow.addView(layout);
		scrollToBottom();
		et_content.setText("");
	}

	/**
	 * 将滚动视图滑动到底端
	 */
	public void scrollToBottom() {
		sv_chat.post(new Runnable() {

			@Override
			public void run() {
				sv_chat.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}

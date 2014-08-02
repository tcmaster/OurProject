package com.android.joocola.activity;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.joocola.R;
import com.android.joocola.adapter.SingleChatAdapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.chat.SingleChat;
import com.android.joocola.chat.XMPPChat;
import com.android.joocola.entity.ChatOfflineInfo;
import com.android.joocola.utils.Constans;
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
	private ChatReceiver receiver;

	/**
	 * 判断该窗口是单人聊天窗口还是多人聊天窗口,默认是单人
	 */
	private boolean isSingle = true;
	/**
	 * 单人聊天时的聊天对象
	 */
	private String nickName = "test1";
	private SingleChatAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		ViewUtils.inject(this);
		initActionBar();
		if (isSingle) {
			IntentFilter filter = new IntentFilter(Constans.CHAT_ACTION);
			receiver = new ChatReceiver();
			registerReceiver(receiver, filter);
			adapter = new SingleChatAdapter(this, "test1");
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
		getActionBarTitle().setText("test1");
		getActionBarRight().setText("");

	}

	public class ChatReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			adapter.updateNoReadData();
			scrollBottom();
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
		ChatOfflineInfo info = new ChatOfflineInfo();
		info.setContent(content);
		info.setIsFrom(XMPPChat.getInstance().getConnection().getUser()
				.split("@")[0]);
		info.setIsTo("test1");
		info.setIsRead(0);
		try {
			JoocolaApplication.getInstance().getDB().save(info);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		adapter.updateNoReadData();
		scrollBottom();
		et_content.setText("");
	}

	@Override
	protected void onDestroy() {
		if (receiver != null)
			unregisterReceiver(receiver);
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
}

package com.android.joocola.chat;

import java.sql.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.MyChatInfo;
import com.android.joocola.utils.Constans;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public class EaseMobChat {

	private static EaseMobChat chatServic;
	private int flag = 0;

	private MyChatBroadCastReceiver receiver;
	private EaseSingleChat chat;
	private DbUtils db;

	public synchronized static EaseMobChat getInstance() {
		if (chatServic == null) {
			chatServic = new EaseMobChat();
		}
		return chatServic;
	}

	private EaseMobChat() {
		db = JoocolaApplication.getInstance().getDB();
	}

	public int getFlag() {
		return flag;
	}

	public void init(Context context) {
		EMChat.getInstance().init(context);
	}

	public synchronized void beginWork() {
		if (receiver == null)
			receiver = new MyChatBroadCastReceiver();
		chat = new EaseSingleChat();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		JoocolaApplication.getInstance().registerReceiver(receiver, intentFilter);
	}

	public synchronized void endWork() {
		if (receiver != null && chatServic != null) {
			JoocolaApplication.getInstance().unregisterReceiver(receiver);
			EMChatManager.getInstance().logout();
			chatServic = null;
		}
	}

	public void registerAccount(String name, String passwd) {
		new RegisterTask(name, passwd).execute();
	}

	public void sendTxtMessage(String userName, ChatType chatType, String content, final EMCallBack callBack) {
		chat.sendTextMessage(userName, chatType, content, callBack);
	}

	public void sendImgMessage(String userName, ChatType chatType, String content, final EMCallBack callBack) {
		chat.sendImageMessage(userName, chatType, content, callBack);
	}

	public void login(String name, String passwd) {
		EMChatManager.getInstance().login(name, passwd, new EMCallBack() {

			@Override
			public void onSuccess() {
				// 登录成功
				flag = -1;
				Log.e("lixiaosong", "登录成功");
			}

			@Override
			public void onProgress(int arg0, String arg1) {
			}

			@Override
			public void onError(int arg0, String arg1) {
				flag = 2;
			}
		});
	}

	// ===================注册任务=========================//
	private class RegisterTask extends AsyncTask<Void, Void, String> {

		private String name, passwd;

		public RegisterTask(String name, String passwd) {
			this.name = name;
			this.passwd = passwd;
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				EMChatManager.getInstance().createAccountOnServer(name, passwd);
			} catch (EaseMobException e) {
				return null;
			}
			return name;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				Toast.makeText(JoocolaApplication.getInstance(), "注册失败", Toast.LENGTH_SHORT).show();
			} else {
				flag = 1;
				login(name, passwd);
			}
			super.onPostExecute(result);
		}
	}

	private class MyChatBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			String content = "";
			String from = "";
			String localUrl = "";
			long time = 0l;
			switch (message.getType()) {
			case TXT:
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				content = txtBody.getMessage();
				break;
			case IMAGE:
				ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
				content = imgBody.getRemoteUrl();
				localUrl = imgBody.getThumbnailUrl();
				break;
			default:
				break;
			}
			from = message.getFrom();
			time = message.getMsgTime();
			List<MyChatInfo> temp = null;
			MyChatInfo info = new MyChatInfo();
			info.messageId = msgId;
			info.user = from;
			info.PID = JoocolaApplication.getInstance().getPID();
			// 接收到消息，说明有未读消息
			info.isRead = false;
			try {
				temp = db.findAll(Selector.from(MyChatInfo.class).where("user", "=", from));
				if (temp == null || temp.size() == 0) {
					db.save(info);
				} else {
					info = temp.get(0);
					info.messageId = message.getMsgId();
					db.update(info, WhereBuilder.b("user", "=", from), "messageId", "PID", "isRead");
				}
			} catch (DbException e) {
				e.printStackTrace();
			}
			Log.v("test", localUrl + " " + from + " " + new Date(time).toLocaleString());
			Intent chat = new Intent(Constans.CHAT_ACTION);
			JoocolaApplication.getInstance().sendBroadcast(chat);
		}

	}
}

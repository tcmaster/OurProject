package com.android.joocola.chat;

import java.sql.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.AdminMessage;
import com.android.joocola.entity.AdminMessageNotify;
import com.android.joocola.entity.MyChatInfo;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.JsonUtils;
import com.easemob.EMCallBack;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

/**
 * 聊天类，用于初始化聊天，注册登录，接收发送聊天信息
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-10-10
 */
public class EaseMobChat {

	private int flag = 0;
	/**
	 * 本类的单例
	 */
	private static EaseMobChat chatServic;
	/**
	 * 接收消息类
	 */
	private MyChatBroadCastReceiver receiver;
	/**
	 * 发送消息的工具类
	 */
	private EaseSingleChat chat;
	private DbUtils db;
	/**
	 * 管理员用户,该用户发送的消息要特殊处理
	 */
	private final String ADMIN_USER = "u1";
	private boolean DEBUG = true;

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

	public void sendSystemMessage(String userName, String content, ChatType chatType, final EMCallBack callBack) {
		chat.sendSystemMessage(userName, content, chatType, callBack);
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
				EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
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

	/**
	 * 用于处理系统发送过来的消息
	 * 
	 * @param message
	 * @author: LiXiaosong
	 * @date:2014-10-10
	 */
	private void processAdminMessage(EMMessage message) {
		String text = ((TextMessageBody) message.getBody()).getMessage();
		JSONObject object = null;
		try {
			// 先判断消息类型，在将消息转化为可以进行处理的实体
			object = new JSONObject(text);
			String msgType = object.getString("MsgType");
			if (msgType.equals("SM_Normal") || msgType.equals("SM_VerifyTalk")) {
				AdminMessage entity = null;
				entity = JsonUtils.getAdminMessageEntity(object);
				entity.setUser(JoocolaApplication.getInstance().getPID());
				entity.setUser(JoocolaApplication.getInstance().getPID());
				if (DEBUG)
					LogUtils.v("解析完成后的聊天消息为" + entity.toString());
				// 将解析好的消息存入数据库
				db.save(entity);
				// 发送广播通知有更新
				Intent intent = new Intent(Constants.CHAT_ADMIN_ACTION);
				JoocolaApplication.getInstance().sendBroadcast(intent);
			} else if (msgType.equals("PM_Normal")) {
				final AdminMessageNotify entity = JsonUtils.getAdminMessageNotifyEntity(object);
				LogUtils.v("实体的值为" + entity.toString());
				// 直接向同意方发送一条消息
				sendSystemMessage("u" + entity.getTalkFromUserID(), entity.getMsgContent(), ChatType.Chat, new EMCallBack() {

					@Override
					public void onSuccess() {
						// 消息发送成功以后，将该条聊天数据存入数据库，代表有数据
						// 发送成功，发送的消息需要显示在聊天会话上
						if (DEBUG)
							LogUtils.v("发送消息成功");
						MyChatInfo info = new MyChatInfo();
						EMConversation conversation = EMChatManager.getInstance().getConversation("u" + entity.getTalkFromUserID());
						EMMessage message = conversation.getLastMessage();
						info.messageId = message.getMsgId();
						info.user = "u" + entity.getTalkFromUserID();
						info.isRead = false;
						info.PID = JoocolaApplication.getInstance().getPID();
						info.chatType = message.getChatType() == ChatType.GroupChat ? Constants.CHAT_TYPE_MULTI : Constants.CHAT_TYPE_SINGLE;
						List<MyChatInfo> temp = null;
						try {
							temp = db.findAll(Selector.from(MyChatInfo.class).where("user", "=", "u" + entity.getTalkFromUserID()).and("PID", "=", info.PID));
							if (temp == null || temp.size() == 0) {
								db.save(info);
							} else {
								info = temp.get(0);
								info.messageId = message.getMsgId();
								db.update(info, WhereBuilder.b("user", "=", "u" + entity.getTalkFromUserID()).and("PID", "=", info.PID), "messageId", "isRead");
							}
						} catch (DbException e) {
							e.printStackTrace();
						}
						Intent chat = new Intent(Constants.CHAT_ACTION);
						JoocolaApplication.getInstance().sendBroadcast(chat);
					}

					@Override
					public void onProgress(int arg0, String arg1) {
					}

					@Override
					public void onError(int arg0, String arg1) {
					}
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (DbException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 消息接收广播
	 * 
	 * @author:LiXiaoSong
	 * @copyright © joocola.com
	 * @Date:2014-10-10
	 */
	private class MyChatBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			if (DEBUG)
				LogUtils.v("收到了一条聊天消息");
			if (message.getFrom().equals(ADMIN_USER)) {
				if (DEBUG)
					LogUtils.v("聊天消息来自admin");
				processAdminMessage(message);
				return;
			}
			processMessage(message);
		}
	}

	/**
	 * 对接收到的某条消息的处理
	 * 
	 * @author: LiXiaosong
	 * @date:2014-10-14
	 */
	private void processMessage(EMMessage message) {
		if (message == null) {
			LogUtils.v("收到一条消息，不过是空值");
			return;
		}
		LogUtils.v("原生的消息为" + message.toString());
		String content = "";
		// 发送消息的对象，单聊时为消息发送者，群聊时为接收的group
		String user = "";
		String localUrl = "";
		String chatType = "";
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
		time = message.getMsgTime();
		List<MyChatInfo> temp = null;
		MyChatInfo info = new MyChatInfo();
		info.messageId = message.getMsgId();

		info.PID = JoocolaApplication.getInstance().getPID();
		if (message.getChatType() == ChatType.Chat) {
			info.chatType = Constants.CHAT_TYPE_SINGLE;
			// 个人ID
			user = message.getFrom();
		} else if (message.getChatType() == ChatType.GroupChat) {
			info.chatType = Constants.CHAT_TYPE_MULTI;
			// 群ID
			user = message.getTo();
		}
		// 接收到消息，说明有未读消息
		info.isRead = false;
		info.user = user;
		try {
			temp = db.findAll(Selector.from(MyChatInfo.class).where("user", "=", user).and("PID", "=", info.PID));
			if (temp == null || temp.size() == 0) {
				db.save(info);
			} else {
				info.messageId = message.getMsgId();
				db.update(info, WhereBuilder.b("user", "=", user).and("PID", "=", info.PID), "messageId", "isRead");
				temp = db.findAll(Selector.from(MyChatInfo.class).where("user", "=", user).and("PID", "=", info.PID));
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		LogUtils.v("收到的消息是" + localUrl + " " + user + " " + new Date(time).toLocaleString());
		Intent chat = new Intent(Constants.CHAT_ACTION);
		JoocolaApplication.getInstance().sendBroadcast(chat);
	}

	/**
	 * 连接状态监听,目前没有效果，仅作为备用
	 * 
	 * @author:LiXiaoSong
	 * @copyright © joocola.com
	 * @Date:2014-10-10
	 */
	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {

		}

		@Override
		public void onConnecting(String arg0) {
		}

		@Override
		public void onDisConnected(String arg0) {

		}

		@Override
		public void onReConnected() {

		}

		@Override
		public void onReConnecting() {

		}

	}
}

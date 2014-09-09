package com.example.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.example.huanxindemo.HuanXinApp;

/**
 * 环信聊天类
 * 
 * @author lixiaosong
 * 
 */
public class EaseMobChat {
	/**
	 * 本类的单例
	 */
	private static EaseMobChat chatServic;
	/**
	 * 本类初始化的标志，用于标记当前进行到了哪一步 0为未初始化，1为进行注册完毕，2为登录完毕，3为所有初始化完成，-1为初始化失败
	 */
	private int flag = 0;
	/**
	 * 聊天消息监听
	 */
	private MyChatBroadCastReceiver receiver;

	public synchronized static EaseMobChat getInstance() {
		if (chatServic == null) {
			chatServic = new EaseMobChat();
		}
		return chatServic;
	}

	private EaseMobChat() {
	}

	/**
	 * 初始化环信聊天SDK，这个方法建议在APP中调用
	 */
	public void init(Context context) {
		EMChat.getInstance().init(context);
	}

	/**
	 * 开启聊天相关内容
	 */
	public void beginWork() {
		if (receiver == null)
			receiver = new MyChatBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		HuanXinApp.getSelf().registerReceiver(receiver, intentFilter);
	}

	/**
	 * 停止聊天相关内容
	 */
	public void endWork() {
		if (receiver != null) {
			HuanXinApp.getSelf().unregisterReceiver(receiver);
		}
	}

	/**
	 * 注册账号
	 * 
	 * @param name
	 *            注册的用户名
	 * @param passwd
	 *            注册的密码
	 */
	public void registerAccount(String name, String passwd) {
		new RegisterTask(name, passwd).execute();
	}

	/**
	 * 登录
	 * 
	 * @param name
	 *            用户名
	 * @param passwd
	 *            密码
	 */
	public void login(String name, String passwd) {
		EMChatManager.getInstance().login(name, passwd, new EMCallBack() {

			@Override
			public void onSuccess() {
				// 登录成功
				flag = 2;
			}

			@Override
			public void onProgress(int arg0, String arg1) {
			}

			@Override
			public void onError(int arg0, String arg1) {
				flag = -1;
			}
		});
	}

	// ===================内部类=========================//
	private class RegisterTask extends AsyncTask<Void, Void, String> {
		/**
		 * 注册的账号和密码
		 */
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
				Log.e("lixiaosong", "貌似注册出现了问题");
				return null;
			}
			return name;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				Toast.makeText(HuanXinApp.getSelf(), "注册失败", Toast.LENGTH_SHORT)
						.show();
			} else {
				flag = 1;
			}
			super.onPostExecute(result);
		}
	}

	private class MyChatBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			/**
			 * 暂时接收图片的类型就这两种
			 */
			String content = "";
			String from = "";
			switch (message.getType()) {
			case TXT:
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				break;
			case IMAGE:
				ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
				break;
			default:
				break;
			}

		}

	}
}

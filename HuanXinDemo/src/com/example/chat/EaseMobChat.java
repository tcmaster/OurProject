package com.example.chat;

import java.sql.Date;

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
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.example.huanxindemo.HuanXinApp;

/**
 * ����������
 * 
 * @author lixiaosong
 * 
 */
public class EaseMobChat {
	/**
	 * ����ĵ���
	 */
	private static EaseMobChat chatServic;
	/**
	 * �����ʼ���ı�־�����ڱ�ǵ�ǰ���е�����һ��
	 * 0Ϊδ��ʼ����1Ϊ����ע����ϣ�2Ϊ��¼��ϣ�3Ϊ���г�ʼ����ɣ�-1Ϊ��ʼ��ʧ��
	 */
	private int flag = 0;
	/**
	 * ������Ϣ����
	 */
	private MyChatBroadCastReceiver receiver;
	private SingleChat chat;

	public synchronized static EaseMobChat getInstance() {
		if (chatServic == null) {
			chatServic = new EaseMobChat();
		}
		return chatServic;
	}

	private EaseMobChat() {
	}

	/**
	 * ��ʼ����������SDK���������������APP�е���
	 */
	public void init(Context context) {
		EMChat.getInstance().init(context);
	}

	/**
	 * ���������������
	 */
	public void beginWork() {
		if (receiver == null)
			receiver = new MyChatBroadCastReceiver();
		chat = new SingleChat();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		HuanXinApp.getSelf().registerReceiver(receiver, intentFilter);
	}

	/**
	 * ֹͣ�����������
	 */
	public void endWork() {
		if (receiver != null) {
			HuanXinApp.getSelf().unregisterReceiver(receiver);
		}
	}

	/**
	 * ע���˺�
	 * 
	 * @param name
	 *            ע����û���
	 * @param passwd
	 *            ע�������
	 */
	public void registerAccount(String name, String passwd) {
		new RegisterTask(name, passwd).execute();
	}

	public void sendTxtMessage(String userName, ChatType chatType,
			String content, final EMCallBack callBack) {
		chat.sendTextMessage(userName, chatType, content, callBack);
	}

	public void sendImgMessage(String userName, ChatType chatType,
			String content, final EMCallBack callBack) {
		chat.sendImageMessage(userName, chatType, content, callBack);
	}

	/**
	 * ��¼
	 * 
	 * @param name
	 *            �û���
	 * @param passwd
	 *            ����
	 */
	public void login(String name, String passwd) {
		EMChatManager.getInstance().login(name, passwd, new EMCallBack() {

			@Override
			public void onSuccess() {
				// ��¼�ɹ�
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

	// ===================�ڲ���=========================//
	private class RegisterTask extends AsyncTask<Void, Void, String> {
		/**
		 * ע����˺ź�����
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
				Log.e("lixiaosong", "ò��ע�����������");
				return null;
			}
			return name;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				Toast.makeText(HuanXinApp.getSelf(), "ע��ʧ��",
						Toast.LENGTH_SHORT).show();
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
			/**
			 * ��ʱ����ͼƬ�����;�������
			 */
			String content = "";
			String from = "";
			long time = 0l;
			switch (message.getType()) {
			case TXT:
				TextMessageBody txtBody = (TextMessageBody) message.getBody();
				content = txtBody.getMessage();
				break;
			case IMAGE:
				ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
				content = imgBody.getRemoteUrl();
				break;
			default:
				break;
			}
			from = message.getFrom();
			time = message.getMsgTime();
			Log.v("test",
					content + " " + from + " "
							+ new Date(time).toLocaleString());
		}

	}
}

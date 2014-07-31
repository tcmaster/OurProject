package com.android.joocola.chat;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import android.content.Intent;
import android.util.Log;

import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.utils.Constans;

/**
 * 聊天信息接收监听器
 * 
 * @author lixiaosong
 * 
 */
public class UserChatListener implements ChatManagerListener {

	@Override
	public void chatCreated(Chat arg0, boolean arg1) {
		arg0.addMessageListener(new MessageListener() {

			@Override
			public void processMessage(Chat arg0, Message arg1) {
				Log.v("lixiaosong", arg1.getFrom());
				Log.v("lixiaosong", arg1.getTo());
				// 将聊天内容，从哪来，到哪去以广播形式发送到相应的activity
				Log.v("lixiaosong", arg1.getBody());
				// 将这次聊天内容发送出去
				Intent intent = new Intent(Constans.CHAT_ACTION);
				intent.putExtra("from",
						arg1.getFrom()
								.substring(0, arg1.getFrom().indexOf("@")));
				intent.putExtra("to",
						arg1.getTo().substring(0, arg1.getTo().indexOf("@")));
				intent.putExtra("content", arg1.getBody());
				JoocolaApplication.getInstance().sendBroadcast(intent);
			}
		});
	}

}

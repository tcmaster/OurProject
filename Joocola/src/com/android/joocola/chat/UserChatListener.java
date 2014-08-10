package com.android.joocola.chat;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import android.content.Intent;
import android.util.Log;

import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.ChatOfflineInfo;
import com.android.joocola.utils.Constans;
import com.lidroid.xutils.exception.DbException;

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
				Log.v("lixiaosong", arg1.getBody());
				Intent intent = new Intent(Constans.CHAT_ACTION);
				ChatOfflineInfo info = new ChatOfflineInfo();
				info.setContent(arg1.getBody());
				info.setIsFrom(arg1.getFrom().substring(0,
						arg1.getFrom().indexOf("@")));
				info.setIsTo(arg1.getTo().substring(0,
						arg1.getTo().indexOf("@")));
				info.setKey(info.getIsTo() + "-" + info.getIsFrom());
				info.setIsRead(0);
				info.setUser(JoocolaApplication.getInstance().getUserInfo()
						.getUserName());
				try {
					JoocolaApplication.getInstance().getDB().save(info);
				} catch (DbException e) {
					e.printStackTrace();
				}
				JoocolaApplication.getInstance().sendBroadcast(intent);
			}
		});
	}

}

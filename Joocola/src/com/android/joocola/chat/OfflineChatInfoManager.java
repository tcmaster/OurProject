package com.android.joocola.chat;

import java.util.Iterator;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.OfflineMessageManager;

import android.util.Log;

import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.ChatOfflineInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

/**
 * 离线消息的管理类，负责获取当前用户的离线消息，将离线消息存入数据库，待日后使用
 * 
 * @author lixiaosong
 * 
 */
public class OfflineChatInfoManager {
	/**
	 * 得到离线消息
	 */
	public static void getOfflineInfo() {
		OfflineMessageManager manager = new OfflineMessageManager(XMPPChat
				.getInstance().getConnection());
		DbUtils db = JoocolaApplication.getInstance().getDB();
		try {
			Iterator<Message> it = manager.getMessages();
			while (it.hasNext()) {
				Message msg = it.next();
				String from = msg.getFrom().split("@")[0];
				String to = msg.getTo().split("@")[0];
				String content = msg.getBody();
				ChatOfflineInfo info = new ChatOfflineInfo();
				info.setIsFrom(from);
				info.setIsTo(to);
				info.setContent(content);
				info.setIsRead(0);
				info.setKey(info.getIsTo() + "-" + info.getIsFrom());
				info.setUser(JoocolaApplication.getInstance().getUserInfo()
						.getUserName());
				Log.v("lixiaosong", "离线消息是" + info.toString());
				db.save(info);
			}
			manager.deleteMessages();
		} catch (DbException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}
}

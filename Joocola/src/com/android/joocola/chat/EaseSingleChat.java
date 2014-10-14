package com.android.joocola.chat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.MessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;

/**
 * 聊天消息发送类，主要用于发送由本地产生的消息信息
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-10-10
 */
public class EaseSingleChat {

	/**
	 * 用于保存当前聊天的句柄
	 */
	public Map<String, EMConversation> conversations;

	public EaseSingleChat() {
		conversations = new HashMap<String, EMConversation>();
	}

	/**
	 * 发送文字消息
	 * 
	 * @param userName
	 *            要发的用户/群聊的id
	 * @param chatType
	 *            类型，单聊还是群聊
	 * @param content
	 *            发送的消息文件内容/文件的路径
	 * @param callBack
	 *            消息发送状态的回调
	 */
	public void sendTextMessage(String userName, ChatType chatType, String content, final EMCallBack callBack) {
		sendMessage(userName, chatType, Type.TXT, content, callBack);
	}

	/**
	 * 发送图片消息
	 * 
	 * @param userName
	 *            要发的用户/群聊的id
	 * @param chatType
	 *            类型，单聊还是群聊
	 * @param content
	 *            发送的消息文件内容/文件的路径
	 * @param callBack
	 *            消息发送状态的回调
	 */
	public void sendImageMessage(String userName, ChatType chatType, String content, final EMCallBack callBack) {
		sendMessage(userName, chatType, Type.IMAGE, content, callBack);
	}

	/**
	 * 发送声音消息
	 * 
	 * @param userName
	 *            要发的用户/群聊的id
	 * @param chatType
	 *            类型，单聊还是群聊
	 * @param content
	 *            发送的消息文件内容/文件的路径
	 * @param callBack
	 *            消息发送状态的回调
	 */
	public void sendVoiceMessage(String userName, ChatType chatType, String content, final EMCallBack callBack) {
		sendMessage(userName, chatType, Type.VOICE, content, callBack);
	}

	/**
	 * 发送系统消息
	 * 
	 * @param userName
	 *            要发的用户/群聊的id
	 * @param content
	 *            发送的系统消息内容
	 * @param callBack
	 *            消息发送状态的回调
	 * @author: LiXiaosong
	 * @date:2014-10-14
	 */
	public void sendSystemMessage(String userName, String content, ChatType chatType, final EMCallBack callBack) {
		EMConversation conversation = getConversation(userName);
		EMMessage message = EMMessage.createSendMessage(Type.TXT);
		message.setChatType(chatType);
		MessageBody body = new TextMessageBody(content);
		message.addBody(body);
		message.setReceipt(userName);
		// 重要，这个标记了该消息是系统消息
		message.setAttribute("isSystem", true);
		conversation.addMessage(message);
		if (chatType == chatType.Chat) {
			EMChatManager.getInstance().sendMessage(message, callBack);
		} else {
			EMChatManager.getInstance().sendGroupMessage(message, callBack);
		}
	}

	/**
	 * 发送地理位置消息
	 * 
	 * @param userName
	 *            要发的用户/群聊的id
	 * @param chatType
	 *            类型，单聊还是群聊
	 * @param content
	 *            发送的消息文件内容/文件的路径
	 * @param callBack
	 *            消息发送状态的回调
	 */
	public void sendLocationMessage(String userName, ChatType chatType, String content, final EMCallBack callBack) {
		sendMessage(userName, chatType, Type.LOCATION, content, callBack);
	}

	/**
	 * 发送文件消息
	 * 
	 * @param userName
	 *            要发的用户/群聊的id
	 * @param chatType
	 *            类型，单聊还是群聊
	 * @param content
	 *            发送的消息文件内容/文件的路径
	 * @param callBack
	 *            消息发送状态的回调
	 */
	public void sendFileMessage(String userName, ChatType chatType, String content, final EMCallBack callBack) {
		sendMessage(userName, chatType, Type.FILE, content, callBack);
	}

	/**
	 * 发送消息
	 * 
	 * @param userName
	 *            要发的用户/群聊的id
	 * @param chatType
	 *            类型，单聊还是群聊
	 * @param type
	 *            发送的消息类型
	 * @param content
	 *            发送的消息文件内容/文件的路径
	 * @param callBack
	 *            消息发送状态的回调
	 */
	private void sendMessage(String userName, ChatType chatType, Type type, String content, final EMCallBack callBack) {
		EMConversation conversation = getConversation(userName);
		EMMessage message = EMMessage.createSendMessage(type);
		message.setChatType(chatType);
		MessageBody body = null;
		switch (type) {
		case TXT:
			body = new TextMessageBody(content);
			break;
		case IMAGE:
			body = new ImageMessageBody(new File(content));
			break;
		case LOCATION:
			// 暂时用不到
			// body = new LocationMessageBody(arg0, arg1, arg2)
			break;
		case FILE:
			body = new NormalFileMessageBody(new File(content));
			break;
		case VOICE:
			// 暂时用不到
			// body = new VoiceMessageBody(arg0, arg1)
			break;
		default:
			break;
		}
		message.addBody(body);
		message.setReceipt(userName);
		conversation.addMessage(message);
		if (chatType == chatType.Chat) {
			EMChatManager.getInstance().sendMessage(message, callBack);
		} else {
			EMChatManager.getInstance().sendGroupMessage(message, callBack);
		}
	}

	/**
	 * 得到当前会话
	 * 
	 * @param userName
	 *            当前会话的用户id/群组id
	 * @return
	 */
	private EMConversation getConversation(String userName) {
		EMConversation conversation = conversations.get(userName);
		if (conversation == null) {
			conversation = EMChatManager.getInstance().getConversation(userName);
			conversations.put(userName, conversation);
		}
		return conversation;
	}
}

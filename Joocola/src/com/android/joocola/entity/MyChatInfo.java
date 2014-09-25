package com.android.joocola.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * 
 * 用于存放聊天消息的相关信息（用于在消息列表使用）
 * 
 * @author lixiaosong
 * 
 */
public class MyChatInfo {

	/**
	 * 主键
	 */
	@Id
	public int id;
	/**
	 * 会话用户/组用户
	 */
	public String user;
	/**
	 * 最后一条消息的id
	 */
	public String messageId;
	/**
	 * 该条消息记录所属的用户
	 */
	public String PID;
	/**
	 * 该条消息的类型,取值见Constance类
	 */
	public String chatType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getPID() {
		return PID;
	}

	public void setPID(String pID) {
		PID = pID;
	}

	public String getChatType() {
		return chatType;
	}

	public void setChatType(String chatType) {
		this.chatType = chatType;
	}

	@Override
	public String toString() {
		return "MyChatInfo [id=" + id + ", user=" + user + ", messageId=" + messageId + ", PID=" + PID + ", chatType=" + chatType + "]";
	}

}

package com.android.joocola.entity;

/**
 * 未读消息暂存 将首次登录后收藏到的未读消息存入数据库， 当收到未读消息之后， 将消息进行显示， 之后将未读消息标记 该实体为未读消息存储的实体，
 * 本记录只保证单一对一的消息记录
 * 
 * @author lixiaosong
 * 
 */
public class ChatOfflineInfo {
	/**
	 * 主键
	 */
	public int id;
	/**
	 * 未读消息来自
	 */
	public String isFrom;
	/**
	 * 未读消息到
	 */
	public String isTo;
	/**
	 * 未读消息的内容
	 */
	public String content;
	/**
	 * 未读消息标志位,如果为1，就为已读，为0，仍然未读
	 */
	public int isRead;
	/**
	 * 用于标识一组聊天，组成：登陆User-其他聊天对象
	 */
	public String key;
	/**
	 * 用于标识是哪个用户的聊天记录
	 */
	public String user;
	/**
	 * 该条消息的接收/发送时间
	 */
	public String time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIsFrom() {
		return isFrom;
	}

	public void setIsFrom(String isFrom) {
		this.isFrom = isFrom;
	}

	public String getIsTo() {
		return isTo;
	}

	public void setIsTo(String isTo) {
		this.isTo = isTo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "ChatOfflineInfo [id=" + id + ", isFrom=" + isFrom + ", isTo="
				+ isTo + ", content=" + content + ", isRead=" + isRead
				+ ", key=" + key + ", user=" + user + ", time=" + time + "]";
	}
}

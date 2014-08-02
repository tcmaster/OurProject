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

	@Override
	public String toString() {
		return "ChatOfflineInfo [id=" + id + ", isFrom=" + isFrom + ", isTo="
				+ isTo + ", content=" + content + ", isRead=" + isRead + "]";
	}

}

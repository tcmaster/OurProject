package com.android.joocola.entity;

/**
 * 多人聊天数据
 * 
 * @author lixiaosong
 * 
 */
public class MultiChatInfo {
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
	 * 用于标识一组聊天，值为该邀约的id
	 */
	public String multikey;
	/**
	 * 用于标识是哪个用户的聊天记录
	 */
	public String user;
	/**
	 * 该条消息的接收/发送时间
	 */
	public String time;
	/**
	 * 该字段如果不为""，说明该跳消息是图片消息,该字段代表该图片的本地地址
	 */
	public String imgUrl;

	public int getId() {
		return id;
	}

	public String getIsFrom() {
		return isFrom;
	}

	public String getIsTo() {
		return isTo;
	}

	public String getContent() {
		return content;
	}

	public int getIsRead() {
		return isRead;
	}

	public String getMultikey() {
		return multikey;
	}

	public String getUser() {
		return user;
	}

	public String getTime() {
		return time;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIsFrom(String isFrom) {
		this.isFrom = isFrom;
	}

	public void setIsTo(String isTo) {
		this.isTo = isTo;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public void setMultikey(String multikey) {
		this.multikey = multikey;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public String toString() {
		return "MultiChatInfo [id=" + id + ", isFrom=" + isFrom + ", isTo="
				+ isTo + ", content=" + content + ", isRead=" + isRead
				+ ", multikey=" + multikey + ", user=" + user + ", time="
				+ time + ", imgUrl=" + imgUrl + "]";
	}

}

package com.android.joocola.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * 离线消息暂存 将首次登录后收藏到的离线消息存入数据库， 当收到离线消息之后， 将消息进行显示， 之后将离线消息删除 该实体为离线消息存储的实体，
 * 本记录只保证单一对一的消息记录
 * 
 * @author lixiaosong
 * 
 */
public class ChatOfflineInfo {
	/**
	 * 主键
	 */
	@Id
	public int _id;
	/**
	 * 离线消息来自
	 */
	public String from;
	/**
	 * 离线消息的内容
	 */
	public String content;

	public int get_id() {
		return _id;
	}

	public String getFrom() {
		return from;
	}

	public String getContent() {
		return content;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ChatOfflineInfo [_id=" + _id + ", from=" + from + ", content="
				+ content + "]";
	}

}

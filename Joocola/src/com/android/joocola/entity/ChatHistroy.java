package com.android.joocola.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * 聊天历史记录，仅保存最后的20条历史记录
 * 
 * @author lixiaosong
 * 
 */
public class ChatHistroy {
	/**
	 * 主键
	 */
	@Id
	public int _id;
	/**
	 * 这条消息从谁发出
	 */
	public String from;
	/**
	 * 这条消息的去向
	 */
	public String to;
	/**
	 * 这条消息的内容
	 */
	public String content;
	/**
	 * 这条消息的顺序，从1到20
	 */
	public String sequence;

	public int get_id() {
		return _id;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getContent() {
		return content;
	}

	public String getSequence() {
		return sequence;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "ChatHistroy [_id=" + _id + ", from=" + from + ", to=" + to
				+ ", content=" + content + ", sequence=" + sequence + "]";
	}

}

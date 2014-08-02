package com.android.joocola.entity;

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
	public int id;
	/**
	 * 这条消息从谁发出
	 */
	public String isFrom;
	/**
	 * 这条消息的去向
	 */
	public String isTo;
	/**
	 * 这条消息的内容
	 */
	public String content;
	/**
	 * 这条消息的顺序，从1到20
	 */
	public String sequence;

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

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "ChatHistroy [id=" + id + ", isFrom=" + isFrom + ", isTo="
				+ isTo + ", content=" + content + ", sequence=" + sequence
				+ "]";
	}

}

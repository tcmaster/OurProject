package com.android.joocola.entity;

public class ReplyEntity {
	private int replypid;// 评论的pid
	private String content;// 评论内容
	private int PublisherID;// 评论者id
	private String publisherName; // 评论者姓名
	private String publisherPhotoString;// 评论者头像
	private String PublishDate;// 评论发布日期

	public int getReplypid() {
		return replypid;
	}

	public void setReplypid(int replypid) {
		this.replypid = replypid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getPublisherID() {
		return PublisherID;
	}

	public void setPublisherID(int publisherID) {
		PublisherID = publisherID;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getPublisherPhotoString() {
		return publisherPhotoString;
	}

	public void setPublisherPhotoString(String publisherPhotoString) {
		this.publisherPhotoString = publisherPhotoString;
	}

	public String getPublishDate() {
		return PublishDate;
	}

	public void setPublishDate(String publishDate) {
		PublishDate = publishDate;
	}

}

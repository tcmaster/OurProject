package com.android.joocola.entity;

public class AppointScoreEntity {
	private String pid; // 评论的pid
	private String appointTitle;// 邀约标题，需设置”NeedAppointInfo”
	private String ReserveDateStr;// 邀约时间
	private String fromUserIDString;// 评价者PID
	private String FromUserName;// 评价者姓名
	private String FromUserPhoto;// 评价者头像地址
	private String ToUserID;// 被评价者PID
	private String ToUserName;// 被评价者姓名
	private String ToUserPhoto;// 被评价者头像地址
	/**
	 * 得分 好评=10 中评=20 差评=30
	 */
	private String ScoreID;

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getAppointTitle() {
		return appointTitle;
	}

	public void setAppointTitle(String appointTitle) {
		this.appointTitle = appointTitle;
	}

	public String getReserveDateStr() {
		return ReserveDateStr;
	}

	public void setReserveDateStr(String reserveDateStr) {
		ReserveDateStr = reserveDateStr;
	}

	public String getFromUserIDString() {
		return fromUserIDString;
	}

	public void setFromUserIDString(String fromUserIDString) {
		this.fromUserIDString = fromUserIDString;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public String getFromUserPhoto() {
		return FromUserPhoto;
	}

	public void setFromUserPhoto(String fromUserPhoto) {
		FromUserPhoto = fromUserPhoto;
	}

	public String getToUserID() {
		return ToUserID;
	}

	public void setToUserID(String toUserID) {
		ToUserID = toUserID;
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getToUserPhoto() {
		return ToUserPhoto;
	}

	public void setToUserPhoto(String toUserPhoto) {
		ToUserPhoto = toUserPhoto;
	}

	public String getScoreID() {
		return ScoreID;
	}

	public void setScoreID(String scoreID) {
		ScoreID = scoreID;
	}

	public String getComment() {
		return Comment;
	}

	public void setComment(String comment) {
		Comment = comment;
	}

	public String getCommentDateStr() {
		return CommentDateStr;
	}

	public void setCommentDateStr(String commentDateStr) {
		CommentDateStr = commentDateStr;
	}

	private String Comment;// 评价内容
	private String CommentDateStr;// 评价时间
}

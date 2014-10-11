package com.android.joocola.entity;

/**
 * 系统消息存放实体
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-10-10
 */
public class AdminMessage {

	/**
	 * 系统消息类型
	 */
	private String MsgType;
	/**
	 * 发送消息的用户ID(为1的话，则不需要显示头像)
	 */
	private String RelateUserID;
	/**
	 * 发送消息用户的昵称
	 */
	private String RelateUserName;
	/**
	 * 系统发送消息用户的头像
	 */
	private String RelateUserPhoto;
	/**
	 * 系统发送消息的内容
	 */
	private String MsgContent;
	/**
	 * 接收用户的ID
	 */
	private String RecUserID;
	/**
	 * 发送消息的日期
	 */
	private String SendDate;

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getRelateUserID() {
		return RelateUserID;
	}

	public void setRelateUserID(String relateUserID) {
		RelateUserID = relateUserID;
	}

	public String getRelateUserName() {
		return RelateUserName;
	}

	public void setRelateUserName(String relateUserName) {
		RelateUserName = relateUserName;
	}

	public String getRelateUserPhoto() {
		return RelateUserPhoto;
	}

	public void setRelateUserPhoto(String relateUserPhoto) {
		RelateUserPhoto = relateUserPhoto;
	}

	public String getMsgContent() {
		return MsgContent;
	}

	public void setMsgContent(String msgContent) {
		MsgContent = msgContent;
	}

	public String getRecUserID() {
		return RecUserID;
	}

	public void setRecUserID(String recUserID) {
		RecUserID = recUserID;
	}

	public String getSendDate() {
		return SendDate;
	}

	public void setSendDate(String sendDate) {
		SendDate = sendDate;
	}

	@Override
	public String toString() {
		return "AdminMessage [MsgType=" + MsgType + ", RelateUserID=" + RelateUserID + ", RelateUserName=" + RelateUserName + ", RelateUserPhoto=" + RelateUserPhoto + ", MsgContent=" + MsgContent + ", RecUserID=" + RecUserID + ", SendDate=" + SendDate + "]";
	}

}

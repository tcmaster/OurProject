package com.android.joocola.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * 系统消息存放实体
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-10-10
 */
public class AdminMessage {

	/**
	 * 数据库所用id
	 */
	@Id
	private String _id;
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
	/**
	 * 按钮类型
	 */
	private String Caption;
	/**
	 * 按钮连接的地址
	 */
	private String CallUrl;
	/**
	 * 当前接收消息的用户
	 */
	private String user;

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

	public String getCaption() {
		return Caption;
	}

	public void setCaption(String caption) {
		Caption = caption;
	}

	public String getCallUrl() {
		return CallUrl;
	}

	public void setCallUrl(String callUrl) {
		CallUrl = callUrl;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "AdminMessage [_id=" + _id + ", MsgType=" + MsgType + ", RelateUserID=" + RelateUserID + ", RelateUserName=" + RelateUserName + ", RelateUserPhoto=" + RelateUserPhoto + ", MsgContent=" + MsgContent + ", RecUserID=" + RecUserID + ", SendDate=" + SendDate + ", Caption=" + Caption + ", CallUrl=" + CallUrl + ", user=" + user + "]";
	}

}

package com.android.joocola.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * 私聊信息的实体类
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-10-14
 */
public class AdminMessageNotify {

	/**
	 * 数据库所用id
	 */
	@Id
	private int id;
	/**
	 * 系统消息类型
	 */
	private String MsgType;
	/**
	 * 请求聊天的用户名
	 */
	private String TalkFromUserID;
	/**
	 * 接收聊天的用户名
	 */
	private String TalkRevUserID;
	/**
	 * 消息的内容
	 */
	private String MsgContent;
	/**
	 * 接收到的用户id
	 */
	private String RecUserID;
	/**
	 * 接收消息的日期
	 */
	private String SendDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getTalkFromUserID() {
		return TalkFromUserID;
	}

	public void setTalkFromUserID(String talkFromUserID) {
		TalkFromUserID = talkFromUserID;
	}

	public String getTalkRevUserID() {
		return TalkRevUserID;
	}

	public void setTalkRevUserID(String talkRevUserID) {
		TalkRevUserID = talkRevUserID;
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
		return "AdminMessageNotify [id=" + id + ", MsgType=" + MsgType + ", TalkFromUserID=" + TalkFromUserID + ", TalkRevUserID=" + TalkRevUserID + ", MsgContent=" + MsgContent + ", RecUserID=" + RecUserID + ", SendDate=" + SendDate + "]";
	}

}

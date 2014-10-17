package com.android.joocola.entity;

import java.io.Serializable;

import com.lidroid.xutils.db.annotation.Id;

/**
 * 新的系统消息实体
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-10-17
 */
public class NAdminMsgEntity implements Serializable {

	/**
	 * 为了进行传输所用的ID
	 */
	private static final long serialVersionUID = 1L;
	@Id
	public int id;
	/**
	 * 消息类型
	 */
	public String MsgType;
	/**
	 * 消息内容(具体是哪个界面的）
	 */
	public String MsgContent;
	/**
	 * 接收的用户
	 */
	public String RecUserID;
	/**
	 * 发送日期
	 */
	public String SendDate;
	/**
	 * 该消息所属的用户，查询的时候一定要带上，否则会把所有用户的数据删掉
	 */
	public String PID;

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPID() {
		return PID;
	}

	public void setPID(String pID) {
		PID = pID;
	}

	@Override
	public String toString() {
		return "NAdminMsgEntity [id=" + id + ", MsgType=" + MsgType + ", MsgContent=" + MsgContent + ", RecUserID=" + RecUserID + ", SendDate=" + SendDate + ", PID=" + PID + "]";
	}

}

package com.android.joocola.entity;

/**
 * 
 * @author:bb
 * @see:
 * @since:
 * @copyright © joocola.com
 * @Date:2014年10月19日
 */
public class AdminMessageContentEntity {

	/**
	 * 消息类型 [Description("邀约动态 报名")] App_Apply = 10, [Description("邀约动态 回复")] App_Reply = 11,
	 * [Description("邀约动态 邀请")] App_Invite = 12, [Description("邀约动态 评论")] App_Comments = 13
	 */

	private int MsgTypeId;
	/**
	 * 其中的button
	 */
	private AdminMessageContentButtonEntity adminMessageContentButtonEntity;
	/**
	 * 发送者的时间
	 */
	private String senderDateStr;
	/**
	 * 发送者的年龄
	 */
	private String SenderAge;
	/**
	 * 发送者的性别
	 */
	private String SenderSexID;
	/**
	 * 发送者性别 是否是女性
	 */
	private boolean SenderSexIsFemale;

	public int getMsgTypeId() {
		return MsgTypeId;
	}

	public void setMsgTypeId(int msgTypeId) {
		MsgTypeId = msgTypeId;
	}

	public AdminMessageContentButtonEntity getAdminMessageContentButtonEntity() {
		return adminMessageContentButtonEntity;
	}

	public void setAdminMessageContentButtonEntity(AdminMessageContentButtonEntity adminMessageContentButtonEntity) {
		this.adminMessageContentButtonEntity = adminMessageContentButtonEntity;
	}

	public String getSenderAge() {
		return SenderAge;
	}

	public void setSenderAge(String senderAge) {
		SenderAge = senderAge;
	}

	public String getSenderSexID() {
		return SenderSexID;
	}

	public void setSenderSexID(String senderSexID) {
		SenderSexID = senderSexID;
	}

	public boolean getSenderSexIsFemale() {
		return SenderSexIsFemale;
	}

	public void setSenderSexIsFemale(boolean senderSexIsFemale) {
		SenderSexIsFemale = senderSexIsFemale;
	}

	public String getSenderPhoto() {
		return SenderPhoto;
	}

	public void setSenderPhoto(String senderPhoto) {
		SenderPhoto = senderPhoto;
	}

	public String getSenderLocationInfo() {
		return SenderLocationInfo;
	}

	public void setSenderLocationInfo(String senderLocationInfo) {
		SenderLocationInfo = senderLocationInfo;
	}

	public String getSenderName() {
		return SenderName;
	}

	public void setSenderName(String senderName) {
		SenderName = senderName;
	}

	public String getSendDateStr() {
		return SendDateStr;
	}

	public void setSendDateStr(String sendDateStr) {
		SendDateStr = sendDateStr;
	}

	public String getMainContent() {
		return MainContent;
	}

	public void setMainContent(String mainContent) {
		MainContent = mainContent;
	}

	public String getAssistContent() {
		return AssistContent;
	}

	public void setAssistContent(String assistContent) {
		AssistContent = assistContent;
	}

	public String getRelateItemID() {
		return RelateItemID;
	}

	public void setRelateItemID(String relateItemID) {
		RelateItemID = relateItemID;
	}

	public int getSenderID() {
		return SenderID;
	}

	public void setSenderID(int senderID) {
		SenderID = senderID;
	}

	public String getSenderDateStr() {
		return senderDateStr;
	}

	public void setSenderDateStr(String senderDateStr) {
		this.senderDateStr = senderDateStr;
	}

	/**
	 * 发送者的头像图片
	 */
	private String SenderPhoto;
	/**
	 * 
	 */
	private String SenderLocationInfo;
	/**
	 * 发送者姓名
	 */
	private String SenderName;
	/**
	 * 发送日期
	 */
	private String SendDateStr;
	/**
	 * 主要内容
	 */
	private String MainContent;
	/**
	 * 辅助内容
	 */
	private String AssistContent;
	/**
	 * 相关Item信息。具体含义有类型而定。如果 SenderID =1，则一般来说，代表需要特殊显示效果。
	 */
	private String RelateItemID;
	/**
	 * 发送者ID
	 */
	private int SenderID;

}

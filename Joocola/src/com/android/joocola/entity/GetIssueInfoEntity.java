package com.android.joocola.entity;

import java.io.Serializable;



/**
 * 获取的邀约信息的实体类
 * 
 * @author bb
 * 
 */
public class GetIssueInfoEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int PID; // 邀约PID
	private int TypeID; // 邀约类型PID
	private String TypeName; // 及显示名称
	private String Title; // 邀约标题
	private int SexID;// 邀约对象性别PID
	private String SexName; // 邀约对象 性别显示名称
	private int CostID;// 邀约话费类型
	private String CostName; // 邀约话费类型显示名称
	private String ReserveDate;// 邀约时间
	private String LocationName;// 地点名称
	private double LocationX;// 地点X坐标
	private double LocationY;// 地点Y坐标
	private String Description; // 邀约的说明
	private int ApplyUserCount;// 已报名人数
	private int ReplyCount;// 回复数量
	private String State;// 状态名称
	private String PublisherName;// 发布者名称
	private String PublisherPhoto;// 发布者照片，需根据页面显示要求获取不同像素尺寸的图片。
	private String PublisherBirthday;// 发布者生日
	private int PublisherAge;// 发布者年龄
	private String PublisherAstro;// 发布者星座
	private String PublishDate;// 发布时间
	private int PublisherID;// 发布者ID
	private int PublisherSexID;// 发布者性别id

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public int getTypeID() {
		return TypeID;
	}

	public void setTypeID(int typeID) {
		TypeID = typeID;
	}

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}


	public int getSexID() {
		return SexID;
	}

	public void setSexID(int sexID) {
		SexID = sexID;
	}

	public String getSexName() {
		return SexName;
	}

	public void setSexName(String sexName) {
		SexName = sexName;
	}

	public int getCostID() {
		return CostID;
	}

	public void setCostID(int costID) {
		CostID = costID;
	}

	public String getCostName() {
		return CostName;
	}

	public void setCostName(String costName) {
		CostName = costName;
	}

	public String getReserveDate() {
		return ReserveDate;
	}

	public void setReserveDate(String reserveDate) {
		ReserveDate = reserveDate;
	}

	public String getLocationName() {
		return LocationName;
	}

	public void setLocationName(String locationName) {
		LocationName = locationName;
	}

	public double getLocationX() {
		return LocationX;
	}

	public void setLocationX(double locationX) {
		LocationX = locationX;
	}

	public double getLocationY() {
		return LocationY;
	}

	public void setLocationY(double locationY) {
		LocationY = locationY;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getApplyUserCount() {
		return ApplyUserCount;
	}

	public void setApplyUserCount(int applyUserCount) {
		ApplyUserCount = applyUserCount;
	}

	public int getReplyCount() {
		return ReplyCount;
	}

	public void setReplyCount(int replyCount) {
		ReplyCount = replyCount;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getPublisherName() {
		return PublisherName;
	}

	public void setPublisherName(String publisherName) {
		PublisherName = publisherName;
	}

	public String getPublisherPhoto() {
		return PublisherPhoto;
	}

	public void setPublisherPhoto(String publisherPhoto) {
		PublisherPhoto = publisherPhoto;
	}

	public String getPublisherBirthday() {
		return PublisherBirthday;
	}

	public void setPublisherBirthday(String publisherBirthday) {
		PublisherBirthday = publisherBirthday;
	}

	public int getPublisherAge() {
		return PublisherAge;
	}

	public void setPublisherAge(int publisherAge) {
		PublisherAge = publisherAge;
	}

	public String getPublisherAstro() {
		return PublisherAstro;
	}

	public void setPublisherAstro(String publisherAstro) {
		PublisherAstro = publisherAstro;
	}

	public String getPublishDate() {
		return PublishDate;
	}

	public void setPublishDate(String publishDate) {
		PublishDate = publishDate;
	}

	@Override
	public String toString() {
		return "GetIssueInfoEntity [PID=" + PID + ", TypeID=" + TypeID
				+ ", TypeName=" + TypeName + ", Title=" + Title + ", SexID="
				+ SexID + ", SexName=" + SexName + ", CostID=" + CostID
				+ ", CostName="
				+ CostName + ", ReserveDate=" + ReserveDate + ", LocationName="
				+ LocationName + ", LocationX=" + LocationX + ", LocationY="
				+ LocationY + ", Description=" + Description
				+ ", ApplyUserCount=" + ApplyUserCount + ", ReplyCount="
				+ ReplyCount + ", State=" + State + ", PublisherName="
				+ PublisherName + ", PublisherPhoto=" + PublisherPhoto
				+ ", PublisherBirthday=" + PublisherBirthday
				+ ", PublisherAge=" + PublisherAge + ", PublisherAstro="
				+ PublisherAstro + ", PublishDate=" + PublishDate + "]";
	}

	public int getPublisherID() {
		return PublisherID;
	}

	public void setPublisherID(int publisherID) {
		PublisherID = publisherID;
	}

	public int getPublisherSexID() {
		return PublisherSexID;
	}

	public void setPublisherSexID(int publisherSexID) {
		PublisherSexID = publisherSexID;
	}

}

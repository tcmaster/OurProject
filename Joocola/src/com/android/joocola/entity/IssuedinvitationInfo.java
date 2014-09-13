package com.android.joocola.entity;

import java.io.Serializable;

public class IssuedinvitationInfo implements Serializable {
	/*
	 * 默认的版本ID
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 邀约类型ID
	 */
	private int issueId;
	/*
	 * 邀约标题
	 */
	private String title;
	/*
	 * 邀约愿花费信用
	 */
	private int costCredit;
	/*
	 * 性别PID，如果选择无限，则为0
	 */
	private int sexId;
	/*
	 * 邀约花销类型PID
	 */
	private int costId;
	/*
	 * 邀约日期 格式为“2014-09-12 20:22:33”
	 */
	private String reserveDate;
	/*
	 * 邀约地点
	 */
	private String locationName;
	/*
	 * 邀约地点坐标X
	 */
	private double locationX;
	/*
	 * 邀约地点坐标Y
	 */
	private double locationY;
	/*
	 * 邀约说明
	 */
	private String locationDescription;
	/*
	 * 当前发布邀约用户PID
	 */
	private int issueUserID;

	public int getIssueId() {
		return issueId;
	}

	public void setIssueId(int issueId) {
		this.issueId = issueId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCostCredit() {
		return costCredit;
	}

	public void setCostCredit(int costCredit) {
		this.costCredit = costCredit;
	}

	public int getSexId() {
		return sexId;
	}

	public void setSexId(int sexId) {
		this.sexId = sexId;
	}

	public int getCostId() {
		return costId;
	}

	public void setCostId(int costId) {
		this.costId = costId;
	}

	public String getReserveDate() {
		return reserveDate;
	}

	public void setReserveDate(String reserveDate) {
		this.reserveDate = reserveDate;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public double getLocationX() {
		return locationX;
	}

	public void setLocationX(double locationX) {
		this.locationX = locationX;
	}

	public double getLocationY() {
		return locationY;
	}

	public void setLocationY(double locationY) {
		this.locationY = locationY;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public int getIssueUserID() {
		return issueUserID;
	}

	public void setIssueUserID(int issueUserID) {
		this.issueUserID = issueUserID;
	}

	@Override
	public String toString() {
		return "IssuedinvitationInfo [issueId=" + issueId + ", title=" + title
				+ ", costCredit=" + costCredit + ", sexId=" + sexId
				+ ", costId=" + costId + ", reserveDate=" + reserveDate
				+ ", locationName=" + locationName + ", locationX=" + locationX
				+ ", locationY=" + locationY + ", locationDescription="
				+ locationDescription + ", issueUserID=" + issueUserID + "]";
	}

}

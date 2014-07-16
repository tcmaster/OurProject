package com.android.joocola.entity;


/*
 * 发布邀约中 不同类别的实体类。
 */
public class IssueInfo {
	private int PID;
	private String TypeName;
	private String PhotoUrl;
	private int SortNo;

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	public String getPhotoUrl() {
		return PhotoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		PhotoUrl = photoUrl;
	}

	public int getSortNo() {
		return SortNo;
	}

	public void setSortNo(int sortNo) {
		SortNo = sortNo;
	}

}

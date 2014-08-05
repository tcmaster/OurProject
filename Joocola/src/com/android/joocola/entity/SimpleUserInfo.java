package com.android.joocola.entity;

public class SimpleUserInfo {
	private int pid;
	private String UserName;
	private String photoUrl;
	private String Signature;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getSignature() {
		return Signature;
	}

	public void setSignature(String signature) {
		Signature = signature;
	}

}

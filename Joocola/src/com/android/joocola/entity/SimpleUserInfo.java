package com.android.joocola.entity;

public class SimpleUserInfo {
	private int pid;
	private String UserName;
	private String photoUrl;
	private String Signature; // 签名
	private int sexID;
	private int age;
	private String astro;// 星座

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAstro() {
		return astro;
	}

	public void setAstro(String astro) {
		this.astro = astro;
	}

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

	public int getSexID() {
		return sexID;
	}

	public void setSexID(int sexID) {
		this.sexID = sexID;
	}

}

package com.android.joocola.entity;

import java.io.Serializable;

public class RegisterInfo implements Serializable {

	/**
	 * 默认的版本ID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 验证码
	 */
	private String autoCode;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 介绍人
	 */
	private String introducer;
	/**
	 * 用户头像地址
	 */
	private String photoUrl;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 性别
	 */
	private String sex;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAutoCode() {
		return autoCode;
	}

	public void setAutoCode(String autoCode) {
		this.autoCode = autoCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIntroducer() {
		return introducer;
	}

	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "RegisterInfo [userName=" + userName + ", autoCode=" + autoCode
				+ ", password=" + password + ", introducer=" + introducer
				+ ", photoUrl=" + photoUrl + ", birthday=" + birthday
				+ ", sex=" + sex + "]";
	}

}

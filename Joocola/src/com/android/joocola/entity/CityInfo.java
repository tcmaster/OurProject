package com.android.joocola.entity;

/**
 * 省份下的城市名称
 * 
 * @author lixiaosong
 */
public class CityInfo {
	/**
	 * 该城市PID
	 */
	private String PID;
	/**
	 * 该城市所属省份PID
	 */
	private String ParentID;
	/**
	 * 该城市所属省份名称
	 */
	private String Parentname;
	/**
	 * 该城市名称
	 */
	private String CityName;

	public String getPID() {
		return PID;
	}

	public String getParentID() {
		return ParentID;
	}

	public String getParentname() {
		return Parentname;
	}

	public String getCityName() {
		return CityName;
	}

	public void setPID(String pID) {
		PID = pID;
	}

	public void setParentID(String parentID) {
		ParentID = parentID;
	}

	public void setParentname(String parentname) {
		Parentname = parentname;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	@Override
	public String toString() {
		return "CityInfo [PID=" + PID + ", ParentID=" + ParentID
				+ ", Parentname=" + Parentname + ", CityName=" + CityName + "]";
	}

}

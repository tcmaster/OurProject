package com.android.joocola.entity;

/**
 * 
 * @author lixiaosong 某个省份的信息
 */
public class BaseCityInfo {
	/**
	 * 省份PID
	 */
	public String PID;
	/**
	 * 省份名称
	 */
	public String CityName;

	public String getPID() {
		return PID;
	}

	public String getCityName() {
		return CityName;
	}

	public void setPID(String pID) {
		PID = pID;
	}

	public void setCityName(String cityName) {
		CityName = cityName;
	}

	@Override
	public String toString() {
		return "BaseCityInfo [PID=" + PID + ", CityName=" + CityName + "]";
	}

}

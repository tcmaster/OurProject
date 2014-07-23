package com.android.joocola.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.joocola.entity.UserInfo;

public class JsonUtils {
	/**
	 * 解析用户信息JSON对象
	 * 
	 * @param object
	 *            需要解析的UserInfo对象
	 * @param userInfo
	 *            用户信息实体
	 * @return 解析后的用户信息实体
	 * @throws JSONException
	 *             JSON异常
	 */
	public static UserInfo getUserInfo(JSONObject object, UserInfo userInfo)
			throws JSONException {
		if (userInfo != null) {
			userInfo.setDrinkName(object.getString("DrinkName"));
			userInfo.setPhone(object.getString("Phone"));
			userInfo.setMarryID(object.getInt("MarryID") + "");
			userInfo.setPID(object.getInt("PID") + "");
			userInfo.setNickName(object.getString("NickName"));
			// userInfo.setCredit(object.getInt("Credit") + "");
			userInfo.setSexName(object.getString("SexName"));
			userInfo.setUserName(object.getString("UserName"));
			userInfo.setHeightID(object.getInt("HeightID") + "");
			userInfo.setProfessionID(object.getInt("ProfessionID") + "");
			userInfo.setSignature(object.getString("Signature"));
			userInfo.setSmokeID(object.getInt("SmokeID") + "");
			userInfo.setBirthday(object.getString("Birthday"));
			userInfo.setMicroQQ(object.getString("MicroQQ"));
			userInfo.setRevenueName(object.getString("RevenueName"));
			userInfo.setQQ(object.getString("QQ"));
			userInfo.setPhotoUrl(object.getString("PhotoUrl"));
			userInfo.setHobbyIDs(object.getString("HobbyIDs"));
			userInfo.setOldCityName(object.getString("OldCityName"));
			userInfo.setDescription(object.getString("Description"));
			userInfo.setHobbyNames(object.getString("HobbyNames"));
			userInfo.setNewCityID(object.getInt("NewCityID") + "");
			userInfo.setOldCityID(object.getInt("OldCityID") + "");
			userInfo.setMicroBlog(object.getString("MicroBlog"));
			userInfo.setHeightName(object.getString("HeightName"));
			userInfo.setRevenueID(object.getInt("RevenueID") + "");
			userInfo.setDrinkID(object.getInt("DrinkID") + "");
			userInfo.setMarryName(object.getString("MarryName"));
			userInfo.setProfessionName(object.getString("ProfessionName"));
			userInfo.setEmail(object.getString("Email"));
			userInfo.setNewCityName(object.getString("NewCityName"));
			userInfo.setSmokeName(object.getString("SmokeName"));
			userInfo.setSexID(object.getInt("SexID") + "");
			userInfo.setAlbumPhotoUrls(object.getString("AlbumPhotoUrls"));
			userInfo.setAge(object.getInt("Age") + "");
			userInfo.setAstro(object.getString("Astro"));
		}
		return userInfo;
	}
}

package com.android.joocola.entity;

import java.io.Serializable;

/**
 * 用户信息实体类
 * 
 * @author lixiaosong
 * 
 */
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 用户PID
	 */
	private String PID;
	/**
	 * 用户名
	 */
	private String UserName;
	/**
	 * 用户昵称
	 */
	private String NickName;
	/**
	 * 用户头像图片地址
	 */
	private String PhotoUrl;
	/**
	 * 用户email
	 */
	private String Email;
	/**
	 * 用户信用值
	 */
	private String Credit;
	/**
	 * 用户性别值PID
	 */
	private String SexID;
	/**
	 * 用户性别名称
	 */
	private String SexName;
	/**
	 * 用户生日
	 */
	private String Birthday;
	/**
	 * 用户原籍城市PID
	 */
	private String OldCityID;
	/**
	 * 用户原籍城市名称
	 */
	private String OldCityName;
	/**
	 * 用户现住城市PID
	 */
	private String NewCityID;
	/**
	 * 用户现住城市名称
	 */
	private String NewCityName;
	/**
	 * 用户职业PID
	 */
	private String ProfessionID;
	/**
	 * 用户职业名称
	 */
	private String ProfessionName;
	/**
	 * 用户婚姻PID
	 */
	private String MarryID;
	/**
	 * 用户婚姻名称
	 */
	private String MarryName;
	/**
	 * 用户抽烟PID
	 */
	private String SmokeID;
	/**
	 * 用户抽烟名称
	 */
	private String SmokeName;
	/**
	 * 用户喝酒PID
	 */
	private String DrinkID;
	/**
	 * 用户喝酒名称
	 */
	private String DrinkName;
	/**
	 * 用户年收入PID
	 */
	private String RevenueID;
	/**
	 * 用户年收入名称
	 */
	private String RevenueName;
	/**
	 * 用户身高PID
	 */
	private String HeightID;
	/**
	 * 用户身高名称
	 */
	private String HeightName;
	/**
	 * 用户QQ
	 */
	private String QQ;
	/**
	 * 用户微信
	 */
	private String MicroQQ;
	/**
	 * 用户微博
	 */
	private String MicroBlog;
	/**
	 * 用户手机
	 */
	private String Phone;
	/**
	 * 用户签名
	 */
	private String Signature;
	/**
	 * 用户个人描述（暂时似乎没用）
	 */
	private String Description;
	/**
	 * 用户爱好PID字符串，以”,”分割
	 */
	private String HobbyIDs;
	/**
	 * 用户爱好名称，以“，”分割
	 */
	private String HobbyNames;
	/**
	 * 用户相册图片，以“，”分割
	 */
	private String AlbumPhotoUrls;
	/**
	 * 用户年龄
	 */
	private String Age;
	/**
	 * 用户星座
	 */
	private String Astro;
	/**
	 * 
	 * 用户当前查询的某邀约的PID
	 */
	private String AppointID;
	/**
	 * 用户在当前查询的邀约中的状态
	 */
	private String AppointStateID;
	/**
	 * 用户发起的邀约数量
	 */
	private String StaAppMyCount;
	/**
	 * 用户参与的邀约数量
	 */
	private String StaAppJoinCount;
	/**
	 * 用户回复的邀约数量
	 */
	private String StaAppReplyCount;
	/**
	 * 用户收藏的邀约数量
	 */
	private String StaAppFavoriteCount;
	/**
	 * 等待用户评价的邀约数量
	 */
	private String StaAppWaitCommentCount;
	/**
	 * 用户评价过的邀约数量
	 */
	private String StaAppCommentCount;

	/**
	 * 该用户与查询用户的距离信息
	 * 
	 * @return
	 */
	private String LocDistince;
	/**
	 * 该用户最后位置时间信息
	 */
	private String LocDate;

	/**
	 * 用户与当前查询用户的相互评价状态ID
	 */
	private int AppointScoreStateID;
	/**
	 * 用户获得评价数量
	 */
	private int StaAppScoredCount;
	/**
	 * 用户获得的好评数量
	 */
	private int StaAppScoredGoodCount;
	/**
	 * 用户获得的中评数量
	 */
	private int StaAppScoredNormalCount;
	/**
	 * 用户获得的差评数量
	 */
	private int StaAppScoredBadCount;

	public String getPID() {
		return PID;
	}

	public void setPID(String pID) {
		PID = pID;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public String getPhotoUrl() {
		return PhotoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		PhotoUrl = photoUrl;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getCredit() {
		return Credit;
	}

	public void setCredit(String credit) {
		Credit = credit;
	}

	public String getSexID() {
		return SexID;
	}

	public void setSexID(String sexID) {
		SexID = sexID;
	}

	public String getSexName() {
		return SexName;
	}

	public void setSexName(String sexName) {
		SexName = sexName;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getOldCityID() {
		return OldCityID;
	}

	public void setOldCityID(String oldCityID) {
		OldCityID = oldCityID;
	}

	public String getOldCityName() {
		return OldCityName;
	}

	public void setOldCityName(String oldCityName) {
		OldCityName = oldCityName;
	}

	public String getNewCityID() {
		return NewCityID;
	}

	public void setNewCityID(String newCityID) {
		NewCityID = newCityID;
	}

	public String getNewCityName() {
		return NewCityName;
	}

	public void setNewCityName(String newCityName) {
		NewCityName = newCityName;
	}

	public String getProfessionID() {
		return ProfessionID;
	}

	public void setProfessionID(String professionID) {
		ProfessionID = professionID;
	}

	public String getProfessionName() {
		return ProfessionName;
	}

	public void setProfessionName(String professionName) {
		ProfessionName = professionName;
	}

	public String getMarryID() {
		return MarryID;
	}

	public void setMarryID(String marryID) {
		MarryID = marryID;
	}

	public String getMarryName() {
		return MarryName;
	}

	public void setMarryName(String marryName) {
		MarryName = marryName;
	}

	public String getSmokeID() {
		return SmokeID;
	}

	public void setSmokeID(String smokeID) {
		SmokeID = smokeID;
	}

	public String getSmokeName() {
		return SmokeName;
	}

	public void setSmokeName(String smokeName) {
		SmokeName = smokeName;
	}

	public String getDrinkID() {
		return DrinkID;
	}

	public void setDrinkID(String drinkID) {
		DrinkID = drinkID;
	}

	public String getDrinkName() {
		return DrinkName;
	}

	public void setDrinkName(String drinkName) {
		DrinkName = drinkName;
	}

	public String getRevenueID() {
		return RevenueID;
	}

	public void setRevenueID(String revenueID) {
		RevenueID = revenueID;
	}

	public String getRevenueName() {
		return RevenueName;
	}

	public void setRevenueName(String revenueName) {
		RevenueName = revenueName;
	}

	public String getHeightID() {
		return HeightID;
	}

	public void setHeightID(String heightID) {
		HeightID = heightID;
	}

	public String getHeightName() {
		return HeightName;
	}

	public void setHeightName(String heightName) {
		HeightName = heightName;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getMicroQQ() {
		return MicroQQ;
	}

	public void setMicroQQ(String microQQ) {
		MicroQQ = microQQ;
	}

	public String getMicroBlog() {
		return MicroBlog;
	}

	public void setMicroBlog(String microBlog) {
		MicroBlog = microBlog;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getSignature() {
		return Signature;
	}

	public void setSignature(String signature) {
		Signature = signature;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getHobbyIDs() {
		return HobbyIDs;
	}

	public void setHobbyIDs(String hobbyIDs) {
		HobbyIDs = hobbyIDs;
	}

	public String getHobbyNames() {
		return HobbyNames;
	}

	public void setHobbyNames(String hobbyNames) {
		HobbyNames = hobbyNames;
	}

	public String getAlbumPhotoUrls() {
		return AlbumPhotoUrls;
	}

	public void setAlbumPhotoUrls(String albumPhotoUrls) {
		AlbumPhotoUrls = albumPhotoUrls;
	}

	public String getAge() {
		return Age;
	}

	public void setAge(String age) {
		Age = age;
	}

	public String getAstro() {
		return Astro;
	}

	public void setAstro(String astro) {
		Astro = astro;
	}

	public String getAppointID() {
		return AppointID;
	}

	public void setAppointID(String appointID) {
		AppointID = appointID;
	}

	public String getAppointStateID() {
		return AppointStateID;
	}

	public void setAppointStateID(String appointStateID) {
		AppointStateID = appointStateID;
	}

	public String getStaAppMyCount() {
		return StaAppMyCount;
	}

	public void setStaAppMyCount(String staAppMyCount) {
		StaAppMyCount = staAppMyCount;
	}

	public String getStaAppJoinCount() {
		return StaAppJoinCount;
	}

	public void setStaAppJoinCount(String staAppJoinCount) {
		StaAppJoinCount = staAppJoinCount;
	}

	public String getStaAppReplyCount() {
		return StaAppReplyCount;
	}

	public void setStaAppReplyCount(String staAppReplyCount) {
		StaAppReplyCount = staAppReplyCount;
	}

	public String getStaAppFavoriteCount() {
		return StaAppFavoriteCount;
	}

	public void setStaAppFavoriteCount(String staAppFavoriteCount) {
		StaAppFavoriteCount = staAppFavoriteCount;
	}

	public String getStaAppWaitCommentCount() {
		return StaAppWaitCommentCount;
	}

	public void setStaAppWaitCommentCount(String staAppWaitCommentCount) {
		StaAppWaitCommentCount = staAppWaitCommentCount;
	}

	public String getStaAppCommentCount() {
		return StaAppCommentCount;
	}

	public void setStaAppCommentCount(String staAppCommentCount) {
		StaAppCommentCount = staAppCommentCount;
	}

	@Override
	public String toString() {
		return "UserInfo [PID=" + PID + ", UserName=" + UserName
				+ ", NickName=" + NickName + ", PhotoUrl=" + PhotoUrl
				+ ", Email=" + Email + ", Credit=" + Credit + ", SexID="
				+ SexID + ", SexName=" + SexName + ", Birthday=" + Birthday
				+ ", OldCityID=" + OldCityID + ", OldCityName=" + OldCityName
				+ ", NewCityID=" + NewCityID + ", NewCityName=" + NewCityName
				+ ", ProfessionID=" + ProfessionID + ", ProfessionName="
				+ ProfessionName + ", MarryID=" + MarryID + ", MarryName="
				+ MarryName + ", SmokeID=" + SmokeID + ", SmokeName="
				+ SmokeName + ", DrinkID=" + DrinkID + ", DrinkName="
				+ DrinkName + ", RevenueID=" + RevenueID + ", RevenueName="
				+ RevenueName + ", HeightID=" + HeightID + ", HeightName="
				+ HeightName + ", QQ=" + QQ + ", MicroQQ=" + MicroQQ
				+ ", MicroBlog=" + MicroBlog + ", Phone=" + Phone
				+ ", Signature=" + Signature + ", Description=" + Description
				+ ", HobbyIDs=" + HobbyIDs + ", HobbyNames=" + HobbyNames
				+ ", AlbumPhotoUrls=" + AlbumPhotoUrls + ", Age=" + Age
				+ ", Astro=" + Astro + ", AppointID=" + AppointID
				+ ", AppointStateID=" + AppointStateID + ", StaAppMyCount="
				+ StaAppMyCount + ", StaAppJoinCount=" + StaAppJoinCount
				+ ", StaAppReplyCount=" + StaAppReplyCount
				+ ", StaAppFavoriteCount=" + StaAppFavoriteCount
				+ ", StaAppWaitCommentCount=" + StaAppWaitCommentCount
				+ ", StaAppCommentCount=" + StaAppCommentCount + "]";
	}

	public String getLocDistince() {
		return LocDistince;
	}

	public void setLocDistince(String locDistince) {
		LocDistince = locDistince;
	}

	public String getLocDate() {
		return LocDate;
	}

	public void setLocDate(String locDate) {
		LocDate = locDate;
	}

	public int getAppointScoreStateID() {
		return AppointScoreStateID;
	}

	public void setAppointScoreStateID(int appointScoreStateID) {
		AppointScoreStateID = appointScoreStateID;
	}

	public int getStaAppScoredCount() {
		return StaAppScoredCount;
	}

	public void setStaAppScoredCount(int staAppScoredCount) {
		StaAppScoredCount = staAppScoredCount;
	}

	public int getStaAppScoredGoodCount() {
		return StaAppScoredGoodCount;
	}

	public void setStaAppScoredGoodCount(int staAppScoredGoodCount) {
		StaAppScoredGoodCount = staAppScoredGoodCount;
	}

	public int getStaAppScoredNormalCount() {
		return StaAppScoredNormalCount;
	}

	public void setStaAppScoredNormalCount(int staAppScoredNormalCount) {
		StaAppScoredNormalCount = staAppScoredNormalCount;
	}

	public int getStaAppScoredBadCount() {
		return StaAppScoredBadCount;
	}

	public void setStaAppScoredBadCount(int staAppScoredBadCount) {
		StaAppScoredBadCount = staAppScoredBadCount;
	}

}

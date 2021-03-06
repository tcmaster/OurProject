package com.android.joocola.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.joocola.entity.AdminMessage;
import com.android.joocola.entity.AdminMessageContentButtonEntity;
import com.android.joocola.entity.AdminMessageContentEntity;
import com.android.joocola.entity.AdminMessageNotify;
import com.android.joocola.entity.AppointScoreEntity;
import com.android.joocola.entity.GetIssueInfoEntity;
import com.android.joocola.entity.NAdminMsgEntity;
import com.android.joocola.entity.ReplyEntity;
import com.android.joocola.entity.UserInfo;

/**
 * 用于处理应用中用到json的地方
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-10-11
 */
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
	public static UserInfo getUserInfo(JSONObject object, UserInfo userInfo) throws JSONException {
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
			userInfo.setAppointID(object.getInt("AppointID") + "");
			userInfo.setAppointStateID(object.getInt("AppointStateID") + "");
			userInfo.setStaAppMyCount(object.getInt("StaAppMyCount") + "");
			userInfo.setStaAppJoinCount(object.getInt("StaAppJoinCount") + "");
			userInfo.setStaAppReplyCount(object.getInt("StaAppReplyCount") + "");
			userInfo.setStaAppFavoriteCount(object.getInt("StaAppFavoriteCount") + "");
			userInfo.setStaAppWaitCommentCount(object.getInt("StaAppWaitCommentCount") + "");
			userInfo.setStaAppCommentCount(object.getInt("StaAppCommentCount") + "");
			userInfo.setAppointScoreStateID(object.getInt("AppointScoreStateID"));
			userInfo.setLocDistince(object.getString("LocDistince"));
			userInfo.setLocDate(object.getString("LocDate"));
			userInfo.setStaAppScoredCount(object.getInt("StaAppScoredCount"));
			userInfo.setStaAppScoredGoodCount(object.getInt("StaAppScoredGoodCount"));
			userInfo.setStaAppScoredNormalCount(object.getInt("StaAppScoredNormalCount"));
			userInfo.setStaAppScoredBadCount(object.getInt("StaAppScoredBadCount"));
		}
		return userInfo;
	}

	/**
	 * 解析发布信息的JSON
	 * 
	 * @param object
	 * @param getIssueInfoEntity
	 * @return
	 * @throws JSONException
	 */
	public static GetIssueInfoEntity getIssueInfoEntity(JSONObject object, GetIssueInfoEntity getIssueInfoEntity) throws JSONException {
		if (getIssueInfoEntity != null) {
			getIssueInfoEntity.setTitle(object.getString("Title"));
			getIssueInfoEntity.setApplyUserCount(object.getInt("ApplyUserCount"));
			getIssueInfoEntity.setCostName(object.getString("CostName"));
			getIssueInfoEntity.setDescription(object.getString("Description"));
			getIssueInfoEntity.setLocationName(object.getString("LocationName"));
			getIssueInfoEntity.setPID(object.getInt("PID"));
			getIssueInfoEntity.setPublishDate(object.getString("PublishDate"));
			getIssueInfoEntity.setPublisherAge(object.getInt("PublisherAge"));
			getIssueInfoEntity.setPublisherAstro(object.getString("PublisherAstro"));
			getIssueInfoEntity.setPublisherBirthday(object.getString("PublisherBirthday"));
			getIssueInfoEntity.setPublisherName(object.getString("PublisherName"));
			getIssueInfoEntity.setPublisherPhoto(object.getString("PublisherPhoto"));
			getIssueInfoEntity.setReplyCount(object.getInt("ReplyCount"));
			getIssueInfoEntity.setReserveDate(object.getString("ReserveDate"));
			getIssueInfoEntity.setPublisherID(object.getInt("PublisherID"));
			getIssueInfoEntity.setSexName(object.getString("SexName"));
			getIssueInfoEntity.setState(object.getString("State"));
			getIssueInfoEntity.setTitle(object.getString("Title"));
			getIssueInfoEntity.setPublisherSexID(object.getInt("PublisherSexID"));
			getIssueInfoEntity.setRoomID(object.getString("RoomID"));
			getIssueInfoEntity.setBrowseCount(object.getInt("BrowseCount"));
		}
		return getIssueInfoEntity;
	}

	/**
	 * 解析回复json
	 * 
	 * @throws JSONException
	 */
	public static ReplyEntity getReplyEntity(JSONObject jsonObject, ReplyEntity replyEntity) throws JSONException {
		if (replyEntity != null) {
			replyEntity.setContent(jsonObject.getString("Content"));
			replyEntity.setPublishDate(jsonObject.getString("PublishDate"));
			replyEntity.setPublisherID(jsonObject.getInt("PublisherID"));
			replyEntity.setPublisherName(jsonObject.getString("PublisherName"));
			replyEntity.setPublisherPhotoString(jsonObject.getString("PublisherPhoto"));
			replyEntity.setReplypid(jsonObject.getInt("PID"));
		}
		return replyEntity;
	}

	/**
	 * 解析评价json
	 */
	public static AppointScoreEntity getAppointScoreEntity(JSONObject jsonObject) {
		AppointScoreEntity appointScoreEntity = new AppointScoreEntity();
		try {
			appointScoreEntity.setAppointTitle(jsonObject.getString("AppointTitle"));
			appointScoreEntity.setComment(jsonObject.getString("Comment"));
			appointScoreEntity.setCommentDateStr(jsonObject.getString("CommentDateStr"));
			appointScoreEntity.setFromUserIDString(jsonObject.getString("FromUserID"));
			appointScoreEntity.setFromUserName(jsonObject.getString("FromUserName"));
			appointScoreEntity.setFromUserPhoto(jsonObject.getString("FromUserPhoto"));
			appointScoreEntity.setPid(jsonObject.getString("PID"));
			appointScoreEntity.setReserveDateStr(jsonObject.getString("ReserveDateStr"));
			appointScoreEntity.setScoreID(jsonObject.getString("ScoreID"));
			appointScoreEntity.setToUserID(jsonObject.getString("ToUserID"));
			appointScoreEntity.setToUserName(jsonObject.getString("ToUserName"));
			appointScoreEntity.setToUserPhoto(jsonObject.getString("ToUserPhoto"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return appointScoreEntity;
	}

	/**
	 * 解析系统消息发送过来的json
	 */
	public static AdminMessage getAdminMessageEntity(JSONObject object) {
		AdminMessage messageEntity = new AdminMessage();
		try {
			messageEntity.setRelateUserID(object.getInt("RelateUserID") + "");
			messageEntity.setRelateUserName(object.getString("RelateUserName"));
			messageEntity.setRelateUserPhoto(object.getString("RelateUserPhoto"));
			messageEntity.setMsgContent(object.getString("MsgContent"));
			messageEntity.setMsgType(object.getString("MsgType"));
			messageEntity.setRecUserID(object.getInt("RecUserID") + "");
			messageEntity.setSendDate(object.getString("SendDate"));
			messageEntity.setCallUrl(object.getJSONArray("Buttons").getJSONObject(0).getString("CallUrl"));
			messageEntity.setCaption(object.getJSONArray("Buttons").getJSONObject(0).getString("Caption"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return messageEntity;
	}

	/**
	 * 解析私聊消息对象
	 * 
	 * @param object
	 * @return 私聊消息对象的实体
	 * @author: LiXiaosong
	 * @date:2014-10-17
	 */
	public static AdminMessageNotify getAdminMessageNotifyEntity(JSONObject object) {
		AdminMessageNotify entity = new AdminMessageNotify();
		try {
			entity.setMsgType(object.getString("MsgType"));
			entity.setMsgContent(object.getString("MsgContent"));
			entity.setRecUserID(object.getInt("RecUserID") + "");
			entity.setSendDate(object.getString("SendDate"));
			entity.setTalkFromUserID(object.getInt("TalkFromUserID") + "");
			entity.setTalkRevUserID(object.getInt("TalkRevUserID") + "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entity;
	}

	/**
	 * 解析新系统消息实体
	 * 
	 * @param object
	 *            要解析的对象
	 * @return 封装好的实体，可存数据库
	 * @author: LiXiaosong
	 * @date:2014-10-17
	 */
	public static NAdminMsgEntity getNAdminMsgEntity(JSONObject object) {
		NAdminMsgEntity entity = new NAdminMsgEntity();
		try {
			entity.setMsgContent(object.getString("MsgContent"));
			entity.setMsgType(object.getString("MsgType"));
			entity.setRecUserID(object.getInt("RecUserID") + "");
			entity.setSendDate(object.getString("SendDate"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return entity;
	}

	/**
	 * 解析系统消息具体的内容
	 */
	public static AdminMessageContentEntity getAdminMessageContentEntity(JSONObject object) {
		AdminMessageContentEntity mEntity = new AdminMessageContentEntity();
		AdminMessageContentButtonEntity mButtonEntity = new AdminMessageContentButtonEntity();
		try {
			mEntity.setAssistContent(object.getString("AssistContent"));
			mEntity.setMainContent(object.getString("MainContent"));
			mEntity.setMsgTypeId(object.getInt("MsgTypeID"));
			mEntity.setSendDateStr(object.getString("SendDateStr"));
			mEntity.setSenderAge(object.getString("SenderAge"));
			mEntity.setSenderDateStr(object.getString("SenderLocationDate"));
			mEntity.setSenderID(object.getInt("SenderID"));
			mEntity.setSenderPhoto(object.getString("SenderPhoto"));
			mEntity.setSenderLocationInfo(object.getString("SenderLocationInfo"));
			mEntity.setSenderName(object.getString("SenderName"));
			mEntity.setSenderSexIsFemale(object.getBoolean("SenderSexIsFemale"));
			String buttons = object.getString("Buttons");
			buttons = buttons.replaceAll("\\\\\\\u0026", "&");
			buttons = buttons.replace("\\\\", "");
			if (buttons.startsWith("[")) {
				JSONArray jsonArray = new JSONArray(buttons);
				if (jsonArray.length() != 0) {
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					mButtonEntity.setCaption(jsonObject.getString("Caption"));
					mButtonEntity.setCallUrl(jsonObject.getString("CallUrl"));
				}
			} else {
				mButtonEntity.setCaption(buttons);
			}
			mEntity.setAdminMessageContentButtonEntity(mButtonEntity);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mEntity;

	}
}

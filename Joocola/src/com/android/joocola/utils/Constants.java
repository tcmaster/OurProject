package com.android.joocola.utils;

/*
 * 常用的字符串
 * 
 */
public class Constants {

	/**
	 * 版本号
	 */
	public final static String version = "1.0.0.0";
	/**
	 * 聊天广播的Action
	 */
	public static final String CHAT_ACTION = "IWantHaveAChat";
	public static final String MULTI_CHAT_ACTION = "IWantHaveAMultiChat";
	public static final String URL = "http://a.joocola.com";
	public static final String MAIN_URL = "http://a.joocola.com/Controller/";
	public static final String UPLOADIMGURL = "http://a.joocola.com/_Base/Uploader/UserPhoto.ashx";
	/**
	 * 获取用户信息接口
	 */
	public static final String USERINFOURL = "Sys.UserController.GetUserInfos.ashx";
	/**
	 * 获取用户简略信息接口
	 */
	public static final String USERSIMPLE = "Sys.UserController.GetUserSimpleInfos.ashx";
	/*
	 * 用来获取登录信息的shareprefernce.
	 */
	public static final String LOGIN_PREFERENCE = "LOGIN_INFO";
	public static final String LOGIN_PID = "login_pid";
	public static final String LOGIN_ACCOUNT = "login_account";
	public static final String LOGIN_AUTOMATIC = "login_Automatic";

	/*
	 * 基础信息的类别
	 */
	public static final String basedata_Sex = "Sex";
	public static final String basedata_Profession = "Profession";
	public static final String basedata_Marry = "Marry";
	public static final String basedata_Smoke = "Smoke";
	public static final String basedata_Drink = "Drink";
	public static final String basedata_Revenue = "Revenue";
	public static final String basedata_Height = "Height";
	public static final String basedata_Hobby = "Hobby";
	public static final String basedata_AppointCost = "AppointCost";

	/*
	 * 发布邀约时的map的字符串
	 */
	public static final String ISSUE_TYPEID = "TypeID";
	public static final String ISSUE_TITLE = "Title";
	public static final String ISSUE_COSTCREDIT = "CostCredit";
	public static final String ISSUE_SEXID = "SexID";
	public static final String ISSUE_COSTID = "CostID";
	public static final String ISSUE_RESERVEDATE = "ReserveDate";
	public static final String ISSUE_LOCATIONAME = "LocationName";
	public static final String ISSUE_LOCATIONX = "LocationX";
	public static final String ISSUE_LOCATIONY = "LocationY";
	public static final String ISSUE_DESCRIPTION = "Description";
	public static final String ISSUE_PUBLISHERID = "PublisherID";
	public static final String ISSUE_LOCATIONCITYNAME = "LocationCityName";
	/**
	 * 获取省份信息
	 */
	public static final String BASE_CITY_INFO_URL = "Sys.CityController.GetProvinces.ashx";
	/**
	 * 获取省份下的城市信息
	 */
	public static final String CITY_INFO_URL = "Sys.CityController.GetCitys.ashx";
	/**
	 * 喜欢某人接口
	 */
	public static final String LIKE_USER_URL = "Sys.UserController.LikeUser.ashx";
	/**
	 * 取消喜欢某人的接口
	 */
	public static final String UNLIKE_USER_URL = "Sys.UserController.LikeUserCancel.ashx";
	/**
	 * 检查是否喜欢某人
	 */
	public static final String IS_LIKE_USER_URL = "Sys.UserController.IsLikeUser.ashx";
	/**
	 * 获取邀约列表
	 */
	public static final String GET_QUERY_APPOINT = "Bus.AppointController.QueryAppoint.ashx";
	/**
	 * 修改用户信息接口URL
	 */
	/**
	 * 用户爱好
	 */
	public static final String HOBBYURL = "Sys.UserController.SetUserHobbies.ashx";
	/**
	 * 用户相册地址
	 */
	public static final String ALBUMURL = "Sys.UserController.SetUserAlbumPhotoUrls.ashx";
	/**
	 * 用户头像地址
	 */
	public static final String PHOTOURL = "Sys.UserController.SetUserPhoto.ashx";
	/**
	 * 用户昵称
	 */
	public static final String NICKNAMEURL = "Sys.UserController.SetUserNickName.ashx";
	/**
	 * 用户性别
	 */
	public static final String SEXURL = "Sys.UserController.SetUserSex.ashx";
	/**
	 * 用户生日
	 */
	public static final String BIRTHDAYURL = "Sys.UserController.SetUserBirthday.ashx";
	/**
	 * 用户签名
	 */
	public static final String SIGNINURL = "Sys.UserController.SetUserSign.ashx";
	/**
	 * 用户手机
	 */
	public static final String PHONEURL = "Sys.UserController.SetUserPhone.ashx";
	/**
	 * 用户原籍
	 */
	public static final String CITYURL = "Sys.UserController.SetUserOldCity.ashx";
	/**
	 * 用户现居地
	 */
	public static final String NEWCITYURL = "Sys.UserController.SetUserNewCity.ashx";
	/**
	 * 得到城市显示名称
	 */
	public static final String GETCITYNAME = "Sys.CityController.GetCityName.ashx";
	/**
	 * 设置用户职业
	 */
	public static final String PROFESSIONURL = "Sys.UserController.SetUserProfession.ashx";
	/**
	 * 设置用户婚姻状况
	 */
	public static final String MARRYURL = "Sys.UserController.SetUserMarry.ashx";
	/**
	 * 设置用户抽烟状况
	 */
	public static final String SMOKEURL = "Sys.UserController.SetUserSmoke.ashx";
	/**
	 * 用户饮酒状况
	 */
	public static final String DRINKURL = "Sys.UserController.SetUserDrink.ashx";
	/**
	 * 用户年收入状况
	 */
	public static final String USERREVENUEURL = "Sys.UserController.SetUserRevenue.ashx";
	/**
	 * 用户身高状况
	 */
	public static final String HEIGHTURL = "Sys.UserController.SetUserHeight.ashx";
	/**
	 * 用户QQ
	 */
	public static final String QQURL = "Sys.UserController.SetUserQQ.ashx";
	/**
	 * 用户微信
	 */
	public static final String MICROQQURL = "Sys.UserController.SetUserMicroQQ.ashx";
	/**
	 * 用户微博
	 */
	public static final String MICROBLOGURL = "Sys.UserController.SetUserMicroBlog.ashx";
	/**
	 * 用户信用
	 */
	public static final String USERCREDITURL = "Sys.UserController.GetUserCredit.ashx";
	/**
	 * 邀约类型获取地址
	 */
	public static final String ISSUE_TYPE_URL = "Bus.AppointController.GetTypes.ashx";
	/**
	 * 记录私聊信息
	 */
	public static final String CHAT_MARK_URL = "Bus.TalkController.LogPrivateTalk.ashx";
	/**
	 * 检查是否可以私聊
	 */
	public static final String IS_TALK_URL = "Bus.TalkController.CanPrivateTalk.ashx";
	/**
	 * 时间界面的几个传值
	 */
	public static final int INTENT_TIME = 1;
	public static final int BACKTOISSUE_OK = 2;
	public static final int BACKTOISSUE_CANCEL = 3;
	/**
	 * 聊天类型，单聊
	 */
	public static final int CHAT_TYPE_SINGLE = 1;
	/**
	 * 聊天类型，群聊
	 */
	public static final int CHAT_TYPE_MULTI = 2;
}
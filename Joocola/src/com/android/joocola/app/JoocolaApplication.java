package com.android.joocola.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.util.Log;

import com.android.joocola.dbmanger.BaseDataInfoManger;
import com.android.joocola.entity.BaseCityInfo;
import com.android.joocola.entity.BaseDataInfo;
import com.android.joocola.entity.IssueInfo;
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;

public class JoocolaApplication extends Application {
	private static JoocolaApplication instance;
	// 存储基础数据
	private ArrayList<BaseDataInfo> baseInfoList;
	private static final String BASEDATAURL = "Sys.BaseDataController.GetDatas.ashx";

	// 发布邀约的类别实体类
	private ArrayList<IssueInfo> issueInfos = new ArrayList<IssueInfo>();
	// 当前登录用户信息,只有本次登录成功才可使用
	private UserInfo userInfo;
	// 基础城市信息
	private List<BaseCityInfo> baseCityInfos;

	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
		getInfoFromNetwork();
		initBaseCityInfo();
	}

	// 得到基础数据信息
	public void getInfoFromNetwork() {
		final BaseDataInfoManger baseDataInfoManger = BaseDataInfoManger
				.getBaseDataInfoManger(getApplicationContext());

		baseInfoList = new ArrayList<BaseDataInfo>();
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("dataType", "0");
		if (Utils.isNetConn(this)) {
			interface1.getData(BASEDATAURL, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					try {
						Log.v("lixiaosong", result);
						JSONArray array = new JSONArray(result);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);
							BaseDataInfo info = new BaseDataInfo();
							info.setPID(object.getInt("PID"));
							info.setSortNo(object.getInt("SortNo"));
							info.setItemName(object.getString("ItemName"));
							info.setTypeName(object.getString("TypeName"));
							baseInfoList.add(info);
						}
						baseDataInfoManger.clearTable();
						baseDataInfoManger.saveAllChannel(baseInfoList);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			// Utils.toast(this, "当前网络不可用，请检查网络连接");
			baseInfoList = baseDataInfoManger.getAllChannel();
			Log.e("没网的情况下", baseInfoList.toString());
		}
	}

	public static JoocolaApplication getInstance() {
		return instance;
	}

	public List<BaseDataInfo> getBaseInfo() {
		return baseInfoList;
	}

	public List<BaseDataInfo> getBaseInfo(String typeName) {
		List<BaseDataInfo> typeList = new ArrayList<BaseDataInfo>();
		for (int i = 0; i < baseInfoList.size(); i++) {
			if (baseInfoList.get(i).getTypeName().equals(typeName)) {
				typeList.add(baseInfoList.get(i));
			}
		}
		return typeList;
	}

	public BaseDataInfo getSingleBaseInfo(String name) {
		return getSingleBaseInfo(name, "");

	}

	public BaseDataInfo getSingleBaseInfo(String name, String typeName) {
		boolean isNotThisType = typeName.isEmpty();
		for (int i = 0; i < baseInfoList.size(); ++i) {
			BaseDataInfo baseDataInfo = baseInfoList.get(i);
			if (baseDataInfo.getItemName().equals(name)
					&& (isNotThisType || baseDataInfo.getTypeName().equals(
							typeName))) {
				return baseDataInfo;
			}

		}
		return null;
	}

	public ArrayList<IssueInfo> getIssueInfos() {
		return issueInfos;
	}

	public void setIssueInfos(ArrayList<IssueInfo> issueInfos) {
		this.issueInfos = issueInfos;
	}

	/**
	 * 在用户登录成功后，用于获得用户信息
	 */
	public void initUserInfo(String userId) {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("userID", userId);
		interface1.getData(Constans.USERINFOURL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result != null && !result.equals("")) {
					userInfo = new UserInfo();
					try {
						JSONObject object = new JSONObject(result);
						/**
						 * 这里写的太麻烦，准备在优化时把这些改成通用反射方法
						 */
						userInfo.setDrinkName(object.getString("DrinkName"));
						userInfo.setPhone(object.getString("Phone"));
						userInfo.setMarryID(object.getInt("MarryID") + "");
						userInfo.setPID(object.getInt("PID") + "");
						userInfo.setNickName(object.getString("NickName"));
						userInfo.setCredit(object.getInt("Credit") + "");
						userInfo.setSexName(object.getString("SexName"));
						userInfo.setUserName(object.getString("UserName"));
						userInfo.setHeightID(object.getInt("HeightID") + "");
						userInfo.setProfessionID(object.getInt("ProfessionID")
								+ "");
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
						userInfo.setProfessionName(object
								.getString("ProfessionName"));
						userInfo.setEmail(object.getString("Email"));
						userInfo.setNewCityName(object.getString("NewCityName"));
						userInfo.setSmokeName(object.getString("SmokeName"));
						userInfo.setSexID(object.getInt("SexID") + "");
						userInfo.setAlbumPhotoUrls(object
								.getString("AlbumPhotoUrls"));
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else
					Utils.toast(getApplicationContext(), "获取登录用户信息失败");
			}
		});
	}

	/**
	 * 得到登录用户的信息，如果没有进行登录，则会返回空,可用该方法更新用户信息
	 * 
	 * @return 用户信息
	 */
	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void initBaseCityInfo() {
		baseCityInfos = new ArrayList<BaseCityInfo>();
		HttpPostInterface interface1 = new HttpPostInterface();
		if (Utils.isNetConn(this)) {
			interface1.getData(Constans.BASE_CITY_INFO_URL,
					new HttpPostCallBack() {

						@Override
						public void httpPostResolveData(String result) {
							try {
								Log.v("lixiaosong", result);
								JSONArray array = new JSONArray(result);
								for (int i = 0; i < array.length(); i++) {
									JSONObject object = array.getJSONObject(i);
									BaseCityInfo info = new BaseCityInfo();
									info.setCityName(object
											.getString("CityName"));
									info.setPID(object.getInt("PID") + "");
									baseCityInfos.add(info);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			Log.e("没网的情况下", baseInfoList.toString());
		}
	}

	public List<BaseCityInfo> getBaseCityInfo() {
		return baseCityInfos;
	}
}

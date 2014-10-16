package com.android.joocola.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.android.joocola.chat.EaseMobChat;
import com.android.joocola.dbmanger.BaseDataInfoManger;
import com.android.joocola.entity.BaseCityInfo;
import com.android.joocola.entity.BaseDataInfo;
import com.android.joocola.entity.IssueInfo;
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.DbUtils;
//
//		_oo0oo_
//	   o8888888o
//     88" . "88
//     (| -_- |)
//     0\  =  /0
//   ___/`---'\___
// .' \\|     |// '.
/// \\|||  :  |||// \
/// _||||| -:- |||||- \
//|   | \\\  -  /// |   |
//| \_|  ''\---/''  |_/ |
//\  .-\__  '-'  ___/-. /
//___'. .'  /--.--\  `. .'___
//."" '<  `.___\_<|>_/___.' >' "".
//| | :  `- \`.;`\ _ /`;.`/ - ` : | |
//\  \ `_.   \_ __\ /__ _/   .-` /  /
//=====`-.____`.___ \_____/___.-`___.-'=====
//`=---='
//
//
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//
//佛祖保佑         永无BUG

public class JoocolaApplication extends Application {

	private static JoocolaApplication instance;
	// 存储基础数据
	private ArrayList<BaseDataInfo> baseInfoList;
	private static final String BASEDATAURL = "Sys.BaseDataController.GetDatas.ashx";
	private static final String CITYURL = "Bus.AppointController.QueryAppointCities.ashx";
	// 发布邀约的类别实体类
	private ArrayList<IssueInfo> issueInfos = new ArrayList<IssueInfo>();
	// 当前登录用户信息,只有本次登录成功才可使用
	private UserInfo userInfo;
	// 基础城市信息
	private List<BaseCityInfo> baseCityInfos;
	private Handler handler;
	/**
	 * 数据库帮助类
	 */
	private DbUtils db;
	/**
	 * 本次登录用户的PID
	 */
	private String PID;
	/**
	 * 公共缓存
	 */
	private BitmapCache cache;

	/**
	 * 
	 */
	private ArrayList<String> mCitys = new ArrayList<String>();

	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
		handler = new Handler();
		getInfoFromNetwork();
		initBaseCityInfo();
		// if (Build.VERSION.SDK_INT >= 9) {
		//
		// // 加载CrashHandler
		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(getApplicationContext());
		// }
		cache = new BitmapCache();
		EaseMobChat.getInstance().init(this);

		// startService(new Intent(this, DefineService.class));

	}

	public BitmapCache getBitmapCache() {
		if (cache == null)
			cache = new BitmapCache();
		return cache;
	}

	/**
	 * 获取邀约城市列表
	 * 
	 * @see:
	 * @since:
	 * @author: bb
	 * @date:2014年9月24日
	 */
	public void getIssueCitys() {
		Log.e("bb", "getIssueCitys");
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		if (Utils.isNetConn(this)) {
			httpPostInterface.getData(CITYURL, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					Log.e("bb", "------------------->" + result);
				}
			});
		}
	}

	// 得到基础数据信息
	public void getInfoFromNetwork() {
		final BaseDataInfoManger baseDataInfoManger = BaseDataInfoManger.getBaseDataInfoManger(getApplicationContext());

		baseInfoList = new ArrayList<BaseDataInfo>();
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("dataType", "0");
		if (Utils.isNetConn(this)) {
			interface1.getData(BASEDATAURL, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					try {
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
			if (baseDataInfo.getItemName().equals(name) && (isNotThisType || baseDataInfo.getTypeName().equals(typeName))) {
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
	 * 首次登录，获取用户信息
	 */
	public void initUserInfoAfterLogin(String userId) {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("UserIDs", userId);
		interface1.getData(Constants.USERINFOURL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result != null && !result.equals("")) {
					userInfo = new UserInfo();
					try {
						JSONObject object = new JSONObject(result);
						JSONArray array = object.getJSONArray("Entities");
						JSONObject userObject = array.getJSONObject(0);
						JsonUtils.getUserInfo(userObject, userInfo);
						PID = userInfo.getPID();
						EaseMobChat.getInstance().beginWork();
						EaseMobChat.getInstance().login("u" + PID, "123456");
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(getApplicationContext(), "获取登录用户信息失败");
						}
					});

				}
			}
		});
	}

	/**
	 * 在用户登录成功后，用于获得用户信息
	 */
	public void initUserInfo(String userId) {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("UserIDs", userId);
		interface1.getData(Constants.USERINFOURL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result != null && !result.equals("")) {
					userInfo = new UserInfo();
					try {
						JSONObject object = new JSONObject(result);
						JSONArray array = object.getJSONArray("Entities");
						JSONObject userObject = array.getJSONObject(0);
						JsonUtils.getUserInfo(userObject, userInfo);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(getApplicationContext(), "获取登录用户信息失败");
						}
					});

				}
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
			interface1.getData(Constants.BASE_CITY_INFO_URL, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					try {
						JSONArray array = new JSONArray(result);
						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);
							BaseCityInfo info = new BaseCityInfo();
							info.setCityName(object.getString("CityName"));
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

	/**
	 * 得到数据库，注意，该数据库是DBUtils
	 */
	public DbUtils getDB() {
		if (db == null)
			db = DbUtils.create(this, "joocolaDB");
		return db;
	}

	/**
	 * 
	 * @return 本次登录的PID
	 */
	public String getPID() {
		return PID;
	}

	/**
	 * 加载add 和 筛选所需的数据 加载完保存在Application中。
	 */
	public ArrayList<IssueInfo> initAddData(final ArrayList<IssueInfo> mInfos, final JoocolaApplication mApplication, final InitAddInfo initAddInfo) {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.getData(Constants.ISSUE_TYPE_URL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				try {
					JSONArray jsonArray = new JSONArray(result);
					for (int i = 0; i < jsonArray.length(); i++) {
						IssueInfo issueInfo = new IssueInfo();
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						issueInfo.setPID(jsonObject.getInt("PID"));
						issueInfo.setSortNo(jsonObject.getInt("SortNo"));
						issueInfo.setPhotoUrl(jsonObject.getString("PhotoUrl"));
						issueInfo.setTypeName(jsonObject.getString("TypeName"));
						mInfos.add(issueInfo);
					}
					mApplication.setIssueInfos(mInfos);
					initAddInfo.initAddInfook();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		return mInfos;
	}

	public interface InitAddInfo {

		public void initAddInfook();
	}

	public ArrayList<String> getmCitys() {
		return mCitys;
	}

	public void setmCitys(ArrayList<String> mCitys) {
		this.mCitys = mCitys;
	}

}

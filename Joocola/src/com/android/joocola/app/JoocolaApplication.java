package com.android.joocola.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.util.Log;

import com.android.joocola.dbmanger.BaseDataInfoManger;
import com.android.joocola.entity.BaseDataInfo;
import com.android.joocola.entity.IssueInfo;
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
	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
		getInfoFromNetwork();
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
}

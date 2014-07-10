package com.android.joocola;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;

import com.android.joocola.entity.BaseDataInfo;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;

public class JoocolaApp extends Application {
	// 存储基础数据
	private List<BaseDataInfo> baseInfoList;
	public static JoocolaApp app;
	private static final String BASEDATAURL = "Sys.BaseDataController.GetDatas.ashx";

	@Override
	public void onCreate() {
		super.onCreate();
		getInfoFromNetwork();
		app = this;
	}

	// 得到基础数据信息
	public void getInfoFromNetwork() {
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
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			Utils.toast(this, "当前网络不可用，请检查网络连接");
		}
	}

	public static JoocolaApp getSelf() {
		return app;
	}
}

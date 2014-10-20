package com.android.joocola.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.IssueDynamicAdapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.AdminMessageContentEntity;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;

/**
 * 报名系统消息界面
 * 
 * @author:bb
 * @see:
 * @since:
 * @copyright © joocola.com
 * @Date:2014年10月19日
 */
public class IssueDynamicFragment extends Fragment {

	private int Type;
	private ListView mListView;
	private String mUserID;
	private ArrayList<AdminMessageContentEntity> mDataList = new ArrayList<AdminMessageContentEntity>();
	private IssueDynamicAdapter mAdapter;
	private TextView mNodata;
	private Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmen_issuedynamic, container, false);
		mListView = (ListView) view.findViewById(R.id.issuedynamic_lv);
		mNodata = (TextView) view.findViewById(R.id.issuedynamic_nodata);
		mAdapter = new IssueDynamicAdapter(mDataList, getActivity(), JoocolaApplication.getInstance().getBitmapCache(), mHandler);
		mListView.setAdapter(mAdapter);
		initData();
		return view;
	}

	public void setmUserID(String mUserID) {
		this.mUserID = mUserID;
	};

	private void initData() {
		mDataList.clear();
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("MsgType", Type + "");
		httpPostInterface.addParams("curUerID", mUserID);
		httpPostInterface.getData(Constants.GET_MSGS_URL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Log.e("bb", result);
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray = jsonObject.getJSONArray("Entities");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject mObject = jsonArray.getJSONObject(i);
						AdminMessageContentEntity mContentEntity = JsonUtils.getAdminMessageContentEntity(mObject);
						mDataList.add(mContentEntity);
					}
					if (mDataList.size() != 0) {
						mListView.setVisibility(View.VISIBLE);
						mNodata.setVisibility(View.GONE);
						mAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}

	public void setType(int type) {
		Type = type;
	}
}

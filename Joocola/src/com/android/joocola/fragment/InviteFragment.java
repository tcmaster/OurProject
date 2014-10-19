package com.android.joocola.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * 邀请系统信息界面
 * 
 * @author:bb
 * @see:
 * @since:
 * @copyright © joocola.com
 * @Date:2014年10月19日
 */
public class InviteFragment extends Fragment {

	private static final String TAG = "Invite_Fragment";

	private TextView mTempTextView;
	private ListView mListView;
	private String mUserID;
	private ArrayList<AdminMessageContentEntity> mDataList = new ArrayList<AdminMessageContentEntity>();
	private IssueDynamicAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmen_issuedynamic, container, false);
		mListView = (ListView) view.findViewById(R.id.issuedynamic_lv);
		mAdapter = new IssueDynamicAdapter(mDataList, getActivity(), JoocolaApplication.getInstance().getBitmapCache());
		mListView.setAdapter(mAdapter);
		initData();
		return view;
	};

	private void initData() {
		mDataList.clear();
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("MsgType", 12 + "");
		httpPostInterface.addParams("curUerID", mUserID);
		httpPostInterface.getData(Constants.GET_MSGS_URL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					JSONArray jsonArray = jsonObject.getJSONArray("Entities");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject mObject = jsonArray.getJSONObject(i);
						AdminMessageContentEntity mContentEntity = JsonUtils.getAdminMessageContentEntity(mObject);
						mDataList.add(mContentEntity);
					}
					mAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setmUserID(String mUserID) {
		this.mUserID = mUserID;
	}
}

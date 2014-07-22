package com.android.joocola.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.joocola.R;
import com.android.joocola.adapter.GetIssueItemAdapter;
import com.android.joocola.entity.GetIssueInfoEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.view.AutoListView;
import com.android.joocola.view.AutoListView.OnLoadListener;
import com.android.joocola.view.AutoListView.OnRefreshListener;


/**
 * 发布预约界面
 * 
 * @author bb
 * 
 */
public class Releasefragment extends Fragment implements OnRefreshListener,
		OnLoadListener {
	private String issue_url = "Bus.AppointController.QueryAppoint.ashx";
	private AutoListView mAutoListView;
	private List<GetIssueInfoEntity> mEntities = new ArrayList<GetIssueInfoEntity>();
	private GetIssueItemAdapter getIssueItemAdapter;
	private BitmapCache bitmapCache = new BitmapCache();
	@SuppressLint("HandlerLeak")
	private Handler releaseHandler  = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Log.e("拿完数据了哦", "<<<----");
				String json = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(json);
					JSONArray jsonArray = jsonObject.getJSONArray("Entities");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = jsonArray.getJSONObject(i);
						GetIssueInfoEntity getIssueInfoEntity = new GetIssueInfoEntity();
						getIssueInfoEntity.setTitle(object.getString("Title"));
						getIssueInfoEntity.setApplyUserCount(object
								.getInt("ApplyUserCount"));
						getIssueInfoEntity.setCostName(object
								.getString("CostName"));
						getIssueInfoEntity.setDescription(object
								.getString("Description"));
						getIssueInfoEntity.setLocationName(object
								.getString("LocationName"));
						getIssueInfoEntity.setPID(object.getInt("PID"));
						getIssueInfoEntity.setPublishDate(object
								.getString("PublishDate"));
						getIssueInfoEntity.setPublisherAge(object
								.getInt("PublisherAge"));
						getIssueInfoEntity.setPublisherAstro(object
								.getString("PublisherAstro"));
						getIssueInfoEntity.setPublisherBirthday(object
								.getString("PublisherBirthday"));
						getIssueInfoEntity.setPublisherName(object
								.getString("PublisherName"));
						getIssueInfoEntity.setPublisherPhoto(object
								.getString("PublisherPhoto"));
						getIssueInfoEntity.setReplyCount(object
								.getInt("ReplyCount"));
						getIssueInfoEntity.setReserveDate(object
								.getString("ReserveDate"));
						getIssueInfoEntity.setSexName(object
								.getString("SexName"));
						getIssueInfoEntity.setState(object.getString("State"));
						getIssueInfoEntity.setTitle(object.getString("Title"));
						mEntities.add(getIssueInfoEntity);
					}
					getIssueItemAdapter = new GetIssueItemAdapter(mEntities,
							getActivity(), bitmapCache);
					mAutoListView.setAdapter(getIssueItemAdapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		};
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_release, container,
				false);
		mAutoListView = (AutoListView) view.findViewById(R.id.issue_listview);
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("ItemsPerPage", 5 + "");
		httpPostInterface.addParams("CurrentPage", 1 + "");
		httpPostInterface.getData(issue_url, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 0;
				message.obj = result;
				releaseHandler.sendMessage(message);
			}
		});

		return view;
	}

	@Override
	public void onLoad() {

	}

	@Override
	public void onRefresh() {

	}
}

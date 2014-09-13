package com.android.joocola.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.joocola.R;
import com.android.joocola.activity.IssuedinvitationDetailsActivity;
import com.android.joocola.adapter.GetIssueItemAdapter;
import com.android.joocola.entity.GetIssueInfoEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.view.AutoListView;
import com.android.joocola.view.AutoListView.OnLoadListener;
import com.android.joocola.view.AutoListView.OnRefreshListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * 发布预约界面
 * 
 * @author bb
 * 
 */
public class Releasefragment extends Fragment implements OnRefreshListener, OnLoadListener {

	private String issue_url = "Bus.AppointController.QueryAppoint.ashx";
	private AutoListView mAutoListView;
	private List<GetIssueInfoEntity> mEntities = new ArrayList<GetIssueInfoEntity>();
	private GetIssueItemAdapter getIssueItemAdapter;
	private BitmapCache bitmapCache = new BitmapCache();
	private int mTotalPagesCount;// 总共有多少页
	private int mCurPageIndex = 1;// 当前显示多少页
	public String Timespan = "0";// 时间段，默认不限
	public String PublishSexID = "0";// 性别，默认不限
	public String TypeID = "-1";// 类型id
	public String PublisherAge = "0";// 年龄id，默认不限
	private ImageLoader mImageLoader;
	/**
	 * 这个参数在为true时说明是通过筛选得到的结果
	 */
	private boolean flag = false;
	@SuppressLint("HandlerLeak")
	private Handler releaseHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case 0:
				mAutoListView.onLoadComplete();
				String json = (String) msg.obj;
				List<GetIssueInfoEntity> newEntities = resolveJson(json);
				mEntities.addAll(newEntities);
				mAutoListView.setResultSize(newEntities.size());
				getIssueItemAdapter.setData(mEntities);
				getIssueItemAdapter.notifyDataSetChanged();
				// getIssueItemAdapter = new GetIssueItemAdapter(mEntities,
				// getActivity(), bitmapCache, mImageLoader);
				// getIssueItemAdapter.notifyDataSetChanged();
				// mAutoListView.setAdapter(getIssueItemAdapter);
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_release, container, false);
		mAutoListView = (AutoListView) view.findViewById(R.id.issue_listview);
		mAutoListView.setOnRefreshListener(this);
		mAutoListView.setOnLoadListener(this);
		mAutoListView.setOnItemClickListener(new MylistviewItemClick());
		mImageLoader = new ImageLoader(Volley.newRequestQueue(getActivity()), bitmapCache);
		getIssueItemAdapter = new GetIssueItemAdapter(mEntities, getActivity(), bitmapCache, mImageLoader);
		mAutoListView.setAdapter(getIssueItemAdapter);
		getData();
		return view;
	}

	/**
	 * 传入的state=1 为
	 * 
	 * @param type
	 */
	private void getData() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("ItemsPerPage", 10 + "");
		httpPostInterface.addParams("CurrentPage", mCurPageIndex + "");
		httpPostInterface.addParams("State", "1");

		if (flag) {
			httpPostInterface.addParams("Timespan", Timespan);
			httpPostInterface.addParams("PublishSexID", PublishSexID);
			if (!TypeID.equals("-1"))
				httpPostInterface.addParams("TypeID", TypeID);
			httpPostInterface.addParams("PublisherAgeType", PublisherAge);
			// flag = false;
			Log.e("bb", flag + "");
		}
		httpPostInterface.getData(issue_url, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 0;
				message.obj = result;
				releaseHandler.sendMessage(message);
				Log.e("bb->", result);
			}
		});
	}

	public void searchData(Intent data) {
		flag = true;
		mEntities.clear();
		Timespan = data.getStringExtra("Timespan");
		PublishSexID = data.getStringExtra("PublishSexID");
		TypeID = data.getStringExtra("TypeID");
		PublisherAge = data.getStringExtra("PublisherAge");
		mCurPageIndex = 1;
		getData();

	}

	@Override
	public void onLoad() {
		Log.e("load", "load");

		if (mCurPageIndex + 1 > mTotalPagesCount) {
			return;
		}
		mCurPageIndex += 1;
		getData();
	}

	@Override
	public void onRefresh() {
		Log.e("load", "onRefresh");
		mAutoListView.onRefreshComplete();
		mEntities.clear();
		mCurPageIndex = 1;
		getData();
	}

	class MylistviewItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if ((position - 1) != mEntities.size()) {
				GetIssueInfoEntity getIssueInfoEntity = mEntities.get(position - 1);
				Intent intent = new Intent(getActivity(), IssuedinvitationDetailsActivity.class);
				intent.putExtra("issueInfo", getIssueInfoEntity);
				startActivity(intent);
			}
		}
	}

	/**
	 * 解析json 返回一个GetIssueInfoEntity的list
	 * 
	 * @param json
	 * @return
	 */
	private List<GetIssueInfoEntity> resolveJson(String json) {
		List<GetIssueInfoEntity> data = new ArrayList<GetIssueInfoEntity>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			mTotalPagesCount = jsonObject.getInt("TotalPagesCount");
			mCurPageIndex = jsonObject.getInt("CurPageIndex");
			JSONArray jsonArray = jsonObject.getJSONArray("Entities");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				GetIssueInfoEntity getIssueInfoEntity = new GetIssueInfoEntity();
				getIssueInfoEntity = JsonUtils.getIssueInfoEntity(object, getIssueInfoEntity);
				data.add(getIssueInfoEntity);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
}

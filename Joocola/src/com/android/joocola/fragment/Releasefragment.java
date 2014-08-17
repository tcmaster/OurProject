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
public class Releasefragment extends Fragment implements OnRefreshListener,
		OnLoadListener {
	private String issue_url = "Bus.AppointController.QueryAppoint.ashx";
	private AutoListView mAutoListView;
	private List<GetIssueInfoEntity> mEntities = new ArrayList<GetIssueInfoEntity>();
	private GetIssueItemAdapter getIssueItemAdapter;
	private BitmapCache bitmapCache = new BitmapCache();
	private int mTotalPagesCount;// 总共有多少页
	private int mCurPageIndex = 1;// 当前显示多少页

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
				getIssueItemAdapter.notifyDataSetChanged();
				break;
			case 1:
				mAutoListView.onLoadComplete();
				String json1 = (String) msg.obj;
				List<GetIssueInfoEntity> newRefreshEntities = resolveJson(json1);
				mEntities.addAll(newRefreshEntities);
				mAutoListView.setResultSize(newRefreshEntities.size());
				getIssueItemAdapter.notifyDataSetChanged();
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
		mAutoListView.setOnRefreshListener(this);
		mAutoListView.setOnLoadListener(this);
		mAutoListView.setOnItemClickListener(new MylistviewItemClick());
		ImageLoader mImageLoader = new ImageLoader(
				Volley.newRequestQueue(getActivity()), bitmapCache);
		getIssueItemAdapter = new GetIssueItemAdapter(mEntities, getActivity(),
				bitmapCache, mImageLoader);
		mAutoListView.setAdapter(getIssueItemAdapter);
		getData(0);
		return view;
	}

	/**
	 * 传入的state=1 为
	 * 
	 * @param type
	 */
	private void getData(final int type) {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("ItemsPerPage", 10 + "");
		httpPostInterface.addParams("CurrentPage", mCurPageIndex + "");
		httpPostInterface.addParams("State", "1");
		httpPostInterface.getData(issue_url, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = type;
				message.obj = result;
				releaseHandler.sendMessage(message);
			}
		});
	}

	@Override
	public void onLoad() {
		Log.e("load", "load");

		if (mCurPageIndex + 1 > mTotalPagesCount) {
			return;
		}
		mCurPageIndex += 1;
		getData(0);
	}

	@Override
	public void onRefresh() {
		Log.e("load", "onRefresh");
		mAutoListView.onRefreshComplete();
		mEntities.clear();
		mCurPageIndex = 1;
		getData(1);
	}

	class MylistviewItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (position < mEntities.size()) {
				GetIssueInfoEntity getIssueInfoEntity = mEntities
						.get(position - 1);
				Intent intent = new Intent(getActivity(),
						IssuedinvitationDetailsActivity.class);
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
				getIssueInfoEntity = JsonUtils.getIssueInfoEntity(object,
						getIssueInfoEntity);
				data.add(getIssueInfoEntity);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return data;
	}
}

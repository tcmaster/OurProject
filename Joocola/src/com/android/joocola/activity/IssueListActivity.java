package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.android.joocola.R;
import com.android.joocola.adapter.GetIssueItemAdapter;
import com.android.joocola.entity.GetIssueInfoEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.view.AutoListView;
import com.android.joocola.view.AutoListView.OnLoadListener;
import com.android.joocola.view.AutoListView.OnRefreshListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * 该Activity 是从个人中心点击各种邀约类型 进入的邀约列表的界面。
 * 
 * @author bb
 * 
 */
public class IssueListActivity extends BaseActivity implements
		OnRefreshListener, OnLoadListener {
	private String type;
	private AutoListView myAutoListView;
	private GetIssueItemAdapter myIssueItemAdapter;
	private String url = "Bus.AppointController.QueryAppoint.ashx";
	private int mTotalPagesCount;// 总共有多少页
	private int mCurPageIndex = 1;// 当前显示多少页
	private BitmapCache bitmapCache;
	private ImageLoader mImageLoader;
	private List<GetIssueInfoEntity> dataList = new ArrayList<GetIssueInfoEntity>();
	private String user_id;
	private SharedPreferences sharedPreferences;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				myAutoListView.onLoadComplete();
				String json = (String) msg.obj;
				List<GetIssueInfoEntity> newEntities = resolveJson(json);
				dataList.addAll(newEntities);
				myAutoListView.setResultSize(newEntities.size());
				myIssueItemAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_issuelist);

		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		initActionBar();
		initListView();
		initData(type);
	}

	/**
	 * 加载listview
	 */
	private void initListView() {
		bitmapCache = new BitmapCache();
		mImageLoader = new ImageLoader(Volley.newRequestQueue(this),
				bitmapCache);
		myAutoListView = (AutoListView) this
				.findViewById(R.id.issuelist_listview);
		myAutoListView.setOnRefreshListener(this);
		myAutoListView.setOnLoadListener(this);
		myIssueItemAdapter = new GetIssueItemAdapter(this, bitmapCache,
				mImageLoader);
		myIssueItemAdapter.setData(dataList);
		myAutoListView.setAdapter(myIssueItemAdapter);
		sharedPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE,
				Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString(Constans.LOGIN_PID, "");
	}

	/**
	 * 加载布局
	 */
	private void initActionBar() {
		useCustomerActionBar();
		getActionBarRight().setVisibility(View.INVISIBLE);
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarleft().setText(type);
	}

	/**
	 * 加载数据
	 */
	private void initData(String type) {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("ItemsPerPage", 10 + "");
		httpPostInterface.addParams("CurrentPage", mCurPageIndex + "");
		if (type.equals("我发起的邀约")) {
			httpPostInterface.addParams("PublisherID", user_id);
		} else if (type.equals("我收藏的邀约")) {
			httpPostInterface.addParams("FavoriteUserID", user_id);
		}
		httpPostInterface.getData(url, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 0;
				message.obj = result;
				mHandler.sendMessage(message);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onLoad() {
		Log.e("load", "load");
		if (mCurPageIndex + 1 > mTotalPagesCount) {
			return;
		}
		mCurPageIndex += 1;
		initData(type);
	}

	@Override
	public void onRefresh() {
		Log.e("load", "refresh");
		myAutoListView.onRefreshComplete();
		mCurPageIndex = 1;
		dataList.clear();
		initData(type);
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

package com.android.joocola.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.android.joocola.R;
import com.android.joocola.adapter.ShowEvaluateAdapter;
import com.android.joocola.entity.AppointScoreEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;

/**
 * 这是显示评价的界面。
 * 
 * @author bb
 * 
 */
public class ShwoEvaluateActivity extends BaseActivity {

	private String mAppointID;// 邀约id
	private String mRelateUserID;// 查用邀约的所用的pid;
	private ListView mListView;
	private ArrayList<AppointScoreEntity> mList;
	private ShowEvaluateAdapter mAdapter;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String result = (String) msg.obj;
				resoloveJson(result);
				mAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showevaluate);
		Intent intent = getIntent();
		mAppointID = intent.getStringExtra("AppointID");
		mRelateUserID = intent.getStringExtra("RelateUserID");
		initListview();
		initActionbar();
		initData();
	}

	private void initListview() {
		mListView = (ListView) this.findViewById(R.id.showevaluate_listview);
		mList = new ArrayList<AppointScoreEntity>();
		BitmapCache bitmapCache = new BitmapCache();
		mAdapter = new ShowEvaluateAdapter(mList, this, bitmapCache);
		mListView.setAdapter(mAdapter);

	}

	private void initActionbar() {
		useCustomerActionBar();
		getActionBarleft().setText("查看评价");
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
	}

	private void initData() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("AppointID", mAppointID);
		httpPostInterface.addParams("RelateUserID", mRelateUserID);
		httpPostInterface.getData(Constants.QUERY_APPOINT_SCORE, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 0;
				message.obj = result;
				mHandler.sendMessage(message);
			}
		});
	}

	private void resoloveJson(String Json) {
		try {
			JSONObject jsonObject = new JSONObject(Json);
			JSONArray jsonArray = jsonObject.getJSONArray("Entities");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				AppointScoreEntity appointScoreEntity = JsonUtils.getAppointScoreEntity(object);
				mList.add(appointScoreEntity);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

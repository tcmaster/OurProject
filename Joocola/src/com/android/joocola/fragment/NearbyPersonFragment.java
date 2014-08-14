package com.android.joocola.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.joocola.R;
import com.android.joocola.adapter.NearByPersonAdapter;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.view.AutoListView;
import com.android.joocola.view.AutoListView.OnLoadListener;
import com.android.joocola.view.AutoListView.OnRefreshListener;

public class NearbyPersonFragment extends Fragment implements
		OnRefreshListener, OnLoadListener {
	private AutoListView mAutoListView;
	private final String url = "Sys.UserController.GetUserInfos.ashx";
	private NearByPersonAdapter mNearByPersonAdapter;
	private BitmapCache bitmapCache;
	private int distance = 1000;
	private SharedPreferences sharedPreferences;
	private String userId;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String result = (String) msg.obj;
				Log.e("呵呵", result);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getActivity().getSharedPreferences(
				Constans.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		userId = sharedPreferences.getString(Constans.LOGIN_PID, 0 + "");

	}

	public android.view.View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.nearby_person, container, false);
		initListview(view);
		return view;
	};

	private void initListview(View view) {
		mAutoListView = (AutoListView) view
				.findViewById(R.id.nearbyperson_listview);
		bitmapCache = new BitmapCache();
		mNearByPersonAdapter = new NearByPersonAdapter(getActivity(),
				bitmapCache);
		initData();

	}

	private void initData() {
		if (!userId.equals("0")) {
			HttpPostInterface httpPostInterface = new HttpPostInterface();
			httpPostInterface.addParams("DistanceFromCurUser", distance + "");
			httpPostInterface.addParams("CurUserID", userId);
			httpPostInterface.getData(url, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					Message message = Message.obtain();
					message.obj = result;
					message.what = 0;
					mHandler.sendMessage(message);
				}
			});
		}

	}

	@Override
	public void onLoad() {
		distance += 1000;
		initData();
	}

	@Override
	public void onRefresh() {
		distance = 1000;
		initData();

	}
}

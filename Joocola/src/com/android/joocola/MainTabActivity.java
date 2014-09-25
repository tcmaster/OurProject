package com.android.joocola;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.android.joocola.activity.FilterActivity;
import com.android.joocola.activity.IssuedinvitationActivity;
import com.android.joocola.activity.PersonalCenterActivity;
import com.android.joocola.adapter.IssueAdapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.chat.EaseMobChat;
import com.android.joocola.entity.IssueInfo;
import com.android.joocola.fragment.Messagefragment;
import com.android.joocola.fragment.MyFragmentPagerAdapter;
import com.android.joocola.fragment.Nearbyfragment;
import com.android.joocola.fragment.Releasefragment;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;

/**
 * 主界面
 * 
 * @author bb
 * 
 */
public class MainTabActivity extends FragmentActivity implements AMapLocationListener {

	private RelativeLayout tab_realease, tab_nearby, tab_message;
	private ViewPager mPager;
	private ActionBar mActionBar;
	private int mBackKeyPressedTimes = 0;
	private ArrayList<Fragment> fragmentsList;
	private ArrayList<IssueInfo> mIssueInfos;
	private JoocolaApplication mJoocolaApplication;
	private BitmapCache bitmapCache;
	private SharedPreferences sharedPreferences;
	private LocationManagerProxy aMapLocManager = null;
	private AMapLocation aMapLocation; // 用于判断定位超时
	private Editor editor;
	private String user_pid;
	private final String locationUrl = "Sys.UserController.UploadLocation.ashx";
	private ImageView mRedPoint;
	private final int REQUEST_CODE = 221;
	private CustomerDialog customerDialog;
	private ImageView img_tab_realease, img_tab_nearby, img_tab_message;
	private TextView tv_tab_realease, tv_tab_nearby, tv_tab_message;
	private Resources res;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				showIssueDialog(mIssueInfos, bitmapCache);
				break;

			default:
				break;
			}
		};
	};

	// getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);//
	// 用来更新menu
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Message message = new Message();
		message.getCallback();
		this.setContentView(R.layout.activity_maintab);
		mJoocolaApplication = JoocolaApplication.getInstance();
		mIssueInfos = new ArrayList<IssueInfo>();
		sharedPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		user_pid = sharedPreferences.getString(Constans.LOGIN_PID, "0");
		res = getResources();
		initActionbar();
		initView();
		initViewPager();
		initLocation();
		bitmapCache = JoocolaApplication.getInstance().getBitmapCache();
		JoocolaApplication.getInstance().initAddData(mIssueInfos, mJoocolaApplication);

	}

	/**
	 * 加载定位相关
	 */
	private void initLocation() {
		aMapLocManager = LocationManagerProxy.getInstance(this);
		/*
		 * mAMapLocManager.setGpsEnable(false);// 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
		 * Location API定位采用GPS和网络混合定位方式 ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
		aMapLocManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 300000, 10, this);
		mHandler.sendEmptyMessageDelayed(10, 10000);// 设置超过10秒还没有定位到就停止定位
	}

	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.maintab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		Intent intent = new Intent();
		switch (id) {
		case R.id.action_add:
			mIssueInfos = mJoocolaApplication.getIssueInfos();
			if (mIssueInfos == null || mIssueInfos.size() == 0) {
				JoocolaApplication.getInstance().initAddData(mIssueInfos, mJoocolaApplication);
				mHandler.sendEmptyMessage(1);
			} else {
				mHandler.sendEmptyMessage(1);
			}

			break;
		case R.id.action_loudou:
			intent.setClass(MainTabActivity.this, FilterActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
			break;
		case R.id.action_me:
			intent.setClass(this, PersonalCenterActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initActionbar() {
		mActionBar = getActionBar();
		mActionBar.setTitle("北京");
		mActionBar.setDisplayShowHomeEnabled(false);
	}

	private void initView() {

		tab_realease = (RelativeLayout) this.findViewById(R.id.tab_realease);
		tab_nearby = (RelativeLayout) this.findViewById(R.id.tab_nearby);
		tab_message = (RelativeLayout) this.findViewById(R.id.tab_message);
		img_tab_realease = (ImageView) this.findViewById(R.id.img_tab_realease);
		img_tab_nearby = (ImageView) this.findViewById(R.id.img_tab_nearby);
		img_tab_message = (ImageView) this.findViewById(R.id.img_tab_message);
		tv_tab_realease = (TextView) this.findViewById(R.id.tv_tab_realease);
		tv_tab_nearby = (TextView) this.findViewById(R.id.tv_tab_nearby);
		tv_tab_message = (TextView) this.findViewById(R.id.tv_tab_message);
		tab_realease.setOnClickListener(new MyOnClickListener(0));
		tab_nearby.setOnClickListener(new MyOnClickListener(1));
		tab_message.setOnClickListener(new MyOnClickListener(2));
		mRedPoint = (ImageView) this.findViewById(R.id.msg_redPoint);
		mRedPoint.setVisibility(View.INVISIBLE);

	}

	private void initViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		mPager.setOffscreenPageLimit(3);
		fragmentsList = new ArrayList<Fragment>();

		Fragment releaseFragment = new Releasefragment();
		Fragment nearbyFragment = new Nearbyfragment();
		Fragment messageFragment = new Messagefragment();

		fragmentsList.add(releaseFragment);
		fragmentsList.add(nearbyFragment);
		fragmentsList.add(messageFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public class MyOnClickListener implements View.OnClickListener {

		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			if (index == 2) {
				mRedPoint.setVisibility(View.INVISIBLE);
			}
			mPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				tab_realease.setBackgroundResource(R.drawable.button_normal);
				tab_message.setBackgroundResource(R.drawable.maintab_bg);
				tab_nearby.setBackgroundResource(R.drawable.maintab_bg);
				img_tab_realease.setImageResource(R.drawable.tab_realease_touch);
				img_tab_nearby.setImageResource(R.drawable.tab_nearby_normal);
				img_tab_message.setImageResource(R.drawable.tab_message_normal);
				tv_tab_realease.setTextColor(res.getColor(R.color.white));
				tv_tab_nearby.setTextColor(res.getColor(R.color.black));
				tv_tab_message.setTextColor(res.getColor(R.color.black));
				break;
			case 1:
				tab_realease.setBackgroundResource(R.drawable.maintab_bg);
				tab_nearby.setBackgroundResource(R.drawable.button_normal);
				tab_message.setBackgroundResource(R.drawable.maintab_bg);
				img_tab_realease.setImageResource(R.drawable.tab_realease_normal);
				img_tab_nearby.setImageResource(R.drawable.tab_nearby_touch);
				img_tab_message.setImageResource(R.drawable.tab_message_normal);
				tv_tab_realease.setTextColor(res.getColor(R.color.black));
				tv_tab_nearby.setTextColor(res.getColor(R.color.white));
				tv_tab_message.setTextColor(res.getColor(R.color.black));
				break;
			case 2:
				tab_realease.setBackgroundResource(R.drawable.maintab_bg);
				tab_nearby.setBackgroundResource(R.drawable.maintab_bg);
				tab_message.setBackgroundResource(R.drawable.button_normal);
				img_tab_realease.setImageResource(R.drawable.tab_realease_normal);
				img_tab_nearby.setImageResource(R.drawable.tab_nearby_normal);
				img_tab_message.setImageResource(R.drawable.tab_message_touch);
				mRedPoint.setVisibility(View.INVISIBLE);
				tv_tab_realease.setTextColor(res.getColor(R.color.black));
				tv_tab_nearby.setTextColor(res.getColor(R.color.black));
				tv_tab_message.setTextColor(res.getColor(R.color.white));
				break;
			default:
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onBackPressed() {
		Utils.toast(this, "再次点击退出应用");
		if (mBackKeyPressedTimes == 0) {
			mBackKeyPressedTimes = 1;
			new Thread() {

				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						mBackKeyPressedTimes = 0;
					}
				}
			}.start();
			return;
		} else {
			EaseMobChat.getInstance().endWork();
			// stopService(new Intent(this, DefineService.class));
			System.exit(0);

		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopLocation();
	}

	private void showIssueDialog(final ArrayList<IssueInfo> issueInfos, final BitmapCache bitmapCache) {
		customerDialog = new CustomerDialog(MainTabActivity.this, R.layout.dialog_issuedinvitation);
		customerDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.TOP);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
				GridView gridView = (GridView) dlg.findViewById(R.id.issue_gridview);
				IssueAdapter issueAdapter = new IssueAdapter(MainTabActivity.this, issueInfos, bitmapCache);
				gridView.setAdapter(issueAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Intent intent = new Intent(MainTabActivity.this, IssuedinvitationActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("PID", issueInfos.get(arg2).getPID());
						bundle.putString("title", issueInfos.get(arg2).getTypeName());
						intent.putExtras(bundle);
						startActivity(intent);
						customerDialog.dismissDlg();
					}
				});

			}
		});
		customerDialog.showDlg();
	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {
	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			this.aMapLocation = location;// 判断超时机制
			Double geoLat = location.getLatitude();// x
			Double geoLng = location.getLongitude();// y
			editor.putString("LocationCity", location.getCity());
			editor.putString("LocationX", geoLat + "");
			editor.putString("LocationY", geoLng + "");
			String str = location.getCity();
			if (!TextUtils.isEmpty(str)) {
				getActionBar().setTitle(str);
			}
			sendLocationInfo(geoLat + "", geoLng + "");

		}
	}

	/**
	 * 将坐标地址发送给服务器。
	 * 
	 * @param LocationX
	 * @param LocatitonY
	 */
	private void sendLocationInfo(String LocationX, String LocatitonY) {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("x", LocationX);
		httpPostInterface.addParams("y", LocatitonY);
		httpPostInterface.addParams("userID", user_pid);
		httpPostInterface.getData(locationUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Log.e("发送location", result);
			}
		});
	}

	/**
	 * 设置 红点是否显示, 如果参数为true 则显示 否则为隐藏
	 * 
	 * @param isVisible
	 */
	public void setRedPointVisible(boolean isVisible) {
		if (mRedPoint != null) {
			if (isVisible) {
				mRedPoint.setVisibility(View.VISIBLE);
			} else {
				mRedPoint.setVisibility(View.INVISIBLE);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			mPager.setCurrentItem(0);
			if (fragmentsList.get(0) != null) {
				Releasefragment fg = (Releasefragment) fragmentsList.get(0);
				fg.searchData(data);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}

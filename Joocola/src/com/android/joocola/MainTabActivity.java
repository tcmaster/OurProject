package com.android.joocola;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.joocola.activity.IssuedinvitationActivity;
import com.android.joocola.app.JoocolaApplication;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * 主界面
 * 
 * @author bb
 * 
 */
public class MainTabActivity extends FragmentActivity {
	private RelativeLayout tab_realease, tab_nearby, tab_message;
	private ViewPager mPager;
	private ActionBar mActionBar;
	private int mBackKeyPressedTimes = 0;
	private ArrayList<Fragment> fragmentsList;
	private final String issue_url = "Bus.AppointController.GetTypes.ashx";
	private ArrayList<IssueInfo> mIssueInfos;
	private JoocolaApplication mJoocolaApplication;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				showIssueDialog(mIssueInfos);
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
		this.setContentView(R.layout.activity_maintab);
		mJoocolaApplication = JoocolaApplication.getInstance();
		mIssueInfos = new ArrayList<IssueInfo>();
		initActionbar();
		initView();
		initViewPager();
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
		switch (id) {
		case R.id.action_add:
			mIssueInfos = mJoocolaApplication.getIssueInfos();
			if (mIssueInfos == null || mIssueInfos.size() == 0) {
			HttpPostInterface httpPostInterface = new HttpPostInterface();
			httpPostInterface.getData(issue_url, new HttpPostCallBack() {
				
				@Override
				public void httpPostResolveData(String result) {
					try {
						JSONArray jsonArray = new JSONArray(result);
						for (int i = 0; i < jsonArray.length(); i++) {
							IssueInfo issueInfo = new IssueInfo();
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							issueInfo.setPID(jsonObject.getInt("PID"));
							issueInfo.setSortNo(jsonObject.getInt("SortNo"));
							issueInfo.setPhotoUrl(jsonObject
									.getString("PhotoUrl"));
							issueInfo.setTypeName(jsonObject
									.getString("TypeName"));
							mIssueInfos.add(issueInfo);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
					mHandler.sendEmptyMessage(1);
				}
				});
			} else {
				mHandler.sendEmptyMessage(1);
			}

			break;
		case R.id.action_loudou:
			Log.i("option", "loudou");
			break;
		case R.id.action_me:
			Log.i("option", "me1");
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

		// mActionBar.setDisplayShowCustomEnabled(true);
		// mActionBar.setCustomView(R.layout.maintab_actionbar);
	}
	private void initView() {

		tab_realease = (RelativeLayout) this.findViewById(R.id.tab_realease);
		tab_nearby = (RelativeLayout) this.findViewById(R.id.tab_nearby);
		tab_message = (RelativeLayout) this.findViewById(R.id.tab_message);
		tab_realease.setOnClickListener(new MyOnClickListener(0));
		tab_nearby.setOnClickListener(new MyOnClickListener(1));
		tab_message.setOnClickListener(new MyOnClickListener(2));

	}

	private void initViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		fragmentsList = new ArrayList<Fragment>();

		Fragment releaseFragment = new Releasefragment();
		Fragment nearbyFragment = new Nearbyfragment();
		Fragment messageFragment = new Messagefragment();

		fragmentsList.add(releaseFragment);
		fragmentsList.add(nearbyFragment);
		fragmentsList.add(messageFragment);

		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentsList));
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
			mPager.setCurrentItem(index);
		}
	};

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			Log.i("bb maintab", arg0 + "");
			switch (arg0) {
			case 0:
				tab_realease.setBackgroundResource(R.drawable.button_normal);
				tab_nearby.setBackgroundResource(R.drawable.maintab_bg);
				tab_message.setBackgroundResource(R.drawable.maintab_bg);
				break;
			case 1:
				tab_realease.setBackgroundResource(R.drawable.maintab_bg);
				tab_nearby.setBackgroundResource(R.drawable.button_normal);
				tab_message.setBackgroundResource(R.drawable.maintab_bg);
				break;
			case 2:
				tab_realease.setBackgroundResource(R.drawable.maintab_bg);
				tab_nearby.setBackgroundResource(R.drawable.maintab_bg);
				tab_message.setBackgroundResource(R.drawable.button_normal);
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
			this.finish();
			System.exit(0);
		}
		super.onBackPressed();
	}

	private void showIssueDialog(final ArrayList<IssueInfo> issueInfos) {
		CustomerDialog customerDialog = new CustomerDialog(
				MainTabActivity.this, R.layout.dialog_issuedinvitation);
		customerDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.TOP);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.WRAP_CONTENT);
				GridView gridView = (GridView) dlg
						.findViewById(R.id.issue_gridview);
				IssueAdapter issueAdapter = new IssueAdapter(
						MainTabActivity.this, issueInfos);
				gridView.setAdapter(issueAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Intent intent = new Intent(MainTabActivity.this,
								IssuedinvitationActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("PID", issueInfos.get(arg2).getPID());
						bundle.putString("title", issueInfos.get(arg2)
								.getTypeName());
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});

			}
		});
		customerDialog.showDlg();
	}
	class IssueAdapter extends BaseAdapter {
		private LayoutInflater layoutInflater;
		private ImageLoader mImageLoader;
		private ArrayList<IssueInfo> issueInfos;

		public IssueAdapter(Context mContext, ArrayList<IssueInfo> infos) {
			layoutInflater = LayoutInflater.from(mContext);
			issueInfos = infos;
			mImageLoader = new ImageLoader(Volley.newRequestQueue(mContext),
					new BitmapCache());
		}

		@Override
		public int getCount() {
			return issueInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return issueInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHodler viewHodler;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.issue_grid_item,
						null);
				viewHodler = new ViewHodler();
				viewHodler.issueIcon = (NetworkImageView) convertView
						.findViewById(R.id.issue_griditem_img);
				viewHodler.issueText = (TextView) convertView
						.findViewById(R.id.issue_griditem_txt);
				convertView.setTag(viewHodler);
			} else {
				viewHodler = (ViewHodler) convertView.getTag();
			}
			IssueInfo issueInfo = issueInfos.get(position);
			String imageUrl = Constans.URL + issueInfo.getPhotoUrl();
			String name = issueInfo.getTypeName();
			viewHodler.issueIcon.setDefaultImageResId(R.drawable.ic_launcher);
			viewHodler.issueIcon.setErrorImageResId(R.drawable.ic_launcher);
			viewHodler.issueIcon.setImageUrl(imageUrl, mImageLoader);
			viewHodler.issueText.setText(name);
			return convertView;
		}

	}

	private class ViewHodler {
		/**
		 * 类别LOGO
		 */
		private NetworkImageView issueIcon;
		/**
		 * 类别名称
		 */
		private TextView issueText;
	}
}

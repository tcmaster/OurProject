package com.android.joocola;

import java.util.ArrayList;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.joocola.fragment.Messagefragment;
import com.android.joocola.fragment.MyFragmentPagerAdapter;
import com.android.joocola.fragment.Nearbyfragment;
import com.android.joocola.fragment.Releasefragment;

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

	// getWindow().invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL);//
	// 用来更新menu
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_maintab);
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
}

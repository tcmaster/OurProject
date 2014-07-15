package com.android.joocola;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

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
	private TextView tv_release, tv_nearby, tv_message;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentsList;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_maintab);
		initView();
		initViewPager();
	}

	private void initView() {
		tv_message = (TextView) this.findViewById(R.id.tv_tab_message);
		tv_nearby = (TextView) this.findViewById(R.id.tv_tab_nearby);
		tv_release = (TextView) this.findViewById(R.id.tv_tab_realease);
		tv_release.setOnClickListener(new MyOnClickListener(0));
		tv_nearby.setOnClickListener(new MyOnClickListener(1));
		tv_message.setOnClickListener(new MyOnClickListener(2));

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

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}

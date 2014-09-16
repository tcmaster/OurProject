package com.android.joocola;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.joocola.fragment.GuideAFragment;
import com.android.joocola.fragment.GuideBFragment;
import com.android.joocola.fragment.GuideCFragment;
import com.android.joocola.fragment.MyFragmentPagerAdapter;

public class GuideActivity extends FragmentActivity {

	// 定义ViewPager对象
	private ViewPager viewPager;

	// 定义一个ArrayList来存放View
	private ArrayList<Fragment> views;

	// 定义底部小点图片
	private ImageView pointImage0, pointImage1, pointImage2;

	// 当前的位置索引值
	private int currIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide);
		Log.e("bb", "进入GuideActivity");
		initView();
		initData();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {

		// 实例化ViewPager
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setOffscreenPageLimit(3);
		// 实例化ArrayList对象
		views = new ArrayList<Fragment>();
		Fragment a = new GuideAFragment();
		Fragment b = new GuideBFragment();
		Fragment c = new GuideCFragment();
		views.add(a);
		views.add(b);
		views.add(c);
		// 实例化底部小点图片对象
		pointImage0 = (ImageView) findViewById(R.id.page0);
		pointImage1 = (ImageView) findViewById(R.id.page1);
		pointImage2 = (ImageView) findViewById(R.id.page2);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		// 设置监听
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 设置适配器数据
		viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				pointImage0.setImageResource(R.drawable.page_indicator_focused);
				pointImage1.setImageResource(R.drawable.page_indicator_unfocused);
				pointImage2.setImageResource(R.drawable.page_indicator_unfocused);
				break;
			case 1:
				pointImage0.setImageResource(R.drawable.page_indicator_unfocused);
				pointImage1.setImageResource(R.drawable.page_indicator_focused);
				pointImage2.setImageResource(R.drawable.page_indicator_unfocused);
				break;
			case 2:

				pointImage0.setImageResource(R.drawable.page_indicator_unfocused);
				pointImage1.setImageResource(R.drawable.page_indicator_unfocused);
				pointImage2.setImageResource(R.drawable.page_indicator_focused);
				break;
			}
			currIndex = position;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

}

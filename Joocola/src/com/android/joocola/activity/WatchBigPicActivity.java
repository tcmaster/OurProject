package com.android.joocola.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.android.joocola.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 点击图片看大图的界面，这个界面可以将当前的图片保存到本地相册，以及滑动浏览所有图片 这个界面用到了XUtils
 * 
 * @author lixiaosong
 * 
 */
public class WatchBigPicActivity extends Activity {
	/**
	 * 展示图片的ViewPager
	 */
	@ViewInject(R.id.bigPic_vp)
	private ViewPager photo_vp;
	/**
	 * 图片地址
	 */
	private List<String> imgUrls;
	/**
	 * 当前点击位置
	 */
	private int currentItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_big_pic);
		ViewUtils.inject(this);
		imgUrls = getIntent().getStringArrayListExtra("imgUrls");
		currentItem = getIntent().getIntExtra("position", 0);
	}

	private void initActionBar() {

	}

	private void initViewPager() {
		photo_vp.setAdapter(new Photo_Vp_Adapter());
		photo_vp.setCurrentItem(currentItem);
	}

	private class Photo_Vp_Adapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imgUrls.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			return super.instantiateItem(container, position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			super.destroyItem(container, position, object);
		}

	}

}

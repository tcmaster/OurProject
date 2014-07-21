package com.android.joocola.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.android.joocola.R;

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
	private ViewPager photo_vp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_big_pic);
	}

	private class Photo_Vp_Adapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return false;
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

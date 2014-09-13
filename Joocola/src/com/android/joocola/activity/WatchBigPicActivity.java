package com.android.joocola.activity;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.joocola.R;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 点击图片看大图的界面，这个界面可以将当前的图片保存到本地相册，以及滑动浏览所有图片 这个界面用到了XUtils
 * 
 * @author lixiaosong
 * 
 */
public class WatchBigPicActivity extends BaseActivity {
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
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_big_pic);
		ViewUtils.inject(this);
		imgUrls = getIntent().getStringArrayListExtra("imgUrls");
		currentItem = getIntent().getIntExtra("position", 0);
		handler = new Handler();
		initActionBar();
		initViewPager();
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarleft().setText((currentItem + 1) + "/" + imgUrls.size());
		getActionBarleft().setTextColor(
				getResources().getColor(R.color.deepgray));
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setText("保存");
		getActionBarRight().setBackgroundResource(R.drawable.btnclick);
		getActionBarRight().setTextColor(
				getResources().getColor(R.color.deepgray));
		getActionBarRight().setGravity(Gravity.CENTER);
		getActionBarRight().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * 开启线程，保存下载图片
				 */
				Utils.toast(WatchBigPicActivity.this, "图片开始下载");
				new Thread(new Runnable() {

					@Override
					public void run() {
						final String path = Utils.getNetBitmap(Constans.URL
								+ imgUrls.get(photo_vp.getCurrentItem()));
						if (path == null) {
							handler.post(new Runnable() {

								@Override
								public void run() {
									Utils.toast(WatchBigPicActivity.this,
											"图片未成功保存，请检查网络连接");

								}
							});
						} else {
							handler.post(new Runnable() {

								@Override
								public void run() {
									Utils.toast(WatchBigPicActivity.this,
											"图片已经保存成功,保存地址是" + path);

								}
							});
						}

					}
				}).start();
			}
		});
	}

	private void initViewPager() {
		photo_vp.setAdapter(new Photo_Vp_Adapter());
		photo_vp.setCurrentItem(currentItem);
		photo_vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				getActionBarleft().setText((arg0 + 1) + "/" + imgUrls.size());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
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
			View view = LayoutInflater.from(WatchBigPicActivity.this).inflate(
					R.layout.item_vp_watchbigimg, null);
			BitmapUtils utils = new BitmapUtils(WatchBigPicActivity.this);
			utils.display(view, Constans.URL + imgUrls.get(position),
					new BitmapLoadCallBack<View>() {
						@Override
						public void onPreLoad(View container, String uri,
								BitmapDisplayConfig config) {
							ImageView iV = (ImageView) container
									.findViewById(R.id.vp_bigimg);
							iV.setImageDrawable(getResources().getDrawable(
									R.drawable.loading_medium));
							Animation anim = AnimationUtils.loadAnimation(
									WatchBigPicActivity.this,
									R.anim.progress_rotate);
							iV.startAnimation(anim);
							super.onPreLoad(container, uri, config);
						}

						@Override
						public void onLoadCompleted(View arg0, String arg1,
								Bitmap arg2, BitmapDisplayConfig arg3,
								BitmapLoadFrom arg4) {
							ImageView iV = (ImageView) arg0
									.findViewById(R.id.vp_bigimg);
							iV.clearAnimation();
							iV.setImageBitmap(arg2);
						}

						@Override
						public void onLoadFailed(View arg0, String arg1,
								Drawable arg2) {
							ImageView iV = (ImageView) arg0
									.findViewById(R.id.vp_bigimg);
							iV.clearAnimation();
							iV.setImageBitmap(null);
						}
					});
			((ViewPager) container).addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((RelativeLayout) object);
		}

	}

}

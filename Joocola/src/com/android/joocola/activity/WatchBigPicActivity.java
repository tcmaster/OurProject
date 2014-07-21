package com.android.joocola.activity;

import android.app.Activity;
import android.os.Bundle;

import com.android.joocola.R;

/**
 * 点击图片看大图的界面，这个界面可以将当前的图片保存到本地相册，以及滑动浏览所有图片 这个界面用到了XUtils
 * 
 * @author lixiaosong
 * 
 */
public class WatchBigPicActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_watch_big_pic);
	}

}

package com.android.joocola.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.joocola.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 聊天看大图界面
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-10-20
 */
public class BigPicActivity extends BaseActivity {

	/**
	 * 大图容器
	 */
	@ViewInject(R.id.chat_big_pic)
	private ImageView iv_chat_big_pic;
	/**
	 * 图片工具
	 */
	private BitmapUtils utils;
	/**
	 * 本界面的大图url
	 */
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_show_pic);
		ViewUtils.inject(this);
		initData();
		initActionBar();
		url = getIntent().getStringExtra("big_url");
		if (url != null || !url.equals("")) {// 显示大图
			LogUtils.v("图片地址是" + url);
			if (url.startsWith("http")) {
				// 网络图片
				utils.display(iv_chat_big_pic, url);
			} else {
				iv_chat_big_pic.setImageBitmap(BitmapFactory.decodeFile(url));
			}
		}
	}

	private void initData() {
		utils = new BitmapUtils(this);
		utils.configDefaultLoadFailedImage(R.drawable.logo);
		utils.configDefaultLoadingImage(R.drawable.logo);
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarleft().setText("");
		getActionBarTitle().setText("");
		getActionBarRight().setText("");
	}
}

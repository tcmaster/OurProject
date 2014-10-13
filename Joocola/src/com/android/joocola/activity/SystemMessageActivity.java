package com.android.joocola.activity;

import android.os.Bundle;

import com.android.joocola.R;
import com.android.joocola.view.AutoListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 系统消息列表类，用于存放系统消息
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-10-13
 */
public class SystemMessageActivity extends BaseActivity {

	/**
	 * 消息列表的listView
	 */
	@ViewInject(R.id.sm_listview)
	private AutoListView lv_sm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_message);
		ViewUtils.inject(this);
	}

}

package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.android.joocola.R;
import com.android.joocola.adapter.SystemMessageAdapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.AdminMessage;
import com.android.joocola.utils.Constants;
import com.android.joocola.view.AutoListView;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
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
	/**
	 * 数据源
	 */
	private List<AdminMessage> dataSource;
	/**
	 * 消息列表的适配器
	 */
	private SystemMessageAdapter adapter;
	/**
	 * 数据库工具
	 */
	private DbUtils db;
	/**
	 * 本界面的广播
	 */
	private MyBroadcaseReceiver receiver;
	/**
	 * 日志开关
	 */
	private boolean DEBUG = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_message);
		ViewUtils.inject(this);
		initData();
	}

	/**
	 * 用于接收系统消息的广播
	 * 
	 * @author:LiXiaoSong
	 * @copyright © joocola.com
	 * @Date:2014-10-14
	 */
	private class MyBroadcaseReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateData();
		}

	}

	/**
	 * 更新数据
	 * 
	 * @author: LiXiaosong
	 * @date:2014-10-14
	 */
	private void updateData() {
		try {
			List<AdminMessage> temp = db.findAll(Selector.from(AdminMessage.class).where("user", "=", JoocolaApplication.getInstance().getPID()));
			if (DEBUG)
				LogUtils.v("更新了数据");
			if (temp != null && temp.size() != 0) {
				adapter.bindData(temp);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化本界面的数据
	 * 
	 * @author: LiXiaosong
	 * @date:2014-10-14
	 */
	private void initData() {
		db = JoocolaApplication.getInstance().getDB();
		dataSource = new ArrayList<AdminMessage>();
		adapter = new SystemMessageAdapter(dataSource, this);
		lv_sm.setAdapter(adapter);
		/**
		 * 从数据库获取最新数据
		 */
		updateData();
		/**
		 * 初始化并注册广播
		 */
		receiver = new MyBroadcaseReceiver();
		IntentFilter filter = new IntentFilter(Constants.CHAT_ADMIN_ACTION);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onDestroy() {
		if (receiver != null)
			unregisterReceiver(receiver);
		super.onDestroy();
	}

}

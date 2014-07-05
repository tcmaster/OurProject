package com.android.doubanmovie.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.doubanmovie.datasrc.ShowData;
import com.android.doubanmovie.httptask.GetJsonTask;
import com.android.doubanmovie.provider.BackupDataHelper;

public class BackupService extends Service {
	private IBinder binder;
	private ShowData data = null;

	@Override
	public void onCreate() {
		binder = new MyBinder();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.v("test", "已经绑定");
		return binder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.v("test", "已经解除绑定");
		return super.onUnbind(intent);
	}

	public class MyBinder extends Binder {
		public BackupService getService() {
			return BackupService.this;
		}
	}

	// 重新从网络下载数据,更新UI
	public void refresh(final Uri url) {
		new GetJsonTask(GetJsonTask.SHOWTASK,
				new GetJsonTask.JsonCallBackAboutShow() {

					@Override
					public void getData(ShowData data) {
						// 得到网络数据，提示UI更新
						BackupDataHelper.BackupData(data, BackupService.this,
								url);
						Toast.makeText(BackupService.this, "刷新成功",
								Toast.LENGTH_SHORT).show();
					}
				}, null).execTask();

	}

	public ShowData getShowData() {
		return data;
	}

	public void backupData(final Uri url) {

		data = BackupDataHelper.getShowData(this, url);
		if (data == null) {
			// 数据库中无数据,下载后台数据
			Log.v("test", "数据库中无数据,下载后台数据");
			refresh(url);
		} else {
			// 数据库中有数据，更新UI
			Log.v("test", "首次运行，刷新一次数据库");
			BackupDataHelper.BackupData(data, this, url);
			// 后台下载数据，取得最新数据
			refresh(url);
		}
	}

}

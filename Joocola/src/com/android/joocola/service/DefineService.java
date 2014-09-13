package com.android.joocola.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 这个服务主要用于防止APP在后台运行时被意外关闭
 * 
 * @author:LiXiaoSong
 * @see:
 * @since:
 * @copyright © joocola.com
 * @Date:2014-9-12
 */
public class DefineService extends Service {

	// 关闭服务的标记
	public boolean flag = false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Notification note = new Notification(0, null, System.currentTimeMillis());
				note.flags |= Notification.FLAG_NO_CLEAR;
				startForeground(200, note);
				while (!flag)
					;
				stopForeground(true);

			}
		}).start();

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		flag = true;
		super.onDestroy();
	}

}

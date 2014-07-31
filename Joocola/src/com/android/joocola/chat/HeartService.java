package com.android.joocola.chat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 发送心跳包的服务
 * 
 * @author lixiaosong
 * 
 */
public class HeartService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}

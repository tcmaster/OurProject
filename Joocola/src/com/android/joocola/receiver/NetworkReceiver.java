package com.android.joocola.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import com.android.joocola.utils.Utils;

public class NetworkReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		State wifiState = null;
		State mobileState = null;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		final Context c = context;
		if (wifiState != null && mobileState != null
				&& State.CONNECTED != wifiState
				&& State.CONNECTED != mobileState) {
			Utils.toast(context, "网络已断开");
		} else {
			// 无线网络连接成功
			Utils.toast(context, "网络已恢复");
		}

	}

}

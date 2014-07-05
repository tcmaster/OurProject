package com.android.doubanmovie.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetWorkUtils {
	public static ConnectivityManager connectivityManager;

	public static boolean isConnectionToInternet(Context context) {
		connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobileInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (!wifiInfo.isConnected() && !mobileInfo.isConnected()) {
			Toast.makeText(context, "当前网络未连接", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}

	}
}

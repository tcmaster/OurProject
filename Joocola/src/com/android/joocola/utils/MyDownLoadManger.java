package com.android.joocola.utils;

import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;

public class MyDownLoadManger {

	private static final String url = "Bus.VersionController.GetLastVersion.ashx";
	private Handler handler;
	private Activity mActivity;
	private DownloadManager downloadManager;
	private Dialog noticeDialog;
	// 提示语
	private String updateMsg = "有最新的软件包哦，快下载吧";

	public MyDownLoadManger(Activity context, Handler mHandler, DownloadManager mDownloadManager) {
		mActivity = context;
		handler = mHandler;
		downloadManager = mDownloadManager;
	}

	private void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mActivity);
		builder.setTitle("软件版本更新");
		builder.setMessage(updateMsg);
		builder.setPositiveButton("下载", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				handler.post(new Runnable() {

					@Override
					public void run() {
						doDownLoad("http://a.utryi.com/Download");
					}
				});
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 更新APK
	 * 
	 * @param versionInfo
	 *            最新版本信息
	 * @return 是否更新成功
	 */
	public boolean update() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("platform", 1 + "");
		httpPostInterface.getData(url, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String version = jsonObject.getString("Item1");
					final String downloadUrl = Constants.URL + jsonObject.getString("Item2");
					if (version.equals(Constants.version)) {
						handler.sendEmptyMessage(4);
					} else {
						handler.post(new Runnable() {

							@Override
							public void run() {
								showNoticeDialog();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});

		return false;
	}

	private void doDownLoad(String url) {
		Log.e("bb", url);
		Uri uri = Uri.parse(url);
		DownloadManager.Request request = new Request(uri);
		request.setTitle("Joocola");// 设置名字
		request.setAllowedNetworkTypes(Request.NETWORK_WIFI);// 设置wifi下载
		request.setDestinationInExternalFilesDir(mActivity, Environment.DIRECTORY_DOWNLOADS, "joocola.apk");
		long reference = downloadManager.enqueue(request);

		try {
			downloadManager.openDownloadedFile(reference);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		;
	}
}

package com.android.joocola.utils;

import android.app.Activity;
import android.app.ProgressDialog;

public class ShowLoadingDialog {

	/**
	 * 显示进度框
	 */
	public static ProgressDialog showProgressDialog(Activity mActivity, String title) {

		ProgressDialog progDialog = new ProgressDialog(mActivity);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage(title);
		progDialog.show();
		return progDialog;
	}
}

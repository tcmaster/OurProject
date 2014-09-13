package com.android.joocola.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;

public class ShowLoadingDialog {

	public static CustomerDialog showCustomerDialog(Activity mActivity) {
		CustomerDialog mCustomerDialog = new CustomerDialog(mActivity,
				R.layout.loading);
		mCustomerDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.CENTER);
				window.setLayout(100, 100);
				TextView textView = (TextView) dlg
						.findViewById(R.id.loading_msg_id);
				textView.setText("加载中");
			}
		});
		mCustomerDialog.showDlg();
		return mCustomerDialog;

	}
}

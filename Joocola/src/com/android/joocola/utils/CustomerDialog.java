package com.android.joocola.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.view.Window;
import android.view.WindowManager;

/**
 * 自定义Dialog 可以自定义Dialog的布局，以及定义布局上的监听事件，不影响界面上键盘的弹出
 * 
 * @author lixiaosong
 */
public class CustomerDialog {
	private Activity context;
	private int res;
	private CustomerViewInterface listener;
	private AlertDialog dlg;

	/**
	 * 
	 * @param location
	 *            dialog在屏幕上显示的位置（x,y)
	 * @param context
	 *            与dialog关联的上下文
	 * @param res
	 *            自定义dialog的资源id
	 */
	public CustomerDialog(Activity context, int res) {
		this.context = context;
		this.res = res;
	}

	/**
	 * 调用这个构造方法之后必须调用init方法
	 */
	public CustomerDialog() {

	}

	public void init(Activity context, int res) {
		this.context = context;
		this.res = res;
	}

	/**
	 * 在调用这个方法之前最好先调用setOnCustomerViewCreated来控制dialog自定义界面上的内容
	 */
	public void showDlg() {
		dlg = new Builder(context).create();
		dlg.setCanceledOnTouchOutside(true);
		dlg.setCancelable(true);
		dlg.show();
		Window window = dlg.getWindow();
		// 下面的清除flag主要是为了在dialog中有editText时弹出软件盘所用。
		window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		window.setContentView(res);
		if (listener != null) {
			listener.getCustomerView(window, dlg);
		}
	}

	public void dismissDlg() {
		if (dlg != null) {
			dlg.dismiss();
		}
	}

	public interface CustomerViewInterface {
		public void getCustomerView(final Window window, final AlertDialog dlg);
	}

	public void setOnCustomerViewCreated(CustomerViewInterface listener) {
		this.listener = listener;
	}
}

package com.android.joocola.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.joocola.R;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.PostDataUtils;
import com.android.joocola.utils.PostDataUtils.VolleyPostCallBack;
import com.android.joocola.utils.Utils;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class FindPasswordActivity extends BaseActivity implements
		OnClickListener {
	private String url = "Sys.UserController.ApplyForgetPWDVerifyCode.ashx"; // 找回密码需要的网址
	private String amend_url = "Sys.UserController.ResetPWD.ashx";// 修改新密码的网址
	private EditText edit_pm, edit_security, edit_new_pwsd;
	private Button get_security_code, find_done;
	private String security_code = "";
	// 验证码获取标志位
	private static final int GETCODESUCCESS = 0;
	private static final int GETCODEFAIL = 1;
	private boolean codeButtonOK = true;
	private RequestQueue queue;
	@SuppressLint("HandlerLeak")
	private Handler findHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GETCODESUCCESS:
				Utils.toast(FindPasswordActivity.this, "修改密码成功");
				break;
			case GETCODEFAIL:
				Utils.toast(FindPasswordActivity.this, "验证码获取失败，请重新获取");
				codeButtonOK = true;
				get_security_code.setEnabled(true);
				get_security_code.setText("获取验证码");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activiy_findpswd);
		queue = Volley.newRequestQueue(this);
		mActionBar.setTitle(getString(R.string.forgetpswd));
		initView();

	}

	private void initView() {
		edit_pm = (EditText) this.findViewById(R.id.edit_pm);
		edit_security = (EditText) this.findViewById(R.id.edit_security);
		edit_new_pwsd = (EditText) this.findViewById(R.id.edit_new_pwsd);
		get_security_code = (Button) this.findViewById(R.id.get_security_code);
		find_done = (Button) this.findViewById(R.id.find_done);
		find_done.setOnClickListener(this);
		get_security_code.setOnClickListener(this);
	}

	@SuppressLint("HandlerLeak")
	private void waitCodeReceive() {
		get_security_code.setEnabled(false);
		get_security_code.setText("120秒后重新获取");
		codeButtonOK = false;
		final Handler waitHandler = new Handler() {
			int count = 120;

			@Override
			public void handleMessage(Message msg) {
				count--;
				get_security_code.setText(count + "秒后重新获取");
				if (count == 0 || codeButtonOK) {
					get_security_code.setText("获取验证码");
					get_security_code.setEnabled(true);
				}
			};
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				int i = 119;
				while (i >= 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					waitHandler.sendEmptyMessage(1);
					if (codeButtonOK) {
						break;
					}
					i--;
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_security_code:
			String input = edit_pm.getText().toString();
			if (edit_pm != null && Utils.judgeAccount(input)
					&& !Utils.stringIsNullOrEmpty(input)) {
				// do请求
				Log.e("test", input);
				waitCodeReceive();
				PostDataUtils postDataUtils = new PostDataUtils();
				postDataUtils.addParams("userName", input);
				postDataUtils.postNewRequest(url, queue,
						new VolleyPostCallBack() {

							@Override
							public void VolleyPostResolveData(String result) {
								Log.e("result", result);
								try {
									JSONObject jsonObject = new JSONObject(
											result);
									if (jsonObject.getBoolean("Item1")) {
										security_code = jsonObject
												.getString("Item2");
										findHandler
												.sendEmptyMessage(GETCODESUCCESS);
									} else {
										findHandler
												.sendEmptyMessage(GETCODEFAIL);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
				// HttpPostInterface httpPostInterface = new
				// HttpPostInterface();
				// httpPostInterface.addParams("userName", input);
				// httpPostInterface.getData(url, new HttpPostCallBack() {
				//
				// @Override
				// public void httpPostResolveData(String result) {
				// Log.e("result", result);
				// try {
				// JSONObject jsonObject = new JSONObject(result);
				// if (jsonObject.getBoolean("Item1")) {
				// security_code = jsonObject.getString("Item2");
				// findHandler.sendEmptyMessage(GETCODESUCCESS);
				// } else {
				// findHandler.sendEmptyMessage(GETCODEFAIL);
				// }
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }
				// }
				// });
			} else {
				Toast.makeText(FindPasswordActivity.this,
						getString(R.string.input_right), Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.find_done:
			Log.e("修改密码", "点击修改密码按钮");
			String input_name = edit_pm.getText().toString();
			String input_security_code = edit_security.getText().toString();// 用户输入的验证码
			String newPswd = edit_new_pwsd.getText().toString();// 用户输入的新密码
			if (input_security_code.length() != 6) {

				Utils.toast(FindPasswordActivity.this, "请输入正确的验证码");
				break;
			}
			Log.e("点击修改密码之后的保存下来的security_code", security_code);
			Log.e("点击修改密码之后用户输入的security_code", input_security_code);
			if (input_security_code.equals(security_code)) {
				Log.e("修改密码", "开始发送修改密码的请求");
				HttpPostInterface httpPostInterface = new HttpPostInterface();
				httpPostInterface.addParams("userName", input_name);
				httpPostInterface.addParams("newPWD", newPswd);
				httpPostInterface.addParams("verifyCode", input_security_code);
				httpPostInterface.getData(amend_url, new HttpPostCallBack() {

					@Override
					public void httpPostResolveData(String result) {
						Log.e("修改密码", result);
					}
				});
			}
			break;
		default:
			break;
		}
	}
}
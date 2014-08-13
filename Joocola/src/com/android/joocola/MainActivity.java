package com.android.joocola;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.joocola.activity.FindPasswordActivity;
import com.android.joocola.activity.RegisterOneActivity;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.chat.XMPPChat;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;

public class MainActivity extends Activity implements OnClickListener {

	protected String url_b = "Sys.UserController.AppLogon.ashx";
	private EditText nameEdit, pswdEdit;
	private Button loginButton, registerButton;
	private TextView forget_pswd;
	private static final int LOGIN_SUCCESS = 0; // 登录成功
	private static final int LOGIN_ERROR = 1; // 登录失败
	private int mBackKeyPressedTimes = 0;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private RelativeLayout loading;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOGIN_ERROR:
				hideLoading();
				Utils.toast(MainActivity.this, getString(R.string.loginerror));
				break;
			case LOGIN_SUCCESS:
				// 登录成功的操作
				hideLoading();
				String pid = (String) msg.obj;
				JoocolaApplication.getInstance().initUserInfoAfterLogin(pid);
				editor.putString(Constans.LOGIN_PID, pid);
				editor.commit();
				Intent intent = new Intent(MainActivity.this,
						MainTabActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		sharedPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	private void initView() {
		nameEdit = (EditText) this.findViewById(R.id.name_edit);
		pswdEdit = (EditText) this.findViewById(R.id.pswd_edit);
		loginButton = (Button) this.findViewById(R.id.login);
		forget_pswd = (TextView) this.findViewById(R.id.forget_pswd);
		registerButton = (Button) this.findViewById(R.id.register);
		loading = (RelativeLayout) this.findViewById(R.id.loading);
		registerButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		forget_pswd.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			Log.e("Login", "点击登录");
			Utils.hideSoftInputMode(MainActivity.this, v);
			String name = nameEdit.getText().toString();
			String pswd = pswdEdit.getText().toString();
			if (name.length() == 0) {
				Toast.makeText(MainActivity.this,
						getString(R.string.name_hint), Toast.LENGTH_SHORT)
						.show();
				break;
			}
			if (pswd.length() == 0) {
				Toast.makeText(MainActivity.this,
						getString(R.string.input_pswd), Toast.LENGTH_SHORT)
						.show();
				break;
			}

			if (Utils.judgeAccount(nameEdit.getText().toString())) {
				showLoading();
				HttpPostInterface mHttpPostInterface = new HttpPostInterface();
				mHttpPostInterface.addParams("userName", name);
				mHttpPostInterface.addParams("pwd", pswd);
				mHttpPostInterface.getData(url_b, new HttpPostCallBack() {

					@Override
					public void httpPostResolveData(String result) {
						// 在这里用handler 把json 发出去 进行更新UI的操作

						if (result.equals("0")) {
							Message error = Message.obtain();
							error.what = LOGIN_ERROR;
							error.obj = result;
							mHandler.sendMessage(error);
						} else {
							Message success = Message.obtain();
							success.what = LOGIN_SUCCESS;
							success.obj = result;
							mHandler.sendMessage(success);
						}
					}
				});
			} else {
				Toast.makeText(MainActivity.this,
						getString(R.string.input_right), Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.forget_pswd:
			Intent forgetIntent = new Intent(MainActivity.this,
					FindPasswordActivity.class);
			startActivity(forgetIntent);
			break;
		case R.id.register:
			Intent registerIntent = new Intent(MainActivity.this,
					RegisterOneActivity.class);
			startActivity(registerIntent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (isShow()) {
			hideLoading();
			return;
		}
		if (mBackKeyPressedTimes == 0) {
			mBackKeyPressedTimes = 1;
			Toast.makeText(this, "Press back button again for exit.",
					Toast.LENGTH_SHORT).show();
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						mBackKeyPressedTimes = 0;
					}
				}
			}.start();
			return;
		} else {
			/**
			 * 关闭心跳连接
			 */
			XMPPChat.getInstance().stopHeartService(
					JoocolaApplication.getInstance());
			/**
			 * 关闭聊天服务
			 */
			XMPPChat.getInstance().closeConnection();
			this.finish();
			System.exit(0);
		}
		super.onBackPressed();
	}

	private void showLoading() {
		if (loading != null) {
			loading.setVisibility(View.VISIBLE);
		}
	}

	private void hideLoading() {
		if (loading != null) {
			loading.setVisibility(View.GONE);
		}
	}

	private boolean isShow() {
		if (loading != null && loading.getVisibility() == View.VISIBLE) {
			return true;
		} else {
			return false;
		}
	}
}

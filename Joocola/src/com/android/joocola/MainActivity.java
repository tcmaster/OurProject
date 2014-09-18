package com.android.joocola;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
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
	private ProgressDialog progDialog = null;// 搜索时进度条
	private AlertDialog mConnectionDialog;// 网络的对话框。
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOGIN_ERROR:
				Utils.toast(MainActivity.this, getString(R.string.loginerror));
				if (progDialog != null) {
					progDialog.dismiss();
				}
				break;
			case LOGIN_SUCCESS:
				// 登录成功的操作
				if (progDialog != null) {
					progDialog.dismiss();
				}
				String pid = (String) msg.obj;
				editor.putString(Constans.LOGIN_PID, pid);
				editor.putString(Constans.LOGIN_ACCOUNT, nameEdit.getText().toString());
				editor.putBoolean(Constans.LOGIN_AUTOMATIC, true);
				editor.commit();
				Intent intent = new Intent(MainActivity.this, MainTabActivity.class);
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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		sharedPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
		mConnectionDialog = Utils.getNetAlertDialog(MainActivity.this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		nameEdit = (EditText) this.findViewById(R.id.name_edit);
		pswdEdit = (EditText) this.findViewById(R.id.pswd_edit);
		loginButton = (Button) this.findViewById(R.id.login);
		forget_pswd = (TextView) this.findViewById(R.id.forget_pswd);
		registerButton = (Button) this.findViewById(R.id.register);
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
			if (Utils.isNetConn(this)) {
				if (mConnectionDialog != null && mConnectionDialog.isShowing()) {
					mConnectionDialog.dismiss();
				}
			} else {
				if (!mConnectionDialog.isShowing()) {
					mConnectionDialog.show();
					return;
				}
			}
			Utils.hideSoftInputMode(MainActivity.this, v);
			String name = nameEdit.getText().toString();
			String pswd = pswdEdit.getText().toString();
			if (name.length() == 0) {
				Toast.makeText(MainActivity.this, getString(R.string.name_hint), Toast.LENGTH_SHORT).show();
				break;
			}
			if (pswd.length() == 0) {
				Toast.makeText(MainActivity.this, getString(R.string.input_pswd), Toast.LENGTH_SHORT).show();
				break;
			}

			if (Utils.judgeAccount(nameEdit.getText().toString())) {

				HttpPostInterface mHttpPostInterface = new HttpPostInterface();
				showProgressDialog("登录中");
				mHttpPostInterface.addParams("userName", name);
				mHttpPostInterface.addParams("pwd", pswd);
				mHttpPostInterface.addParams("version", Constans.version);
				mHttpPostInterface.getData(url_b, new HttpPostCallBack() {

					@Override
					public void httpPostResolveData(final String result) {
						// 在这里用handler 把json 发出去 进行更新UI的操作

						if (result.equals("0")) {
							Message error = Message.obtain();
							error.what = LOGIN_ERROR;
							error.obj = result;
							mHandler.sendMessage(error);
						} else {
							mHandler.post(new Runnable() {

								@Override
								public void run() {

									new AllWorkDoneTask(result).execute();
								}
							});
						}
					}
				});
			} else {
				Toast.makeText(MainActivity.this, getString(R.string.input_right), Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.forget_pswd:
			Intent forgetIntent = new Intent(MainActivity.this, FindPasswordActivity.class);
			startActivity(forgetIntent);
			break;
		case R.id.register:
			Intent registerIntent = new Intent(MainActivity.this, RegisterOneActivity.class);
			startActivity(registerIntent);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		if (mBackKeyPressedTimes == 0) {
			mBackKeyPressedTimes = 1;
			Toast.makeText(this, "再次点击后退出程序.", Toast.LENGTH_SHORT).show();
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
			this.finish();
			// stopService(new Intent(this, DefineService.class));
			System.exit(0);
		}
		super.onBackPressed();
	}

	private class AllWorkDoneTask extends AsyncTask<Void, Void, Integer> {

		private String userPid;

		public AllWorkDoneTask(String result) {
			userPid = result;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			JoocolaApplication.getInstance().initUserInfoAfterLogin(userPid);
			// while (XMPPChat.getInstance().flag1 == 0)
			// ;
			// if (XMPPChat.getInstance().flag1 != 1
			// || XMPPChat.getInstance().flag1 != 2) {
			// // 注册失败
			// }
			while (XMPPChat.getInstance().flag2 == 0)
				;
			if (XMPPChat.getInstance().flag2 != 1) {
				// 连接失败
				return 2;
			}
			while (XMPPChat.getInstance().flag3 == 0)
				;
			if (XMPPChat.getInstance().flag3 != 1) {
				// 登录失败
				return 3;
			}
			while (XMPPChat.getInstance().flag4 == 0)
				;
			if (XMPPChat.getInstance().flag4 != 1) {
				// 设置在线状态失败
				return 4;
			}
			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 1:
			case 2:
			case 3:
			case 4:
				Utils.toast(MainActivity.this, "数据获取异常，请重新尝试登陆");
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						finish();
						System.exit(0);
					}
				}).start();

				break;
			case -1:
				Message success = Message.obtain();
				success.what = LOGIN_SUCCESS;
				success.obj = userPid;
				mHandler.sendMessage(success);
				break;
			default:
				Utils.toast(MainActivity.this, "到了不可能的地方啊 ");
				break;
			}

		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (intent.getBooleanExtra("isFromRegister", false)) {
			nameEdit.setText(intent.getStringExtra("userName"));
			pswdEdit.setText(intent.getStringExtra("password"));
			if (Utils.isNetConn(this)) {
				if (mConnectionDialog != null && mConnectionDialog.isShowing()) {
					mConnectionDialog.dismiss();
				}
			} else {
				if (!mConnectionDialog.isShowing()) {
					mConnectionDialog.show();
					return;
				}
			}
			String name = nameEdit.getText().toString();
			String pswd = pswdEdit.getText().toString();
			if (name.length() == 0) {
				Toast.makeText(MainActivity.this, getString(R.string.name_hint), Toast.LENGTH_SHORT).show();
				return;
			}
			if (pswd.length() == 0) {
				Toast.makeText(MainActivity.this, getString(R.string.input_pswd), Toast.LENGTH_SHORT).show();
				return;
			}

			if (Utils.judgeAccount(nameEdit.getText().toString())) {

				HttpPostInterface mHttpPostInterface = new HttpPostInterface();
				showProgressDialog("登录中");
				mHttpPostInterface.addParams("userName", name);
				mHttpPostInterface.addParams("pwd", pswd);
				mHttpPostInterface.addParams("version", Constans.version);
				mHttpPostInterface.getData(url_b, new HttpPostCallBack() {

					@Override
					public void httpPostResolveData(final String result) {
						// 在这里用handler 把json 发出去 进行更新UI的操作

						if (result.equals("0")) {
							Message error = Message.obtain();
							error.what = LOGIN_ERROR;
							error.obj = result;
							mHandler.sendMessage(error);
						} else {
							mHandler.post(new Runnable() {

								@Override
								public void run() {
									new AllWorkDoneTask(result).execute();
								}
							});
						}
					}
				});
			}
			super.onNewIntent(intent);
		}
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog(String title) {
		if (progDialog == null) {
			progDialog = new ProgressDialog(this);
		}
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage(title);
		progDialog.show();
	}

}

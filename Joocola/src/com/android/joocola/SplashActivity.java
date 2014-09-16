package com.android.joocola;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.chat.XMPPChat;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;

/**
 * 闪屏界面
 * 
 * @author bb
 * 
 */
public class SplashActivity extends Activity {

	private SharedPreferences isFirstPreferences;
	private Editor mEditor;
	public static final String IS_FIRST = "isfirst";
	private static Handler handler = new Handler();
	private SharedPreferences loginPreferences;
	private boolean isAutoMatic;
	private String user_pid;
	private String userName;
	private static final String AutoUrl = "Sys.UserController.AppAutoLogon.ashx";
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		isFirstPreferences = getSharedPreferences("IsFirstLogin", Context.MODE_PRIVATE);
		loginPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		editor = loginPreferences.edit();
		isAutoMatic = loginPreferences.getBoolean(Constans.LOGIN_AUTOMATIC, false);
		Log.e("bb", isAutoMatic + "");
		if (isAutoMatic) {
			user_pid = loginPreferences.getString(Constans.LOGIN_PID, "");
			userName = loginPreferences.getString(Constans.LOGIN_ACCOUNT, "");
		}
		mEditor = isFirstPreferences.edit();
		Log.e("bb", isFirst() + "");
		if (isFirst()) {
			mEditor.putBoolean(IS_FIRST, false);
			mEditor.commit();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}
			}, 1000);

		} else {
			/**
			 * 可以进行自动登录
			 */
			if (isAutoMatic) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						if (TextUtils.isEmpty(user_pid)) {
							gotoMainActivity();
							return;
						}
						if (TextUtils.isEmpty(userName)) {
							gotoMainActivity();
							return;
						}

						Log.e("bb", "自动登录user_pid" + user_pid);
						Log.e("bb", "自动登录时userName" + userName);
						HttpPostInterface httpPostInterface = new HttpPostInterface();
						httpPostInterface.addParams("userID", user_pid);
						httpPostInterface.addParams("userName", userName);
						httpPostInterface.addParams("version", Constans.version);
						httpPostInterface.getData(AutoUrl, new HttpPostCallBack() {

							@Override
							public void httpPostResolveData(final String result) {
								handler.post(new Runnable() {

									@Override
									public void run() {
										if (!result.equals("0")) {
											editor.putString(Constans.LOGIN_PID, result);
											editor.putString(Constans.LOGIN_ACCOUNT, userName);
											editor.putBoolean(Constans.LOGIN_AUTOMATIC, true);
											editor.commit();
											// TODO Auto-generated
											// method stub
											// 需要进行聊天的注册。我不是太知道你那边逻辑怎么搞的。。
											new AllWorkDoneTask(result).execute();

										} else {
											Utils.toast(SplashActivity.this, "自动登录失败");
											editor.putBoolean(Constans.LOGIN_AUTOMATIC, false);
											editor.commit();
											gotoMainActivity();
										}
									}
								});
							}
						});
					}
				});
			} else {
				/**
				 * 不能进行自动登录 进入正常登录页
				 */
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						Log.e("bb", "不能进行自动登录 进入正常登录页");
						gotoMainActivity();
					}
				}, 1000);
			}
		}

	}

	private boolean isFirst() {
		boolean isfirst = isFirstPreferences.getBoolean(IS_FIRST, true);
		return isfirst;
	}

	/**
	 * 进登录页
	 */
	private void gotoMainActivity() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		SplashActivity.this.finish();
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
				Utils.toast(SplashActivity.this, "数据获取异常，请重新尝试登陆");
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
				// 这里说明可以登录了
				Intent intent = new Intent(SplashActivity.this, MainTabActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
				break;
			default:
				Utils.toast(SplashActivity.this, "到了不可能的地方啊 ");
				break;
			}

		}

	}
}

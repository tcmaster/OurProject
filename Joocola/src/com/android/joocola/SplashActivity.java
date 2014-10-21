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
import com.android.joocola.chat.EaseMobChat;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;

/**
 * 闪屏界面 每次登录首先启动该界面
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
	private Editor editor;
	private int versioncodeByPackage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		isFirstPreferences = getSharedPreferences("IsFirstLogin", Context.MODE_PRIVATE);
		loginPreferences = getSharedPreferences(Constants.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		editor = loginPreferences.edit();
		isAutoMatic = loginPreferences.getBoolean(Constants.LOGIN_AUTOMATIC, false);
		if (isAutoMatic) {
			user_pid = loginPreferences.getString(Constants.LOGIN_PID, "");
			userName = loginPreferences.getString(Constants.LOGIN_ACCOUNT, "");
		}
		mEditor = isFirstPreferences.edit();
		Log.e("bb", isFirst() + "");
		if (isFirst()) {
			mEditor.putInt(IS_FIRST, versioncodeByPackage);
			mEditor.commit();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
					startActivity(intent);
					SplashActivity.this.finish();
				}
			}, 2000);

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

						HttpPostInterface httpPostInterface = new HttpPostInterface();
						httpPostInterface.addParams("userID", user_pid);
						httpPostInterface.addParams("userName", userName);
						httpPostInterface.addParams("version", Constants.version);
						httpPostInterface.getData(Constants.AUTO_LOGIN_URL, new HttpPostCallBack() {

							@Override
							public void httpPostResolveData(final String result) {
								handler.post(new Runnable() {

									@Override
									public void run() {
										if (!result.equals("0")) {
											editor.putString(Constants.LOGIN_PID, result);
											editor.putString(Constants.LOGIN_ACCOUNT, userName);
											editor.putBoolean(Constants.LOGIN_AUTOMATIC, true);
											editor.commit();
											// TODO Auto-generated
											// method stub
											// 需要进行聊天的注册。我不是太知道你那边逻辑怎么搞的。。
											new AllWorkDoneTask(result).execute();

										} else {
											Utils.toast(SplashActivity.this, "自动登录失败");
											editor.putBoolean(Constants.LOGIN_AUTOMATIC, false);
											editor.commit();
											gotoMainActivity();
										}
									}
								});
							}

							@Override
							public void onNetWorkError() {
								// TODO Auto-generated method stub

							}
						});
					}
				});
			} else {
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						gotoMainActivity();

					}
				}, 2000);
			}
		}

	}

	private boolean isFirst() {
		int versioncodeByshare = isFirstPreferences.getInt(IS_FIRST, 0);
		versioncodeByPackage = Utils.getVersionCode(SplashActivity.this);
		if (versioncodeByPackage != versioncodeByshare)
			return true;
		else {
			return false;
		}
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
			while (EaseMobChat.getInstance().getFlag() == 0)
				;
			return EaseMobChat.getInstance().getFlag();
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

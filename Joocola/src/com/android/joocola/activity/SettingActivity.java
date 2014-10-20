package com.android.joocola.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.MyDownLoadManger;
import com.android.joocola.utils.Utils;

public class SettingActivity extends BaseActivity implements OnClickListener {

	private LinearLayout pswdLayout, mailLayout, phonenumLayout, updateLayout;
	private final static int PSWD_NUM = 1;// 修改密码后的值
	private final static int MAIL_NUM = 2;// 修改邮箱后的值
	private final static int PHONE_NUM = 3;// 修改手机号的值
	private CustomerDialog pswddlg, maildlg, phonedlg;
	private SharedPreferences sharedPreferences;
	private String user_pid;// 当前操作用户的id;

	private DownLoadReciver downLoadReciver;
	private DownloadManager downloadManager;
	private TextView versionTextView;
	private TextView phoneTextView;
	private TextView mailTextView;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {

			case PSWD_NUM:
				String pswdResult = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(pswdResult);
					boolean isTrue = jsonObject.getBoolean("Item1");
					String result = jsonObject.getString("Item2");
					if (isTrue) {
						Utils.toast(SettingActivity.this, "修改密码成功");
					} else {
						Utils.toast(SettingActivity.this, result);
					}
					if (pswddlg != null)
						pswddlg.dismissDlg();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case MAIL_NUM:
				String mailResult = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(mailResult);
					boolean isTrue = jsonObject.getBoolean("Item1");
					String result = jsonObject.getString("Item2");
					if (isTrue) {
						Utils.toast(SettingActivity.this, "修改邮箱成功");
					} else {
						Utils.toast(SettingActivity.this, result);
					}
					if (maildlg != null)
						maildlg.dismissDlg();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case PHONE_NUM:
				String phoneResult = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(phoneResult);
					boolean isTrue = jsonObject.getBoolean("Item1");
					String result = jsonObject.getString("Item2");
					if (isTrue) {
						Utils.toast(SettingActivity.this, "修改手机号成功");
					} else {
						Utils.toast(SettingActivity.this, result);
					}
					if (phonedlg != null)
						phonedlg.dismissDlg();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				Utils.toast(SettingActivity.this, "已经是最新版本");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sharedPreferences = getSharedPreferences(Constants.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		user_pid = sharedPreferences.getString(Constants.LOGIN_PID, "");
		initView();
		initActionbar();
		IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		downLoadReciver = new DownLoadReciver();
		registerReceiver(downLoadReciver, intentFilter);
		String serviceString = DOWNLOAD_SERVICE;
		downloadManager = (DownloadManager) getSystemService(serviceString);
	}

	private void initView() {
		pswdLayout = (LinearLayout) this.findViewById(R.id.pswdlayout);
		mailLayout = (LinearLayout) this.findViewById(R.id.mailLayout);
		phonenumLayout = (LinearLayout) this.findViewById(R.id.phonenumlayout);
		updateLayout = (LinearLayout) this.findViewById(R.id.updatelayout);
		pswdLayout.setOnClickListener(this);
		mailLayout.setOnClickListener(this);
		phonenumLayout.setOnClickListener(this);
		updateLayout.setOnClickListener(this);
		versionTextView = (TextView) this.findViewById(R.id.version_text);
		versionTextView.setText(Constants.version);
		phoneTextView = (TextView) this.findViewById(R.id.phone_num_text);
		mailTextView = (TextView) this.findViewById(R.id.mail_text);
		JoocolaApplication joocolaApplication = JoocolaApplication.getInstance();
		UserInfo userInfo = joocolaApplication.getUserInfo();
		if (userInfo.getPhone() != null && !TextUtils.isEmpty(userInfo.getPhone()))
			phoneTextView.setText(userInfo.getPhone());
		if (userInfo.getEmail() != null && !TextUtils.isEmpty(userInfo.getEmail()))
			mailTextView.setText(userInfo.getEmail());

	}

	/**
	 * 加载Actionbar
	 */
	private void initActionbar() {
		useCustomerActionBar();
		getActionBarleft().setText("设置");
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
		mActionBar.setBackgroundDrawable(new ColorDrawable(R.color.transparent));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (downLoadReciver != null) {
			unregisterReceiver(downLoadReciver);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pswdlayout:
			showAlterPswdDlg();
			break;
		case R.id.mailLayout:
			showAlterMailDlg();
			break;

		case R.id.phonenumlayout:
			showAlterPhoneNumDlg();
			break;
		case R.id.updatelayout:
			MyDownLoadManger downLoadManger = new MyDownLoadManger(SettingActivity.this, handler, downloadManager);
			downLoadManger.update();
			break;

		default:
			break;
		}
	}

	/**
	 * 弹出修改密码的对话框
	 */
	private void showAlterPswdDlg() {
		pswddlg = new CustomerDialog(this, R.layout.dlg_alterpswd);
		pswddlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.CENTER);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
				TextView ok = (TextView) dlg.findViewById(R.id.dlg_pe_ok);
				TextView cancle = (TextView) dlg.findViewById(R.id.dlg_pe_cancel);
				final EditText old_pswd = (EditText) dlg.findViewById(R.id.old_pswd);
				final EditText new_pswd = (EditText) dlg.findViewById(R.id.new_pswd);
				final EditText new_pswd_again = (EditText) dlg.findViewById(R.id.new_pswd_again);
				cancle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						pswddlg.dismissDlg();

					}
				});
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String old = old_pswd.getText().toString();
						String newtext = new_pswd.getText().toString();
						String newtextAgain = new_pswd_again.getText().toString();
						if (TextUtils.isEmpty(old)) {
							Utils.toast(SettingActivity.this, "请输入旧密码");
							return;
						}
						if (TextUtils.isEmpty(newtext)) {
							Utils.toast(SettingActivity.this, "请输入新密码");
							return;
						}
						if (TextUtils.isEmpty(newtextAgain)) {
							Utils.toast(SettingActivity.this, "请再次输入新密码");
							return;
						}
						if (!newtext.equals(newtextAgain)) {
							Utils.toast(SettingActivity.this, "两次输入的密码不相同");
							return;
						}
						HttpPostInterface httpPostInterface = new HttpPostInterface();
						httpPostInterface.addParams("userID", user_pid);
						httpPostInterface.addParams("oldPWD", old);
						httpPostInterface.addParams("newPWD", newtext);
						httpPostInterface.getData(Constants.SET_NEWPSWD_STRING, new HttpPostCallBack() {

							@Override
							public void httpPostResolveData(String result) {
								Message message = Message.obtain();
								message.obj = result;
								message.what = PSWD_NUM;
								handler.sendMessage(message);
							}

							@Override
							public void onNetWorkError() {
								// TODO Auto-generated method stub

							}
						});
					}
				});
			}

		});

		pswddlg.showDlg();
	}

	/**
	 * 弹出修改手机号的对话框
	 */
	private void showAlterPhoneNumDlg() {
		phonedlg = new CustomerDialog(this, R.layout.dlg_alterphonenum);
		phonedlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.CENTER);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
				TextView ok = (TextView) dlg.findViewById(R.id.dlg_pe_ok);
				TextView cancle = (TextView) dlg.findViewById(R.id.dlg_pe_cancel);
				final EditText editText = (EditText) dlg.findViewById(R.id.alter_phonenum_edit);
				cancle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						phonedlg.dismissDlg();

					}
				});
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String newNum = editText.getText().toString();
						if (TextUtils.isEmpty(newNum)) {
							Utils.toast(SettingActivity.this, "请输入新手机号");
							return;
						}
						HttpPostInterface httpPostInterface = new HttpPostInterface();
						httpPostInterface.addParams("userID", user_pid);
						httpPostInterface.addParams("phone", newNum);
						httpPostInterface.getData(Constants.SET_PHONE_URL, new HttpPostCallBack() {

							@Override
							public void httpPostResolveData(String result) {
								Message message = Message.obtain();
								message.obj = result;
								message.what = PHONE_NUM;
								handler.sendMessage(message);
							}

							@Override
							public void onNetWorkError() {
								// TODO Auto-generated method stub

							}
						});
					}
				});
			}
		});
		phonedlg.showDlg();
	}

	/**
	 * 弹出修改邮箱的对话框
	 */
	private void showAlterMailDlg() {
		maildlg = new CustomerDialog(this, R.layout.dlg_altermail);
		maildlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.CENTER);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
				TextView ok = (TextView) dlg.findViewById(R.id.dlg_pe_ok);
				TextView cancle = (TextView) dlg.findViewById(R.id.dlg_pe_cancel);
				final EditText editText = (EditText) dlg.findViewById(R.id.alter_mail_edit);
				cancle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						maildlg.dismissDlg();

					}
				});
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String newEmail = editText.getText().toString();
						Log.e("bb", newEmail);
						if (TextUtils.isEmpty(newEmail)) {
							Utils.toast(SettingActivity.this, "请输入新邮箱");
							return;
						}
						HttpPostInterface httpPostInterface = new HttpPostInterface();
						httpPostInterface.addParams("userID", user_pid);
						httpPostInterface.addParams("email", newEmail);
						httpPostInterface.getData(Constants.SET_EMAIL_URL, new HttpPostCallBack() {

							@Override
							public void httpPostResolveData(String result) {
								Message message = Message.obtain();
								message.obj = result;
								message.what = MAIL_NUM;
								handler.sendMessage(message);
							}

							@Override
							public void onNetWorkError() {
								// TODO Auto-generated method stub

							}
						});
					}
				});
			}
		});
		maildlg.showDlg();
	}

	class DownLoadReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			long refernce = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

			Query myDownloadQuery = new Query();
			myDownloadQuery.setFilterById(refernce);

			Cursor myDownload = downloadManager.query(myDownloadQuery);
			if (myDownload.moveToFirst()) {
				int fileNameIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
				int fileUriIdx = myDownload.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

				String fileName = myDownload.getString(fileNameIdx);
				String fileUri = myDownload.getString(fileUriIdx);
				if (fileName != null && !TextUtils.isEmpty(fileName)) {
					installApk(fileName);
				}
			}
			myDownload.close();
		}

	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk(String file) {
		File apkfile = new File(file);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
		startActivity(i);
	}

}

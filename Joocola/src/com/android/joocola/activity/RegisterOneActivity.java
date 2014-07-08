package com.android.joocola.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.RegisterInfo;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;

public class RegisterOneActivity extends BaseActivity implements
		OnClickListener {
	// 获取验证码，下一步按钮,点击验证按钮
	private Button b_getAutoCode, b_nextStep, b_verify;
	// 用户名，验证码，密码，介绍人
	private EditText et_userName, et_code, et_password, et_introducer;
	// 返回登陆界面
	private TextView tv_backLogin;
	// 确认当前得到的验证码是正确
	private boolean autoCode_Flag = true;
	private static final String URLAUTOCODEURL = "Sys.UserController.ApplyRegVerifyCode.ashx";
	// 获取到的验证码
	private String resultCode;
	private MyHandler handler;
	// 验证码获取标志位
	private static final int GETCODESUCCESS = 0;
	private static final int GETCODEFAIL = 1;
	// 按钮是否可用标识
	private boolean buttonOk = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interface_register1);
		handler = new MyHandler();
		initView();
		registerListener();
	}

	private void initView() {
		b_getAutoCode = (Button) findViewById(R.id.getAutoCode);
		b_nextStep = (Button) findViewById(R.id.nextStep);
		et_userName = (EditText) findViewById(R.id.usernameText);
		et_code = (EditText) findViewById(R.id.authcode_text);
		et_password = (EditText) findViewById(R.id.password_text);
		et_introducer = (EditText) findViewById(R.id.introducerText);
		tv_backLogin = (TextView) findViewById(R.id.backLogin);
	}

	private void registerListener() {
		b_getAutoCode.setOnClickListener(this);
		b_nextStep.setOnClickListener(this);
		tv_backLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getAutoCode:
			// 获取验证码
			getAutoCode();
			break;
		case R.id.nextStep:
			// 下一步
			nextStep();
			break;
		case R.id.backLogin:
			// 返回登录
			backLogin();
		case R.id.verifyButton:
			// 验证
			verify();
			break;
		default:
			break;
		}
	}

	private void waitCodeReceive() {
		b_getAutoCode.setEnabled(false);
		b_getAutoCode.setText("60");
		buttonOk = false;
		final Handler waitHandler = new Handler() {
			int count = 60;

			@Override
			public void handleMessage(Message msg) {
				count--;
				b_getAutoCode.setText(count + "");
				if (count == 0 || buttonOk) {
					b_getAutoCode.setText("获取验证码");
					b_getAutoCode.setEnabled(true);
				}
			};
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				int i = 59;
				while (i >= 0) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					waitHandler.sendEmptyMessage(1);
					if (buttonOk) {
						break;
					}
					i--;
				}
			}
		}).start();
	}

	private void getAutoCode() {
		// 网络获取验证码
		HttpPostInterface task = new HttpPostInterface();
		if (Utils.judgeAccount(et_userName.getText().toString())) {
			waitCodeReceive();
			task.addParams("applyKey", et_userName.getText().toString());
			task.getData(URLAUTOCODEURL, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					try {
						JSONObject jObject = new JSONObject(result);
						String item1 = jObject.getString("Item1");
						if (item1.equals("true")) {
							resultCode = jObject.getString("Item2");
							handler.sendEmptyMessage(GETCODESUCCESS);
						} else
							handler.sendEmptyMessage(GETCODEFAIL);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} else
			Utils.toast(this, "用户名格式不正确，请使用邮箱/电话号码作为用户名");
	}

	private void nextStep() {
		// 判断是否符合电话号码和邮箱的格式
		if (Utils.judgeAccount(et_userName.getText().toString())) {
			// 判断验证码是否正确
			if (autoCode_Flag) {
				// 判断密码是否符合要求
				if (Utils.isGoodPassword(et_password.getText().toString())) {
					// 介绍人（手机号，邮箱或空串)
					if (Utils.stringIsNullOrEmpty(et_introducer.getText()
							.toString())
							|| Utils.judgeAccount(et_introducer.getText()
									.toString())) {
						Intent intent = new Intent(this,
								RegisterTwoActivity.class);
						RegisterInfo info = new RegisterInfo();
						info.setUserName(et_userName.getText().toString());
						info.setPassword(et_password.getText().toString());
						info.setAutoCode(et_code.getText().toString());
						info.setIntroducer(et_introducer.getText().toString());
						intent.putExtra("info", info);
						startActivity(intent);
						finish();
					} else
						Utils.toast(this, "介绍人输入有误，请重新输入");
				} else
					Utils.toast(this, "密码格式有误，请重新输入密码(6-20位数字或字母");
			} else
				Utils.toast(this, "验证码有误，请重新获取");
		} else
			Utils.toast(this, "用户名格式不正确，请使用邮箱/电话号码作为用户名");

	}

	private void backLogin() {

	}

	private void verify() {
		if (resultCode != null) {
			if (resultCode.equals(et_code.getText().toString())) {
				b_verify.setEnabled(false);
				b_verify.setText("验证成功");
			} else {
				Utils.toast(this, "验证失败，请重新输入验证码");
			}
		} else {
			Utils.toast(this, "网络状况不佳，请稍后再试");
		}
	}

	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETCODESUCCESS:

				break;
			case GETCODEFAIL:
				Utils.toast(RegisterOneActivity.this, "验证码获取失败，请重新获取");
				buttonOk = true;
				b_getAutoCode.setEnabled(true);
				b_getAutoCode.setText("获取验证码");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
}

package com.example.joocola.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joocola.R;

public class RegisterOneActivity extends BaseActivity implements
		OnClickListener {
	// 获取验证码，下一步按钮
	private Button b_getAutoCode, b_nextStep;
	// 用户名，验证码，密码，介绍人
	private EditText et_userName, et_code, et_password, et_introducer;
	// 返回登陆界面
	private TextView tv_backLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			getAutoCode();
			break;
		case R.id.nextStep:
			nextStep();
			break;
		case R.id.backLogin:
			backLogin();
		default:
			break;
		}
	}

	private void getAutoCode() {

	}

	private void nextStep() {

	}

	private void backLogin() {

	}
}

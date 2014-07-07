package com.example.joocola.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.joocola.R;

public class RegisterOneActivity extends BaseActivity {
	// 获取验证码，下一步按钮
	Button b_getAutoCode, b_nextStep;
	// 用户名，验证码，密码，介绍人
	EditText et_userName, et_code, et_password, et_introducer;
	// 返回登陆界面
	TextView tv_backLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
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
}

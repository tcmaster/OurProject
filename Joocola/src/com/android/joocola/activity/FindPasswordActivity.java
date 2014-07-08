package com.android.joocola.activity;

import android.os.Bundle;

import com.android.joocola.R;

public class FindPasswordActivity extends BaseActivity {
	private String url = "Sys.UserController. ApplyForgetPWDVerifyCode.ashx"; // 找回密码需要的网址

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activiy_findpswd);
		mActionBar.setTitle("忘记密码");
	}

}

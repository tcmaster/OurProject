package com.android.joocola.activity;

import android.os.Bundle;

import com.example.joocola.R;

public class FindPasswordActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activiy_findpswd);
		mActionBar.setTitle("忘记密码");
	}

}

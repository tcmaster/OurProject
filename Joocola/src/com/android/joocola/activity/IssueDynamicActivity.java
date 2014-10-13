package com.android.joocola.activity;

import android.os.Bundle;
import android.view.View;

import com.android.joocola.R;

public class IssueDynamicActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_issuedynamic);
		initActionbar();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initActionbar() {
		useCustomerActionBar();
		getActionBarleft().setText("邀约动态");
		getActionBarRight().setVisibility(View.INVISIBLE);
		getActionBarTitle().setVisibility(View.INVISIBLE);
	}
}

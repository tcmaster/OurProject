package com.android.joocola.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.joocola.R;

public class IssuedinvitationActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiy_issuedinvitation);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String title = bundle.getString("title");
		initActionbar(title);
	}

	private void initActionbar(String title) {
		useCustomerActionBar();
		getActionBarleft().setText("发布邀约:" + title);
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setText("下一步");
	}
}

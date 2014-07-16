package com.android.joocola.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.joocola.R;

public class IssuedinvitationActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiy_issuedinvitation);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String title = bundle.getString("title");
		mActionBar.setTitle(title);
	}

}

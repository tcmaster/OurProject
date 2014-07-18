package com.android.joocola.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.joocola.entity.IssuedinvitationInfo;

public class IssuedinvitationBActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		IssuedinvitationInfo info = (IssuedinvitationInfo) intent
				.getSerializableExtra("info");
		Log.e("info", info.toString());
	}
}

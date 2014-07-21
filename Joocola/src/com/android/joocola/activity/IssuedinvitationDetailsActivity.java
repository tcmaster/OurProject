package com.android.joocola.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.joocola.R;

/**
 * 邀约详情界面 需要传入1个邀约的pid
 * 
 * @author bb
 * 
 */
public class IssuedinvitationDetailsActivity extends BaseActivity {
	private int issue_pid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_issuedetails);
		Intent intent = getIntent();
		issue_pid = intent.getIntExtra("issue_pid", 0);
	}
}

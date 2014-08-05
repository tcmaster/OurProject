package com.android.joocola.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.SimpleUserInfo;
import com.android.joocola.view.AutoListView;

public class ApllyMangerActivity extends BaseActivity {
	private final String applyUrl = "Sys.UserController.GetUserSimpleInfos.ashx";
	private String issue_pid;// 该邀约id.
	private String ReserveDate;// 到期时间
	private AutoListView joinListView, unJoinListView;
	private List<SimpleUserInfo> joinList;
	private List<SimpleUserInfo> unJoinList;
	private TextView reserveDateTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_applymanger);
		Intent intent = getIntent();
		issue_pid = intent.getStringExtra("issue_pid");
		ReserveDate = intent.getStringExtra("ReserveDate");
		initActionbar();
		initView();
	}

	private void initView() {
		reserveDateTextView = (TextView) this.findViewById(R.id.apply_time);
		reserveDateTextView.setText(ReserveDate);
		joinListView = (AutoListView) this.findViewById(R.id.join_userlistview);
		unJoinListView = (AutoListView) this
				.findViewById(R.id.unjoin_userlistview);

	}

	/**
	 * 加载Actionbar
	 */
	private void initActionbar() {
		useCustomerActionBar();
		getActionBarleft().setText("报名管理");
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
	}
}

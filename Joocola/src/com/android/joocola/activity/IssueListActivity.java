package com.android.joocola.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.joocola.R;
import com.android.joocola.view.AutoListView;

/**
 * 该Activity 是从个人中心点击各种邀约类型 进入的邀约列表的界面。
 * 
 * @author bb
 * 
 */
public class IssueListActivity extends BaseActivity {
	private String type;
	private AutoListView myAutoListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_issuelist);
		myAutoListView = (AutoListView) this
				.findViewById(R.id.issuelist_listview);
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		initActionBar();
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarRight().setVisibility(View.INVISIBLE);
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarleft().setText(type);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}

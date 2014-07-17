package com.android.joocola.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.joocola.R;

public class BaseActivity extends Activity {
	public ActionBar mActionBar;
	// 自定义ActionBar相关
	private TextView left, right, title;
	private ImageView arrow, logo;
	private LinearLayout backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar = getActionBar();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP
				| ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME
				| ActionBar.DISPLAY_USE_LOGO);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;

		}
		return true;
	}

	/**
	 * 要使用自定义actionBar，必须调用这个方法
	 */
	public void useCustomerActionBar() {
		mActionBar.setCustomView(R.layout.layout_actionbar);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = mActionBar.getCustomView();
		left = (TextView) view.findViewById(R.id.leftText);
		right = (TextView) view.findViewById(R.id.rightText);
		logo = (ImageView) view.findViewById(R.id.logo);
		arrow = (ImageView) view.findViewById(R.id.arrow);
		title = (TextView) view.findViewById(R.id.title);
		backButton = (LinearLayout) view.findViewById(R.id.homelayout);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseActivity.this.onBackPressed();
			}
		});
	}

	/**
	 * 得到自定义actionBar左边的文字，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 左边的文字
	 */
	public TextView getActionBarleft() {
		return left;
	}

	/**
	 * 得到自定义actionBar右边的文字，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 右边的文字
	 */
	public TextView getActionBarRight() {
		return right;
	}

	/**
	 * 得到自定义actionBar标题文字，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 标题文字
	 */
	public TextView getActionBarTitle() {
		return title;
	}

	/**
	 * 得到自定义actionBar左边的箭头，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 左边的箭头
	 */
	public ImageView getActionBarArrow() {
		return arrow;
	}

	/**
	 * 得到自定义actionBar自定义的logo，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 自定义的logo
	 */
	public ImageView getActionBarLogo() {
		return logo;
	}

	/**
	 * 得到自定义actionBar自定义的返回布局，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 返回键的layout
	 */
	public LinearLayout getActionBarBackButton() {
		return backButton;
	}

	public View getActionBarCustomerView() {
		return mActionBar.getCustomView();
	}

	/**
	 * 得到ActionBar的CustomerView， 使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return ActionBar的CustomerView
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}

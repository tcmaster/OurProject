package com.android.joocola.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.BitmapUtils;

/**
 * 
 * @author lixiaosong
 * @Destribe:个人中心界面
 * 
 */
public class PersonalCenterActivity extends BaseActivity implements OnClickListener {

	// 用户头像
	private ImageView iv_photo;
	// 红点，依次是头像上的红点，发起邀约的红点，设置的红点
	private ImageView iv_photoRedPoint, iv_addPoint, iv_settingsPoint;
	// 红色背景的文字提示，依次是报名邀约的文字提示，回复邀约的文字提示，评价邀约的文字提示
	private TextView tv_applyPoint, tv_answerPoint, tv_commitPoint;
	// 右边的数值文字，用于表示该条目的数量，依次是发起邀约数，报名邀约数，回复邀约数，收藏邀约数
	private TextView tv_AddCount, tv_applyCount, tv_answerCount, tv_saveCount;
	// 昵称
	private TextView nickName;
	// 编辑按钮
	private Button editButton;
	// 几个点击跳转的布局,依次是发起，报名，回复，收藏，评价，设置
	private LinearLayout ll_add, ll_apply, ll_answer, ll_save, ll_commit, ll_settings;

	private SharedPreferences mSharedPreferences;
	private String user_pid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);
		initViews();
		initActionBar();
		initListeners();
	}

	private void initViews() {
		iv_photo = (ImageView) findViewById(R.id.photoImg);
		iv_addPoint = (ImageView) findViewById(R.id.redPoint);
		iv_photoRedPoint = (ImageView) findViewById(R.id.addPoint);
		iv_settingsPoint = (ImageView) findViewById(R.id.SettingsPoint);
		tv_applyPoint = (TextView) findViewById(R.id.applyPoint);
		tv_answerPoint = (TextView) findViewById(R.id.answerPoint);
		tv_commitPoint = (TextView) findViewById(R.id.commitPoint);
		tv_AddCount = (TextView) findViewById(R.id.AddCount);
		tv_applyCount = (TextView) findViewById(R.id.applyCount);
		tv_answerCount = (TextView) findViewById(R.id.answerCount);
		tv_saveCount = (TextView) findViewById(R.id.saveCount);
		nickName = (TextView) findViewById(R.id.personalName);
		editButton = (Button) findViewById(R.id.edit);
		ll_add = (LinearLayout) findViewById(R.id.addLayout);
		ll_apply = (LinearLayout) findViewById(R.id.applayLayout);
		ll_answer = (LinearLayout) findViewById(R.id.answerLayout);
		ll_save = (LinearLayout) findViewById(R.id.saveLayout);
		ll_commit = (LinearLayout) findViewById(R.id.waitapplyLayout);
		ll_settings = (LinearLayout) findViewById(R.id.settingLayout);
		// 获得当前登录的user_pid
		mSharedPreferences = getSharedPreferences(Constants.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		user_pid = mSharedPreferences.getString(Constants.LOGIN_PID, 0 + "");

	}

	private void initListeners() {
		editButton.setOnClickListener(this);
		ll_add.setOnClickListener(this);// 发起
		ll_apply.setOnClickListener(this);// 报名
		ll_answer.setOnClickListener(this);// 回复
		ll_save.setOnClickListener(this);// 收藏
		ll_commit.setOnClickListener(this);// 评价
		ll_settings.setOnClickListener(this);// 设置
		iv_photo.setOnClickListener(this);

	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarleft().setText("我");
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit:
			Intent intent = new Intent(this, PersonalInfoEditActivity.class);
			startActivity(intent);
			break;
		case R.id.addLayout:
			Intent addIntent = new Intent(this, IssueListActivity.class);
			addIntent.putExtra("type", "我发起的邀约");
			startActivity(addIntent);
			break;
		case R.id.applayLayout:
			Intent applayIntent = new Intent(this, IssueListActivity.class);
			applayIntent.putExtra("type", "我报名的邀约");
			startActivity(applayIntent);
			break;
		case R.id.answerLayout:
			Intent answerIntent = new Intent(this, IssueListActivity.class);
			answerIntent.putExtra("type", "我回复的邀约");
			startActivity(answerIntent);
			break;
		case R.id.saveLayout:
			Intent saveIntent = new Intent(this, IssueListActivity.class);
			saveIntent.putExtra("type", "我收藏的邀约");
			startActivity(saveIntent);
			break;
		case R.id.waitapplyLayout:
			Intent commitIntent = new Intent(this, IssueListActivity.class);
			commitIntent.putExtra("type", "等我评价的邀约");
			startActivity(commitIntent);
			break;
		case R.id.settingLayout:
			Intent settingIntent = new Intent(this, SettingActivity.class);
			startActivity(settingIntent);
			break;
		case R.id.photoImg:
			Intent userIntent = new Intent(PersonalCenterActivity.this, PersonalDetailActivity.class);
			userIntent.putExtra("userId", user_pid + "");
			startActivity(userIntent);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onStart() {
		initUserInfo();
		super.onResume();
	}

	private void initUserInfo() {
		BitmapUtils utils = new BitmapUtils(this);
		utils.configDefaultLoadFailedImage(R.drawable.logo);
		String photoUrl = JoocolaApplication.getInstance().getUserInfo().getPhotoUrl();
		if (photoUrl != null)
			utils.display(iv_photo, Utils.processResultStr(Constants.URL + JoocolaApplication.getInstance().getUserInfo().getPhotoUrl(), "_150_"));
		else {
			utils.display(iv_photo, "errorurl");
		}
		nickName.setText(JoocolaApplication.getInstance().getUserInfo().getNickName());
		tv_AddCount.setText(JoocolaApplication.getInstance().getUserInfo().getStaAppMyCount());
		tv_applyCount.setText(JoocolaApplication.getInstance().getUserInfo().getStaAppJoinCount());
		tv_answerCount.setText(JoocolaApplication.getInstance().getUserInfo().getStaAppReplyCount());
		tv_saveCount.setText(JoocolaApplication.getInstance().getUserInfo().getStaAppFavoriteCount());
		tv_commitPoint.setText(JoocolaApplication.getInstance().getUserInfo().getStaAppWaitCommentCount());
	}
}

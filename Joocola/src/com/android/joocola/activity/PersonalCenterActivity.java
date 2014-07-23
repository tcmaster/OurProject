package com.android.joocola.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.BitmapUtils;

/**
 * 
 * @author lixiaosong
 * @category 个人中心界面暂时不知道最终结构，先用activity来写
 * 
 */
public class PersonalCenterActivity extends BaseActivity implements
		OnClickListener {
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
	private LinearLayout ll_add, ll_apply, ll_answer, ll_save, ll_commit,
			ll_settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);
		initViews();
		initActionBar();
		initListeners();
		initUserInfo();
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
		ll_commit = (LinearLayout) findViewById(R.id.commitLayout);
		ll_settings = (LinearLayout) findViewById(R.id.settingLayout);

	}

	private void initListeners() {
		editButton.setOnClickListener(this);
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
		default:
			break;
		}

	}

	private void initUserInfo() {
		BitmapUtils utils = new BitmapUtils(this);
		utils.display(
				iv_photo,
				Utils.processResultStr(JoocolaApplication.getInstance()
						.getUserInfo().getPhotoUrl(), "_150_"));
		nickName.setText(JoocolaApplication.getInstance().getUserInfo()
				.getNickName());
	}
}

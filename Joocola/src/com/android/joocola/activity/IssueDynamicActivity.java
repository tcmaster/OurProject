package com.android.joocola.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.fragment.ApplyFragment;
import com.android.joocola.fragment.EvaluateFragment;
import com.android.joocola.fragment.InviteFragment;
import com.android.joocola.fragment.ReplyFragment;
import com.android.joocola.utils.Constants;

public class IssueDynamicActivity extends BaseActivity implements OnClickListener {

	private FrameLayout mFrameLayout;
	private TextView apply_tt, reply_tt, invite_tt, evaluate_tt;
	private FragmentManager fragmentManager;
	private ApplyFragment mApplyFragment;
	private EvaluateFragment mEvaluateFragment;
	private InviteFragment mInviteFragment;
	private ReplyFragment mReplyFragment;
	private String mUseID;// 获取当前用户ID
	private SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_issuedynamic);
		initActionbar();
		initView();
		mPreferences = getSharedPreferences(Constants.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		mUseID = mPreferences.getString(Constants.LOGIN_PID, "");

		initFragment();

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

	private void initView() {
		apply_tt = (TextView) this.findViewById(R.id.apply_tt);
		reply_tt = (TextView) this.findViewById(R.id.reply_tt);
		invite_tt = (TextView) this.findViewById(R.id.invite_tt);
		evaluate_tt = (TextView) this.findViewById(R.id.evaluate_tt);
		apply_tt.setOnClickListener(this);
		reply_tt.setOnClickListener(this);
		invite_tt.setOnClickListener(this);
		evaluate_tt.setOnClickListener(this);
	}

	private void initFragment() {
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (mUseID != null && !TextUtils.isEmpty(mUseID)) {
			mApplyFragment = new ApplyFragment();
			mApplyFragment.setmUserID(mUseID);
			fragmentTransaction.replace(R.id.issuedynamic_fl, mApplyFragment);
			fragmentTransaction.commit();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apply_tt:
			FragmentTransaction applyTransaction = fragmentManager.beginTransaction();
			if (mApplyFragment == null) {
				mApplyFragment = new ApplyFragment();
				mApplyFragment.setmUserID(mUseID);
			}
			applyTransaction.replace(R.id.issuedynamic_fl, mApplyFragment);
			applyTransaction.commit();
			break;
		case R.id.reply_tt:
			FragmentTransaction replyTransaction = fragmentManager.beginTransaction();
			if (mReplyFragment == null) {
				mReplyFragment = new ReplyFragment();
				mReplyFragment.setmUserID(mUseID);
			}
			replyTransaction.replace(R.id.issuedynamic_fl, mReplyFragment);
			replyTransaction.commit();
			break;
		case R.id.invite_tt:
			FragmentTransaction inviteTransaction = fragmentManager.beginTransaction();
			if (mInviteFragment == null) {
				mInviteFragment = new InviteFragment();
				mInviteFragment.setmUserID(mUseID);
			}

			inviteTransaction.replace(R.id.issuedynamic_fl, mInviteFragment);
			inviteTransaction.commit();
			break;
		case R.id.evaluate_tt:
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			if (mEvaluateFragment == null) {
				mEvaluateFragment = new EvaluateFragment();
				mEvaluateFragment.setmUserID(mUseID);
			}
			fragmentTransaction.replace(R.id.issuedynamic_fl, mEvaluateFragment);
			fragmentTransaction.commit();
			break;

		default:
			break;
		}
	}
}

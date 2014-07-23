package com.android.joocola.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.GetIssueInfoEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * 邀约详情界面 需要传入1个邀约的pid
 * 
 * @author bb
 * 
 */
public class IssuedinvitationDetailsActivity extends BaseActivity {
	private int issue_pid;
	private TextView title, name, age, astro, issuetime, issuesex, issuecost,
			location, description, state, usercount, replycount;
	private NetworkImageView touxiang;
	private ImageView sexImageView;
	private ImageLoader mImageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_issuedetails);
		Intent intent = getIntent();
		initActionbar();
		mImageLoader = new ImageLoader(
				Volley.newRequestQueue(IssuedinvitationDetailsActivity.this),
				new BitmapCache());
		issue_pid = intent.getIntExtra("issue_pid", -1);
		if (issue_pid == -1) {
		GetIssueInfoEntity getIssueInfoEntity = (GetIssueInfoEntity) intent
				.getSerializableExtra("issueInfo");
			initView(getIssueInfoEntity);
		}
	}

	private void initActionbar() {
		useCustomerActionBar();
		getActionBarleft().setText("邀约详情");
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
	}
	private void initView(GetIssueInfoEntity entity)
	{
		title = (TextView) this
				.findViewById(R.id.issueitem_title);
		name = (TextView) this
				.findViewById(R.id.issueitem_name);
		age = (TextView) this
				.findViewById(R.id.issueitem_age);
		astro = (TextView) this
				.findViewById(R.id.issueitem_astro);
		issuetime = (TextView) this
				.findViewById(R.id.issueitem_time);
		issuesex = (TextView) this
				.findViewById(R.id.issueitem_issuesex);
		issuecost = (TextView) this
				.findViewById(R.id.issueitem_issuecost);
		location = (TextView) this
				.findViewById(R.id.issueitem_location);
		description = (TextView) this
				.findViewById(R.id.issueitem_description);
		state = (TextView) this
				.findViewById(R.id.issueitem_state);
		usercount = (TextView) this
				.findViewById(R.id.issueitem_usercount);
		replycount = (TextView) this
				.findViewById(R.id.issueitem_replycount);
		touxiang = (NetworkImageView) this
				.findViewById(R.id.issueitem_img);
		sexImageView = (ImageView) this
.findViewById(R.id.issueitem_seximg);
		title.setText(entity.getTitle());
		name.setText(entity.getPublisherName());
		age.setText(entity.getPublisherAge() + "");
		astro.setText(entity.getPublisherAstro());
		issuetime.setText(entity.getReserveDate());
		issuesex.setText(entity.getSexName());
		issuecost.setText(entity.getCostName());
		location.setText(entity.getLocationName());
		description.setText(entity.getDescription());
		state.setText(entity.getState());
		usercount.setText("报名(" + entity.getApplyUserCount() + ")");
		replycount.setText("回复(" + entity.getReplyCount() + ")");
		String touxiangUrl = entity.getPublisherPhoto();
		touxiang.setDefaultImageResId(R.drawable.photobg);
		if (touxiangUrl.startsWith("h")) {
			touxiang.setImageUrl(touxiangUrl, mImageLoader);
		} else {
			touxiang.setImageUrl(Constans.URL + touxiangUrl, mImageLoader);
		}
		int publishID = entity.getPublisherID(); // 用于传值到嵩哥界面

	}
}

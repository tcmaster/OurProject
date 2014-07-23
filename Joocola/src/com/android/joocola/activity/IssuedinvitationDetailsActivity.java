package com.android.joocola.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.GetIssueInfoEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
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
	private String url = "Bus.AppointController.QueryAppoint.ashx";
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String json = (String) msg.obj;
				GetIssueInfoEntity getIssueInfoEntity = resloveJson(json);
				initView(getIssueInfoEntity);
				break;

			default:
				break;
			}
		};
	};
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
		} else {
			getIssueInfoEntity(issue_pid);
		}
	}

	private GetIssueInfoEntity getIssueInfoEntity(int issue_pid) {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("IDs", issue_pid + "");
		httpPostInterface.getData(url, new HttpPostCallBack() {
			
			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 0;
				message.obj = result;
				handler.sendMessage(message);
			}
		});
		return null;
	}
	private void initActionbar() {
		useCustomerActionBar();
		getActionBarleft().setText("邀约详情");
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
	}

	private GetIssueInfoEntity resloveJson(String json) {
		JSONObject jsonObject;
		GetIssueInfoEntity getIssueInfoEntity = new GetIssueInfoEntity();
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("Entities");
			if (jsonArray.length() != 0) {
				JSONObject object = jsonArray.getJSONObject(0);
				
				getIssueInfoEntity.setTitle(object.getString("Title"));
				getIssueInfoEntity.setApplyUserCount(object
						.getInt("ApplyUserCount"));
				getIssueInfoEntity.setCostName(object.getString("CostName"));
				getIssueInfoEntity.setDescription(object
						.getString("Description"));
				getIssueInfoEntity.setLocationName(object
						.getString("LocationName"));
				getIssueInfoEntity.setPID(object.getInt("PID"));
				getIssueInfoEntity.setPublishDate(object
						.getString("PublishDate"));
				getIssueInfoEntity.setPublisherAge(object
						.getInt("PublisherAge"));
				getIssueInfoEntity.setPublisherAstro(object
						.getString("PublisherAstro"));
				getIssueInfoEntity.setPublisherBirthday(object
						.getString("PublisherBirthday"));
				getIssueInfoEntity.setPublisherName(object
						.getString("PublisherName"));
				getIssueInfoEntity.setPublisherPhoto(object
						.getString("PublisherPhoto"));
				getIssueInfoEntity.setReplyCount(object.getInt("ReplyCount"));
				getIssueInfoEntity.setReserveDate(object
						.getString("ReserveDate"));
				getIssueInfoEntity.setPublisherID(object.getInt("PublisherID"));
				getIssueInfoEntity.setSexName(object.getString("SexName"));
				getIssueInfoEntity.setState(object.getString("State"));
				getIssueInfoEntity.setTitle(object.getString("Title"));
			}  
		} catch (JSONException e1) {
			 
			e1.printStackTrace();
		}
		return getIssueInfoEntity;

	}
	
	/**
	 * 加载布局
	 * 
	 * @param entity
	 */
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
		Log.e("tagf", touxiangUrl);
		if (touxiangUrl.startsWith("h")) {
			touxiang.setImageUrl(touxiangUrl, mImageLoader);
		} else {
			touxiang.setImageUrl(Constans.URL + touxiangUrl, mImageLoader);
		}
		final int publishID = entity.getPublisherID(); // 用于传值到嵩哥界面
		touxiang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						IssuedinvitationDetailsActivity.this,
						PersonalDetailActivity.class);
				intent.putExtra("userId", publishID + "");
				Log.e("跳转的pid", publishID + "");
				startActivity(intent);
				
			}
		});

	}
}

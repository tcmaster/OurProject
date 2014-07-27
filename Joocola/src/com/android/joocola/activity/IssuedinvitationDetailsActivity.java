package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import com.android.joocola.adapter.IssueReplyAdapter;
import com.android.joocola.entity.GetIssueInfoEntity;
import com.android.joocola.entity.ReplyEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.view.AutoListView;
import com.android.joocola.view.AutoListView.OnLoadListener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * 邀约详情界面 需要传入1个邀约的pid
 * 
 * @author bb
 * 
 */
public class IssuedinvitationDetailsActivity extends BaseActivity implements
		OnLoadListener {
	private int issue_pid;
	private TextView title, name, age, astro, issuetime, issuesex, issuecost,
			location, description, state, usercount, replycount;
	private NetworkImageView touxiang;
	private ImageView sexImageView;
	private ImageLoader mImageLoader;
	private AutoListView mAutoListView;
	private String url = "Bus.AppointController.QueryAppoint.ashx";// 根据pid获得邀约详情的地址;
	private String replyUrl = "Bus.AppointController.QueryAppointReply.ashx";// 邀约评论的地址。
	private int totalItemsCount; // 总共多少条
	private int mTotalPagesCount;// 总共有多少页
	private int mCurPageIndex = 1;// 当前显示多少页
	private IssueReplyAdapter issueReplyAdapter;
	private List<ReplyEntity> list = new ArrayList<ReplyEntity>();
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String json = (String) msg.obj;
				GetIssueInfoEntity getIssueInfoEntity = resloveJson(json);
				initView(getIssueInfoEntity);
				break;
			case 1:
				mAutoListView.onLoadComplete();
				String replyJson = (String) msg.obj;
				List<ReplyEntity> list = resolveJson(replyJson);
				mAutoListView.setResultSize(list.size());
				list.addAll(list);
				issueReplyAdapter.notifyDataSetChanged();
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
		mAutoListView = (AutoListView) this.findViewById(R.id.issue_listview);
		mAutoListView.setOnLoadListener(this);
		BitmapCache bitmapCache = new BitmapCache();
		issueReplyAdapter = new IssueReplyAdapter(list,
				IssuedinvitationDetailsActivity.this, bitmapCache);
		mAutoListView.setAdapter(issueReplyAdapter);
		mImageLoader = new ImageLoader(
				Volley.newRequestQueue(IssuedinvitationDetailsActivity.this),
				new BitmapCache());
		issue_pid = intent.getIntExtra("issue_pid", -1);
		if (issue_pid == -1) {
		GetIssueInfoEntity getIssueInfoEntity = (GetIssueInfoEntity) intent
				.getSerializableExtra("issueInfo");
			issue_pid = getIssueInfoEntity.getPID();
			initView(getIssueInfoEntity);
		} else {
			getIssueInfoEntity(issue_pid);
		}
	}

	private void getIssueInfoEntity(int issue_pid) {
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
				getIssueInfoEntity = JsonUtils.getIssueInfoEntity(object,
						getIssueInfoEntity);
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
		initReplyList();// 加载评论JSON

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
		if (entity.getPublisherSexID() == 1) {
			sexImageView.setImageResource(R.drawable.boy);
			age.setTextColor(getResources().getColor(R.color.lanse));
			astro.setTextColor(getResources().getColor(R.color.lanse));
		} else {
			sexImageView.setImageResource(R.drawable.girl);
			age.setTextColor(getResources().getColor(R.color.fense));
			astro.setTextColor(getResources().getColor(R.color.fense));
		}
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

	/**
	 * 加载评论json
	 */
	private void initReplyList() {

		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("AppointID", issue_pid + "");
		Log.e("ids", issue_pid + "");
		httpPostInterface.addParams("ItemsPerPage", 10 + "");
		httpPostInterface.addParams("CurrentPage", mCurPageIndex + "");
		httpPostInterface.getData(replyUrl, new HttpPostCallBack() {
			
			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 1;
				message.obj = result;
				handler.sendMessage(message);
			}
		});
	}

	/**
	 * 解析评论json
	 */
	private List<ReplyEntity> resolveJson(String json) {
		List<ReplyEntity> list = new ArrayList<ReplyEntity>();
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(json);
			totalItemsCount = jsonObject.getInt("TotalItemsCount");
			mTotalPagesCount = jsonObject.getInt("TotalPagesCount");
			mCurPageIndex = jsonObject.getInt("CurPageIndex");
			JSONArray jsonArray = jsonObject.getJSONArray("Entities");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				ReplyEntity replyEntity = new ReplyEntity();
				replyEntity = JsonUtils
						.getReplyEntity(jsonObject2, replyEntity);
				list.add(replyEntity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public void onLoad() {
		if (mCurPageIndex + 1 > mTotalPagesCount) {
			return;
		}
		mCurPageIndex += 1;
		initReplyList();
	}


}

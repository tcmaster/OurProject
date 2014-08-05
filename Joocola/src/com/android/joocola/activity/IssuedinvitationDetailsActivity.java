package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.IssueReplyAdapter;
import com.android.joocola.entity.GetIssueInfoEntity;
import com.android.joocola.entity.ReplyEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.utils.Utils;
import com.android.joocola.view.AutoListView;
import com.android.joocola.view.AutoListView.OnLoadListener;
import com.android.joocola.view.AutoListView.OnRefreshListener;
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
		OnLoadListener, OnClickListener, OnRefreshListener {
	private int issue_pid;
	private TextView title, name, age, astro, issuetime, issuesex, issuecost,
			location, description, state, usercount, replycount;
	private NetworkImageView touxiang;
	private ImageView sexImageView;
	private ImageLoader mImageLoader;
	private AutoListView mAutoListView;
	private String url = "Bus.AppointController.QueryAppoint.ashx";// 根据pid获得邀约详情的地址;
	private String replyUrl = "Bus.AppointController.QueryAppointReply.ashx";// 邀约评论的地址。
	private String replyItUrl = "Bus.AppointController.PubAppointReply.ashx";// 回复地址。
	private String collectUrl = "Sys.UserController.FavoriteAppoint.ashx";// 收藏该邀约
	private String joinUrl = "Bus.AppointController.ApplyAppoint.ashx";// 加入某邀约
	private String quaryUrl = "Bus.AppointController.QueryAppointUserState.ashx";// 查询用户在某邀约的状态
	private int totalItemsCount; // 总共多少条
	private int mTotalPagesCount;// 总共有多少页
	private int mCurPageIndex = 1;// 当前显示多少页
	private LinearLayout apply_ll, reply_ll, collect_ll;
	private IssueReplyAdapter issueReplyAdapter;
	private List<ReplyEntity> list = new ArrayList<ReplyEntity>();
	private SharedPreferences mSharedPreferences;
	private String user_pid;
	private CustomerDialog customerDialog;
	private CustomerDialog issueDialog;//
	private CustomerDialog clickJoinDialog;
	private int publishid;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			/**
			 * 通过issue_pid加载完成后
			 */
			case 0:
				String json = (String) msg.obj;
				GetIssueInfoEntity getIssueInfoEntity = resloveJson(json);
				publishid = getIssueInfoEntity.getPublisherID();
				initView(getIssueInfoEntity);
				break;

			/**
			 * 加载回复列表
			 */
			case 1:
				mAutoListView.onLoadComplete();
				String replyJson = (String) msg.obj;
				List<ReplyEntity> mlist = resolveJson(replyJson);
				mAutoListView.setResultSize(mlist.size());
				replycount.setText("回复(" + mlist.size() + ")");
				list.addAll(mlist);
				issueReplyAdapter.notifyDataSetChanged();
				break;
			/**
			 * 刷新数据
			 */
			case 2:
				if (customerDialog != null) {
					customerDialog.dismissDlg();
				}
				mAutoListView.onRefreshComplete();
				refreshReply();
				break;

			/**
			 * 收藏之后的返回。
			 */
			case 3:
				String result = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean isCollect = jsonObject.getBoolean("Item1");
					String reason = jsonObject.getString("Item2");
					if (isCollect) {
						Utils.toast(IssuedinvitationDetailsActivity.this,
								"收藏成功");
					} else {
						Utils.toast(IssuedinvitationDetailsActivity.this,
								reason);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			/**
			 * 申请加入邀约后的回复
			 */
			case 4:
				String joinResult = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(joinResult);
					boolean isJoinSucces = jsonObject.getBoolean("Item1");
					String joinString = jsonObject.getString("Item2");
					if (isJoinSucces) {
						Utils.toast(IssuedinvitationDetailsActivity.this,
								"申请成功,请等待发起者同意");
					} else {
						Utils.toast(IssuedinvitationDetailsActivity.this,
								joinString);
					}
					if (issueDialog != null) {
						issueDialog.dismissDlg();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			/**
			 * 查询该用户在该邀约中的状态
			 */
			case 5:
				String resultString = (String) msg.obj;
				showClickIssueDialog(resultString);
				break;
			/**
		    * 
		    */
			case 6:
				showClickIssueDialog(100 + "");
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
		initBottomView();
		// 获得当前登录的user_pid
		mSharedPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE,
				Context.MODE_PRIVATE);
		user_pid = mSharedPreferences.getString(Constans.LOGIN_PID, 0 + "");
		// 初始化回复列表的listview。
		mAutoListView = (AutoListView) this.findViewById(R.id.issue_listview);
		mAutoListView.setOnLoadListener(this);
		mAutoListView.setOnRefreshListener(this);
		BitmapCache bitmapCache = new BitmapCache();
		mImageLoader = new ImageLoader(
				Volley.newRequestQueue(IssuedinvitationDetailsActivity.this),
				bitmapCache);
		issueReplyAdapter = new IssueReplyAdapter(list,
				IssuedinvitationDetailsActivity.this, bitmapCache);
		mAutoListView.setAdapter(issueReplyAdapter);

		// 因为有2个界面可以传入此界面,1为发布完成的时候,2为从首页fragment的listview点进来。
		issue_pid = intent.getIntExtra("issue_pid", -1);
		if (issue_pid == -1) {
			GetIssueInfoEntity getIssueInfoEntity = (GetIssueInfoEntity) intent
					.getSerializableExtra("issueInfo");
			issue_pid = getIssueInfoEntity.getPID();
			publishid = getIssueInfoEntity.getPublisherID();
			initView(getIssueInfoEntity);
		} else {
			getIssueInfoEntity(issue_pid);
		}
	}

	/**
	 * 加载底部的布局
	 */

	private void initBottomView() {
		apply_ll = (LinearLayout) this.findViewById(R.id.apply_ll);
		reply_ll = (LinearLayout) this.findViewById(R.id.reply_ll);
		collect_ll = (LinearLayout) this.findViewById(R.id.collect_ll);
		apply_ll.setOnClickListener(this);
		reply_ll.setOnClickListener(this);
		collect_ll.setOnClickListener(this);

	}

	/**
	 * 根据pid去获得issueinfo
	 * 
	 * @param issue_pid
	 */
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

	/**
	 * 加载Actionbar
	 */
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
	 * 加载上面的布局
	 * 
	 * @param entity
	 */
	private void initView(GetIssueInfoEntity entity) {
		initReplyList();// 加载评论JSON

		title = (TextView) this.findViewById(R.id.issueitem_title);
		name = (TextView) this.findViewById(R.id.issueitem_name);
		age = (TextView) this.findViewById(R.id.issueitem_age);
		astro = (TextView) this.findViewById(R.id.issueitem_astro);
		issuetime = (TextView) this.findViewById(R.id.issueitem_time);
		issuesex = (TextView) this.findViewById(R.id.issueitem_issuesex);
		issuecost = (TextView) this.findViewById(R.id.issueitem_issuecost);
		location = (TextView) this.findViewById(R.id.issueitem_location);
		description = (TextView) this.findViewById(R.id.issueitem_description);
		state = (TextView) this.findViewById(R.id.issueitem_state);
		usercount = (TextView) this.findViewById(R.id.issueitem_usercount);
		replycount = (TextView) this.findViewById(R.id.issueitem_replycount);
		touxiang = (NetworkImageView) this.findViewById(R.id.issueitem_img);
		sexImageView = (ImageView) this.findViewById(R.id.issueitem_seximg);
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
		touxiang.setImageUrl(Constans.URL + touxiangUrl, mImageLoader);

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

	private void refreshReply() {
		mCurPageIndex = 1;
		list.clear();
		initReplyList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reply_ll:// 回复按钮
			showReplyDialog();
			Log.e("reply_btn", "reply_btn");
			break;
		case R.id.apply_ll:// 报名按钮
			queryUserinIssue();
			break;
		case R.id.collect_ll:// 收藏按钮
			collectIssue();
			break;
		default:
			break;
		}

	}

	/**
	 * 收藏
	 */
	private void collectIssue() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("opUserID", user_pid);
		httpPostInterface.addParams("appointID", issue_pid + "");
		httpPostInterface.getData(collectUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 3;
				message.obj = result;
				handler.sendMessage(message);
			}
		});
	}

	/**
	 * 发表回复
	 */
	private void doReply(String replyString) {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("AppointID", issue_pid + "");
		httpPostInterface.addParams("PublisherID", user_pid);
		httpPostInterface.addParams("Content", replyString);
		httpPostInterface.getData(replyItUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (customerDialog != null) {
					customerDialog.dismissDlg();

				}
				Message message = Message.obtain();
				message.what = 2;
				message.obj = result;
				handler.sendMessage(message);
			}
		});

	}

	/**
	 * 弹出回复的对话框。
	 */
	private void showReplyDialog() {
		customerDialog = new CustomerDialog(
				IssuedinvitationDetailsActivity.this, R.layout.dialog_reply);
		customerDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.BOTTOM);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.WRAP_CONTENT);
				final EditText editText = (EditText) dlg
						.findViewById(R.id.dialog_reply_edittext);
				Button button = (Button) dlg
						.findViewById(R.id.dialog_reply_btn);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String inputString = editText.getText().toString();
						if (inputString.isEmpty()) {
							Utils.toast(IssuedinvitationDetailsActivity.this,
									"请输入回复");
							return;
						}
						if (issue_pid == -1) {
							Utils.toast(IssuedinvitationDetailsActivity.this,
									"邀约pid错误");
							return;
						}
						if (user_pid.isEmpty()) {
							Utils.toast(IssuedinvitationDetailsActivity.this,
									"用户pid错误");
							return;
						}
						doReply(inputString);

					}
				});

			}
		});
		customerDialog.showDlg();
	}

	@Override
	public void onRefresh() {
		handler.sendEmptyMessage(2);
	}

	/**
	 * 申请加入某邀约.
	 */
	private void joinIssue() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("appointID", issue_pid + "");
		httpPostInterface.addParams("userID", user_pid);
		httpPostInterface.getData(joinUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 4;
				message.obj = result;
				handler.sendMessage(message);
			}
		});

	}

	/**
	 * 点击我要报名后的对话框显示
	 */
	private void showClickIssueDialog(final String result) {

		clickJoinDialog = new CustomerDialog(
				IssuedinvitationDetailsActivity.this, R.layout.click_joinissue);
		clickJoinDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.BOTTOM);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.WRAP_CONTENT);

				Button joinBtn = (Button) dlg.findViewById(R.id.click_joinbtn);
				Button mangerBtn = (Button) dlg
						.findViewById(R.id.click_issuemangerbtn);
				mangerBtn.setVisibility(View.INVISIBLE);
				if (result.equals("0")) {
					joinBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showIssueDialog();
						}
					});
				} else if (result.equals("10")) {
					joinBtn.setOnClickListener(null);
					joinBtn.setBackgroundResource(R.drawable.btnclick);
					joinBtn.setText("已报名");
					joinBtn.setTextColor(getResources().getColor(R.color.black));
				} else if (result.equals("100")) {
					joinBtn.setVisibility(View.INVISIBLE);
					mangerBtn.setVisibility(View.VISIBLE);
					mangerBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Utils.toast(IssuedinvitationDetailsActivity.this,
									"该功能正在编写");
						}
					});
				}
			}
		});
		clickJoinDialog.showDlg();
	}

	/**
	 * 查询该用户在该邀约的状态
	 */
	private void queryUserinIssue() {
		if (user_pid.equals(publishid + "")) {
			handler.sendEmptyMessage(6);
		} else {

			HttpPostInterface httpPostInterface = new HttpPostInterface();
			httpPostInterface.addParams("appointID", issue_pid + "");
			httpPostInterface.addParams("userID", user_pid);
			httpPostInterface.getData(quaryUrl, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					Message message = Message.obtain();
					message.what = 5;
					message.obj = result;
					handler.sendMessage(message);
				}
			});
		}
	}

	/**
	 * 显示是否加入邀约的对话框
	 */
	private void showIssueDialog() {
		issueDialog = new CustomerDialog(this, R.layout.dlg_message);
		issueDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.CENTER);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.WRAP_CONTENT);
				TextView title = (TextView) dlg.findViewById(R.id.dlg_pe_title);
				TextView content = (TextView) dlg
						.findViewById(R.id.dlg_message);
				TextView ok = (TextView) dlg.findViewById(R.id.dlg_pe_ok);
				TextView cancel = (TextView) dlg
						.findViewById(R.id.dlg_pe_cancel);
				content.setText("若被选中,您和发起人可以互相看到对方手机号");
				ok.setText("确认");
				cancel.setText("取消");
				title.setText("确认报名");
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						joinIssue();
					}
				});
				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						issueDialog.dismissDlg();
					}
				});

			}
		});
		issueDialog.showDlg();
	}
}

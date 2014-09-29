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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.IssueReplyAdapter;
import com.android.joocola.app.JoocolaApplication;
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
import com.android.joocola.view.MyListView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * 邀约详情界面 需要传入1个邀约的pid
 * 
 * @author bb
 * 
 */
public class IssuedinvitationDetailsActivity extends BaseActivity implements OnClickListener {

	private int issue_pid;
	private TextView title, name, age, astro, issuetime, issuesex, issuecost, location, description, state,
			usercount, replycount;
	private NetworkImageView touxiang;
	private ImageView sexImageView;
	private ImageLoader mImageLoader;
	private MyListView mAutoListView;
	private String url = "Bus.AppointController.QueryAppoint.ashx";// 根据pid获得邀约详情的地址;
	private String replyUrl = "Bus.AppointController.QueryAppointReply.ashx";// 邀约评论的地址。
	private String replyItUrl = "Bus.AppointController.PubAppointReply.ashx";// 回复地址。
	private String collectUrl = "Sys.UserController.FavoriteAppoint.ashx";// 收藏该邀约
	private String joinUrl = "Bus.AppointController.ApplyAppoint.ashx";// 加入某邀约
	private String quaryUrl = "Bus.AppointController.QueryAppointUserState.ashx";// 查询用户在某邀约的状态
	private String isFavoriteUrl = "Sys.UserController.IsFavoriteAppoint.ashx";// 查询用户是否收藏该邀约
	private String cancleFavoriteUrl = "Sys.UserController.FavoriteAppointCancel.ashx";// 取消收藏某邀约
	private String cancleIssueUrl = "Bus.AppointController.CancelAppoint.ashx";// 取消报名某邀约
	private LinearLayout apply_ll, reply_ll, collect_ll;
	private TextView applly_lltxt;
	private IssueReplyAdapter issueReplyAdapter;
	private List<ReplyEntity> list = new ArrayList<ReplyEntity>();
	private SharedPreferences mSharedPreferences;
	private String user_pid;
	private CustomerDialog customerDialog;
	private CustomerDialog issueDialog;//
	private CustomerDialog cancleDialog;// 取消报名的对话框
	private int publishid;
	private String stateString;
	private String ReserveDate; // 邀约的到期日子。
	private int whatState = 0; // 判断点击我要报名后的操作

	private TextView collect_txt;// 收藏的文字显示
	private ImageView collect_img;// 收藏的图片显示

	private boolean isCollect = false;
	private String RoomId = "";// 群聊房间ID
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
				isPublish(getIssueInfoEntity);
				publishid = getIssueInfoEntity.getPublisherID();
				RoomId = getIssueInfoEntity.getRoomID();
				initView(getIssueInfoEntity);
				break;

			/**
			 * 加载回复列表
			 */
			case 1:
				// mAutoListView.onLoadComplete();
				String replyJson = (String) msg.obj;
				List<ReplyEntity> mlist = resolveJson(replyJson);
				// mAutoListView.setResultSize(mlist.size());
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
				// mAutoListView.onRefreshComplete();
				initReplyList();
				break;

			/**
			 * 收藏之后的返回。
			 */
			case 3:
				String result = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(result);
					boolean resultCollect = jsonObject.getBoolean("Item1");
					String reason = jsonObject.getString("Item2");
					if (resultCollect) {
						Utils.toast(IssuedinvitationDetailsActivity.this, "收藏成功");
						collect_img.setImageResource(R.drawable.collectok);
						collect_txt.setText("已收藏");
						isCollect = true;
					} else {
						Utils.toast(IssuedinvitationDetailsActivity.this, reason);
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
						Utils.toast(IssuedinvitationDetailsActivity.this, "申请成功,请等待发起者同意");
						applly_lltxt.setText("已报名");
						whatState = 50;// 正在报名 点这个按钮的不是发布者,已经报完名了。
					} else {
						Utils.toast(IssuedinvitationDetailsActivity.this, joinString);
					}
					if (issueDialog != null) {
						issueDialog.dismissDlg();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
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
		mSharedPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		user_pid = mSharedPreferences.getString(Constans.LOGIN_PID, 0 + "");
		// 初始化回复列表的listview。
		mAutoListView = (MyListView) this.findViewById(R.id.issue_listview);
		// mAutoListView.setOnLoadListener(this);
		// mAutoListView.setOnRefreshListener(this);
		BitmapCache bitmapCache = new BitmapCache();
		mImageLoader = new ImageLoader(Volley.newRequestQueue(IssuedinvitationDetailsActivity.this), bitmapCache);
		issueReplyAdapter = new IssueReplyAdapter(list, IssuedinvitationDetailsActivity.this, bitmapCache);
		mAutoListView.setAdapter(issueReplyAdapter);

		// 因为有2个界面可以传入此界面,1为发布完成的时候,2为从首页fragment的listview点进来。
		issue_pid = intent.getIntExtra("issue_pid", -1);
		if (issue_pid == -1) {
			GetIssueInfoEntity getIssueInfoEntity = (GetIssueInfoEntity) intent.getSerializableExtra("issueInfo");
			issue_pid = getIssueInfoEntity.getPID();
			publishid = getIssueInfoEntity.getPublisherID();
			RoomId = getIssueInfoEntity.getRoomID();
			initView(getIssueInfoEntity);
			isPublish(getIssueInfoEntity);
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
		applly_lltxt = (TextView) this.findViewById(R.id.apply_lltxt);
		collect_img = (ImageView) this.findViewById(R.id.collect_img);
		collect_txt = (TextView) this.findViewById(R.id.collect_txt);
		apply_ll.setOnClickListener(this);
		reply_ll.setOnClickListener(this);
		collect_ll.setOnClickListener(this);

	}

	private void isFavorite() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("opUserID", user_pid);
		httpPostInterface.addParams("appointID", issue_pid + "");
		httpPostInterface.getData(isFavoriteUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result.equals("true")) {
					isCollect = true;
					handler.post(new Runnable() {

						@Override
						public void run() {
							collect_img.setImageResource(R.drawable.collectok);
							collect_txt.setText("已收藏");
						}
					});
				} else if (result.equals("false")) {
					isCollect = false;
					handler.post(new Runnable() {

						@Override
						public void run() {
							collect_img.setImageResource(R.drawable.collect);
							collect_txt.setText("收藏");
						}
					});
				}
			}
		});
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
				Log.e("lixiaosong", "邀约详情是" + result);
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
		getActionBarRight().setVisibility(View.VISIBLE);
		getActionBarRight().setText("群聊");
		getActionBarRight().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doEnterMultiChat();
			}
		});
	}

	/**
	 * 进入群聊界面
	 */
	private void doEnterMultiChat() {
		// 判断当前用户是否处于群聊界面
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("AppointID", issue_pid + "");
		interface1.addParams("ItemsPerPage", "999");
		interface1.addParams("AppointUserOnlyJoined", "true");
		interface1.getData(Constans.USERSIMPLE, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result == null || result.equals("")) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(IssuedinvitationDetailsActivity.this, "获取邀约用户信息失败");
						}
					});
				} else {
					JSONObject object;
					try {
						object = new JSONObject(result);
						final JSONArray array = object.getJSONArray("Entities");
						for (int i = 0; i < array.length(); i++) {
							final JSONObject userObject = array.getJSONObject(i);
							if (userObject.getString("PID").equals(JoocolaApplication.getInstance().getPID())) {
								handler.post(new Runnable() {

									@Override
									public void run() {
										Intent intent = new Intent(IssuedinvitationDetailsActivity.this, ChatActivity.class);
										intent.putExtra("isSingle", false);
										intent.putExtra("userNickName", title.getText().toString());// 房间名
										intent.putExtra("userId", RoomId);// 房间id
										intent.putExtra("issueID", issue_pid + "");// 邀约id
										ArrayList<String> userPIDs = new ArrayList<String>();
										for (int j = 0; j < array.length(); j++) {
											try {
												userPIDs.add(array.getJSONObject(j).getString("PID"));
											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
										intent.putStringArrayListExtra("userPIDs", userPIDs);
										startActivity(intent);

									}
								});
								return;
							}
						}
						handler.post(new Runnable() {

							@Override
							public void run() {
								Utils.toast(IssuedinvitationDetailsActivity.this, "对不起，您没有参加该邀约，无法参与聊天");
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private GetIssueInfoEntity resloveJson(String json) {
		JSONObject jsonObject;
		GetIssueInfoEntity getIssueInfoEntity = new GetIssueInfoEntity();
		try {
			jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("Entities");
			if (jsonArray.length() != 0) {
				JSONObject object = jsonArray.getJSONObject(0);
				getIssueInfoEntity = JsonUtils.getIssueInfoEntity(object, getIssueInfoEntity);
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
		isFavorite();// 查询是否收藏该邀约
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
		ReserveDate = entity.getReserveDate();
		issuesex.setText(entity.getSexName());
		issuecost.setText(entity.getCostName());
		location.setText(entity.getLocationName());
		description.setText(entity.getDescription());
		// description.setMovementMethod(ScrollingMovementMethod.getInstance());
		state.setText(entity.getState());
		stateString = entity.getState();
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
		touxiang.setImageUrl(Utils.processResultStr(Constans.URL + touxiangUrl, "_150_"), mImageLoader);

		final int publishID = entity.getPublisherID(); // 用于传值到嵩哥界面
		touxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IssuedinvitationDetailsActivity.this, PersonalDetailActivity.class);
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
		list.clear();
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("AppointID", issue_pid + "");
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
			JSONArray jsonArray = jsonObject.getJSONArray("Entities");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject2 = jsonArray.getJSONObject(i);
				ReplyEntity replyEntity = new ReplyEntity();
				replyEntity = JsonUtils.getReplyEntity(jsonObject2, replyEntity);
				list.add(replyEntity);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reply_ll:// 回复按钮
			showReplyDialog();
			Log.e("reply_btn", "reply_btn");
			break;
		case R.id.apply_ll:// 报名按钮
			Log.e("bb->我要报名", whatState + "");
			if (whatState == 10) {
				Intent intent = new Intent(IssuedinvitationDetailsActivity.this, ApplyMangersActivity.class);
				intent.putExtra("issue_pid", issue_pid + "");
				intent.putExtra("ReserveDate", ReserveDate);
				startActivity(intent);
			} else if (whatState == 20) {
				showIssueDialog();
			} else if (whatState == 30) {
				// 以发布者的身份进入评价管理
				Intent intent = new Intent(IssuedinvitationDetailsActivity.this, EvaluateMangerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("user_id", user_pid);
				bundle.putBoolean("ispublish", true);
				bundle.putInt("issue_pid", issue_pid);
				intent.putExtra("value", bundle);
				startActivity(intent);

			} else if (whatState == 40) {
				// 以参加者的身份进入评价管理
				Intent intent = new Intent(IssuedinvitationDetailsActivity.this, EvaluateMangerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("user_id", user_pid);
				bundle.putBoolean("ispublish", false);
				bundle.putInt("issue_pid", issue_pid);
				intent.putExtra("value", bundle);
				startActivity(intent);
			} else if (whatState == 50 || whatState == 60) {
				showCancleDialog();
			} else if (whatState == 0) {
				Utils.toast(IssuedinvitationDetailsActivity.this, "加载状态中,请稍后");
			}
			break;
		case R.id.collect_ll:// 收藏按钮
			Log.e("bb", isCollect + "");
			if (isCollect) {
				cancleCollectIssue();
			} else {
				collectIssue();
			}
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
	 * 取消收藏
	 */
	private void cancleCollectIssue() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("opUserID", user_pid);
		httpPostInterface.addParams("appointID", issue_pid + "");
		httpPostInterface.getData(cancleFavoriteUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result.equals("true")) {
					isCollect = false;
					handler.post(new Runnable() {

						@Override
						public void run() {
							collect_img.setImageResource(R.drawable.collect);
							collect_txt.setText("收藏");
							Utils.toast(IssuedinvitationDetailsActivity.this, "取消成功");
						}
					});
				} else if (result.equals("false")) {
					Utils.toast(IssuedinvitationDetailsActivity.this, "取消失败");
				}
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
		customerDialog = new CustomerDialog(IssuedinvitationDetailsActivity.this, R.layout.dlg_feedback);
		customerDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.CENTER);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
				TextView title = (TextView) dlg.findViewById(R.id.dlg_pe_title);
				final EditText content = (EditText) dlg.findViewById(R.id.feedback_edit);
				TextView ok = (TextView) dlg.findViewById(R.id.dlg_pe_ok);
				TextView cancel = (TextView) dlg.findViewById(R.id.dlg_pe_cancel);
				ok.setText("确认");
				cancel.setText("取消");
				title.setText("回复信息");

				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String inputString = content.getText().toString();
						if (inputString.isEmpty()) {
							Utils.toast(IssuedinvitationDetailsActivity.this, "请输入回复");
							return;
						}
						if (issue_pid == -1) {
							Utils.toast(IssuedinvitationDetailsActivity.this, "邀约pid错误");
							return;
						}
						if (user_pid.isEmpty()) {
							Utils.toast(IssuedinvitationDetailsActivity.this, "用户pid错误");
							return;
						}
						doReply(inputString);

					}
				});
				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						customerDialog.dismissDlg();
					}
				});

			}
		});
		customerDialog.showDlg();
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
	 * 查询该用户在该邀约的状态
	 */
	private void queryUserinIssue() {

		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("appointID", issue_pid + "");
		httpPostInterface.addParams("userID", user_pid);
		httpPostInterface.getData(quaryUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(final String result) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						Log.e("bb", result + "这是查询状态的");
						if (result.equals("0") || result.equals("20")) {

							applly_lltxt.setText("我要报名");
							whatState = 20;// 正在报名 点这个按钮的不是发布者,还没报名

						} else if (result.equals("10")) {
							applly_lltxt.setText("已报名");
							whatState = 50;// 正在报名 点这个按钮的不是发布者,已经报完名了。

						} else if (result.equals("30")) {
							applly_lltxt.setText("已加入");
							whatState = 60;// 正在报名 点这个按钮的不是发布者,已经报完名了。并且被批准加入了
						}
					}
				});
			}
		});
	}

	// }

	/**
	 * 显示是否加入邀约的对话框
	 */
	private void showIssueDialog() {
		issueDialog = new CustomerDialog(this, R.layout.dlg_message);
		issueDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.CENTER);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
				TextView title = (TextView) dlg.findViewById(R.id.dlg_pe_title);
				TextView content = (TextView) dlg.findViewById(R.id.dlg_message);
				TextView ok = (TextView) dlg.findViewById(R.id.dlg_pe_ok);
				TextView cancel = (TextView) dlg.findViewById(R.id.dlg_pe_cancel);
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

	/**
	 * 显示是否退出邀约报名的dialog
	 * 
	 * @param getIssueInfoEntity
	 */
	private void showCancleDialog() {
		cancleDialog = new CustomerDialog(this, R.layout.dlg_message);
		cancleDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.CENTER);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
				TextView title = (TextView) dlg.findViewById(R.id.dlg_pe_title);
				TextView content = (TextView) dlg.findViewById(R.id.dlg_message);
				TextView ok = (TextView) dlg.findViewById(R.id.dlg_pe_ok);
				TextView cancel = (TextView) dlg.findViewById(R.id.dlg_pe_cancel);
				content.setText("确认取消报名吗?");
				ok.setText("确认");
				cancel.setText("取消");
				title.setText("取消报名");
				ok.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cancleIssue();

					}
				});
				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cancleDialog.dismissDlg();
					}
				});

			}
		});
		cancleDialog.showDlg();
	}

	/**
	 * 用户自己取消报名
	 * 
	 * @param getIssueInfoEntity
	 */
	private void cancleIssue() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("appointID", issue_pid + "");
		httpPostInterface.addParams("userID", user_pid);
		httpPostInterface.getData(cancleIssueUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(final String result) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						try {
							JSONObject jsonObject = new JSONObject(result);
							boolean isCancle = jsonObject.getBoolean("Item1");
							String reason = jsonObject.getString("Item2");
							if (isCancle) {
								applly_lltxt.setText("我要报名");
								whatState = 20;// 正在报名 点这个按钮的不是发布者,还没报名
								Utils.toast(IssuedinvitationDetailsActivity.this, "已取消报名");
								cancleDialog.dismissDlg();
							} else {
								Utils.toast(IssuedinvitationDetailsActivity.this, reason);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

	}

	private void isPublish(GetIssueInfoEntity getIssueInfoEntity) {

		String state = getIssueInfoEntity.getState();
		String publisherID = getIssueInfoEntity.getPublisherID() + "";
		if (state.equals("正在报名") && (publisherID.equals(user_pid))) {
			applly_lltxt.setText("报名管理");
			whatState = 10; // 正在报名 并且点这个按钮的是发布者
		} else if (state.equals("正在报名") && (!(publisherID.equals(user_pid)))) {
			queryUserinIssue();
		} else if (!(state.equals("正在报名")) && (publisherID.equals(user_pid))) {
			applly_lltxt.setText("评价管理");
			whatState = 30;// 该邀约报名结束了 点这个按钮的是发布者
		} else if (!(state.equals("正在报名")) && (!(publisherID.equals(user_pid)))) {
			applly_lltxt.setText("评价管理");
			whatState = 40;// 该邀约报名结束了 点这个按钮的不是发布者
		}
		Log.e("bb", "user_pid" + user_pid);
		Log.e("bb", "publisherID" + publisherID);
		Log.e("bb", "state" + state);

	}
}

package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.Dlg_ListView_Adapter;
import com.android.joocola.adapter.IssueAdapter;
import com.android.joocola.adapter.PC_Edit_GridView_Adapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.app.JoocolaApplication.InitAddInfo;
import com.android.joocola.entity.GetIssueInfoEntity;
import com.android.joocola.entity.IssueInfo;
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.utils.Utils;
import com.android.joocola.view.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 个人详情界面 该界面使用了XUtils
 * 
 * @author lixiaosong
 * 
 */
public class PersonalDetailActivity extends BaseActivity {

	/**
	 * 本页面的用户ID
	 */
	private String userId;
	/**
	 * 本页面的用户信息
	 */
	private UserInfo info;
	/**
	 * 星座和年龄
	 */
	@ViewInject(R.id.pd_ageAndstar_tv)
	private TextView ageAndStar_tv;
	/**
	 * 性别
	 */
	@ViewInject(R.id.pd_sexImg_iv)
	private ImageView sex_iv;
	/**
	 * 位置
	 */
	@ViewInject(R.id.pd_locale_tv)
	private TextView locale_tv;
	/**
	 * 时间
	 */
	@ViewInject(R.id.pd_time_tv)
	private TextView time_tv;
	/**
	 * 昵称
	 */
	@ViewInject(R.id.pd_nickname_tv)
	private TextView nickName_tv;
	/**
	 * 爱好
	 */
	@ViewInject(R.id.pd_hobby_tv)
	private TextView hobby_tv;
	/**
	 * 个性签名
	 */
	@ViewInject(R.id.pd_signin_tv)
	private TextView signIn_tv;
	/**
	 * 手机
	 */
	@ViewInject(R.id.pd_phone_tv)
	private TextView phone_tv;
	/**
	 * 发起中的邀约
	 */
	@ViewInject(R.id.pd_startDateCount_tv)
	private TextView startDate_tv;
	/**
	 * 新增评价数量
	 */
	@ViewInject(R.id.newCommitCount)
	private TextView addCommitCount;
	/**
	 * 评价数量
	 */
	@ViewInject(R.id.pd_commitCount_tv)
	private TextView commitCount;
	/**
	 * 所在地
	 */
	@ViewInject(R.id.pd_location_tv)
	private TextView location_tv;
	/**
	 * 职业
	 */
	@ViewInject(R.id.pd_profession_tv)
	private TextView profession_tv;
	/**
	 * 年收入
	 */
	@ViewInject(R.id.pd_annualSalary_tv)
	private TextView annualSalary_tv;
	/**
	 * 身高
	 */
	@ViewInject(R.id.pd_height_tv)
	private TextView height_tv;
	/**
	 * 情感状态
	 */
	@ViewInject(R.id.pd_emotion_tv)
	private TextView emotion_tv;
	/**
	 * 抽烟
	 */
	@ViewInject(R.id.pd_smoke_tv)
	private TextView smoke_tv;
	/**
	 * 喝酒
	 */
	@ViewInject(R.id.pd_drink_tv)
	private TextView drink_tv;
	/**
	 * 用户相册
	 */
	@ViewInject(R.id.pd_myPicShow_gv)
	private MyGridView pic_gv;
	/**
	 * 邀请按钮
	 */
	@ViewInject(R.id.pd_add_ll)
	private LinearLayout add_ll;
	/**
	 * 关注按钮
	 */
	@ViewInject(R.id.pd_like_ll)
	private LinearLayout like_ll;
	/**
	 * 聊天按钮
	 */
	@ViewInject(R.id.pd_talk_ll)
	private LinearLayout talk_ll;
	/**
	 * 关注的文字
	 */
	@ViewInject(R.id.like_tv)
	private TextView like_tv;

	/**
	 * 有关手机的布局，当双方没有邀约关系时，该布局不能进行显示
	 */
	@ViewInject(R.id.pd_phone_ll)
	private LinearLayout pd_phone_ll;
	/**
	 * 我发起的邀约线性布局
	 */
	@ViewInject(R.id.myrelease_issues_ll)
	private LinearLayout myrelease_issues_ll;
	/**
	 * 我获得的所有评价
	 */
	@ViewInject(R.id.myevaluate_ll)
	private LinearLayout myevaluate_ll;
	/**
	 * 用于向主线程发送数据的handler
	 */
	private static Handler handler;
	private ArrayList<IssueInfo> mIssueInfos;
	private JoocolaApplication mJoocolaApplication;
	private BitmapCache bitmapCache;
	private CustomerDialog customerDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_detail);
		ViewUtils.inject(this);
		mJoocolaApplication = JoocolaApplication.getInstance();
		mIssueInfos = new ArrayList<IssueInfo>();
		bitmapCache = JoocolaApplication.getInstance().getBitmapCache();
		userId = getIntent().getStringExtra("userId");
		initLike();
		handler = new Handler();
		initActionBar();
		if (userId != null) {
			getAndShowUserInfo();
		} else {
			Utils.toast(this, "获取用户信息失败");
		}
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarleft().setText("详细资料");
		getActionBarTitle().setText("");
		if (userId.equals(JoocolaApplication.getInstance().getPID())) {
			getActionBarRight().setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.edit), null);
			getActionBarRight().setText("");
			getActionBarRight().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(PersonalDetailActivity.this, PersonalInfoEditActivity.class);
					startActivity(intent);
				}
			});
		} else
			getActionBarRight().setVisibility(View.INVISIBLE);

	}

	/**
	 * 得到用户信息，并且进行展示
	 */
	private void getAndShowUserInfo() {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("UserIDs", userId);
		interface1.addParams("CurUserID", JoocolaApplication.getInstance().getPID());
		interface1.getData(Constants.USERINFOURL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result != null && !result.equals("")) {
					info = new UserInfo();
					try {
						JSONObject object = new JSONObject(result);
						JSONArray array = object.getJSONArray("Entities");
						JSONObject userObject = array.getJSONObject(0);
						JsonUtils.getUserInfo(userObject, info);
						handler.post(new Runnable() {

							@Override
							public void run() {
								ageAndStar_tv.setText(info.getAge() + " " + info.getAstro());
								if (info.getSexName().contains("男")) {
									ageAndStar_tv.setTextColor(getResources().getColor(R.color.lanse));
									sex_iv.setImageResource(R.drawable.boy);
								} else {
									ageAndStar_tv.setTextColor(getResources().getColor(R.color.fense));
									sex_iv.setImageResource(R.drawable.girl);
								}
								locale_tv.setText(Utils.getNewStr(info.getLocDistince(), "未填写"));
								time_tv.setText(Utils.getNewStr(info.getLocDate(), "未填写"));
								nickName_tv.setText(Utils.getNewStr(info.getNickName(), "未填写"));
								hobby_tv.setText(Utils.getNewStr(info.getHobbyNames(), "未填写"));
								signIn_tv.setText(Utils.getNewStr(info.getSignature(), "这个人很懒，什么也没有留下"));
								phone_tv.setText(Utils.getNewStr(info.getPhone(), "未填写"));
								startDate_tv.setText(info.getStaAppMyCount() + "");
								commitCount.setText(info.getStaAppScoredCount() + "");
								addCommitCount.setText("0");
								addCommitCount.setVisibility(View.INVISIBLE);
								location_tv.setText(Utils.getNewStr(info.getNewCityName(), "未填写"));
								profession_tv.setText(Utils.getNewStr(info.getProfessionName(), "未填写"));
								height_tv.setText(Utils.getNewStr(info.getHeightName(), "未填写"));
								annualSalary_tv.setText(Utils.getNewStr(info.getRevenueName(), "未填写"));
								emotion_tv.setText(Utils.getNewStr(info.getMarryName(), "未填写"));
								smoke_tv.setText(Utils.getNewStr(info.getSmokeName(), "未填写"));
								drink_tv.setText(Utils.getNewStr(info.getDrinkName(), "未填写"));
								String[] imgUrls = info.getAlbumPhotoUrls().split(",");
								pic_gv.setAdapter(new PC_Edit_GridView_Adapter(PersonalDetailActivity.this, false));
								for (int i = 0; i < imgUrls.length; i++) {
									if (imgUrls[i].equals(""))
										continue;
									else
										((PC_Edit_GridView_Adapter) pic_gv.getAdapter()).addImgUrls(imgUrls[i]);
								}
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(getApplicationContext(), "获取信息失败，请重试");
						}
					});

			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 确定关注按钮的初始状态
	 */
	private void initLike() {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("opUserID", JoocolaApplication.getInstance().getPID());
		interface1.addParams("likeUserID", userId);
		interface1.getData(Constants.IS_LIKE_USER_URL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result != null) {
					if (result.equals("true")) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								like_tv.setText("已关注");
							}
						});
					} else {
						handler.post(new Runnable() {

							@Override
							public void run() {
								like_tv.setText("关注");
							}
						});
					}
				} else {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(PersonalDetailActivity.this, "网络状况不佳");
						}
					});
				}
			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 用来判断是否关注该用户
	 */
	private void ifLikeUser() {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("opUserID", JoocolaApplication.getInstance().getPID());
		interface1.addParams("likeUserID", userId);
		interface1.getData(Constants.IS_LIKE_USER_URL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result != null) {
					if (result.equals("true")) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								processUnlike();
							}
						});
					} else {
						handler.post(new Runnable() {

							@Override
							public void run() {
								processLike();
							}
						});
					}
				} else {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(PersonalDetailActivity.this, "网络状况不佳");
						}
					});
				}
			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}

	private void likeUser() {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("opUserID", JoocolaApplication.getInstance().getUserInfo().getPID());
		interface1.addParams("likeUserID", userId);
		interface1.getData(Constants.LIKE_USER_URL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result != null) {
					try {
						final JSONObject object = new JSONObject(result);
						if (object.getBoolean("Item1")) {
							handler.post(new Runnable() {

								@Override
								public void run() {
									Utils.toast(PersonalDetailActivity.this, "关注成功");
									like_tv.setText("已关注");
								}
							});

						} else {
							handler.post(new Runnable() {

								@Override
								public void run() {
									try {
										Utils.toast(PersonalDetailActivity.this, object.getString("Item2"));
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
							});
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(PersonalDetailActivity.this, "网络状况不佳");
						}
					});
				}
			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}

	private void unlikeUser() {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("opUserID", JoocolaApplication.getInstance().getUserInfo().getPID());
		interface1.addParams("likeUserID", userId);
		interface1.getData(Constants.UNLIKE_USER_URL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result != null) {
					if (result.equals("true")) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								Utils.toast(PersonalDetailActivity.this, "已取消关注");
								like_tv.setText("关注");
							}
						});

					} else {
						handler.post(new Runnable() {

							@Override
							public void run() {
								Utils.toast(PersonalDetailActivity.this, "未能成功取消关注");

							}
						});
					}
				} else {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(PersonalDetailActivity.this, "网络状况不佳");
						}
					});
				}
			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}

	@OnItemClick(R.id.pd_myPicShow_gv)
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, WatchBigPicActivity.class);
		intent.putStringArrayListExtra("imgUrls", ((PC_Edit_GridView_Adapter) pic_gv.getAdapter()).getImageUrls());
		intent.putExtra("position", position);
		startActivity(intent);
	}

	@OnClick({ R.id.pd_add_ll, R.id.pd_like_ll, R.id.pd_talk_ll, R.id.myrelease_issues_ll, R.id.myevaluate_ll })
	public void OnClick(View v) {
		switch (v.getId()) {
		case R.id.pd_add_ll:
			if (Utils.isNetConn(PersonalDetailActivity.this))
				processAdd();
			else
				Utils.toast(PersonalDetailActivity.this, "网络异常");
			break;
		case R.id.pd_like_ll:
			if (Utils.isNetConn(PersonalDetailActivity.this))
				ifLikeUser();

			else
				Utils.toast(PersonalDetailActivity.this, "网络异常");
			break;
		case R.id.pd_talk_ll:
			if (Utils.isNetConn(PersonalDetailActivity.this))
				processTalk();
			else
				Utils.toast(PersonalDetailActivity.this, "网络异常");
			break;
		case R.id.myrelease_issues_ll:
			Intent intent = new Intent(PersonalDetailActivity.this, IssueListActivity.class);
			intent.putExtra("userid", userId);
			intent.putExtra("type", "该用户发起的邀约");
			startActivity(intent);
			break;
		case R.id.myevaluate_ll:

			Intent myevaluateIntent = new Intent();
			myevaluateIntent.setClass(PersonalDetailActivity.this, TheUserAllEvaluateActivity.class);
			myevaluateIntent.putExtra("RelateUserID", userId);
			myevaluateIntent.putExtra("name", info.getNickName());
			startActivity(myevaluateIntent);
			break;
		default:
			break;
		}
	}

	private void processAdd() {
		final CustomerDialog cdlg = new CustomerDialog(this, R.layout.dlg_singlechoice);
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				final TextView title_tv = (TextView) window.findViewById(R.id.dlg_pe_title);
				final TextView ok_btn = (TextView) window.findViewById(R.id.dlg_pe_ok);
				final TextView cancel_btn = (TextView) window.findViewById(R.id.dlg_pe_cancel);
				final ListView add_lv = (ListView) window.findViewById(R.id.dlg_pe_listview);
				final List<GetIssueInfoEntity> entitys = new ArrayList<GetIssueInfoEntity>();
				title_tv.setText("邀请参加我的邀约");
				HttpPostInterface interface1 = new HttpPostInterface();
				interface1.addParams("ItemsPerPage", 9999 + "");
				interface1.addParams("State", 1 + "");
				interface1.addParams("PublisherID", JoocolaApplication.getInstance().getUserInfo().getPID());
				interface1.getData(Constants.GET_QUERY_APPOINT, new HttpPostCallBack() {

					@Override
					public void httpPostResolveData(String result) {
						try {
							if (result != null) {
								JSONObject jObject = new JSONObject(result);
								JSONArray array = jObject.getJSONArray("Entities");
								for (int i = 0; i < array.length(); i++) {
									JSONObject object = array.getJSONObject(i);
									GetIssueInfoEntity entity = new GetIssueInfoEntity();
									entity.setPID(object.getInt("PID"));
									entity.setTypeID(object.getInt("TypeID"));
									entity.setTypeName(object.getString("TypeName"));
									entity.setTitle(object.getString("Title"));
									entity.setSexID(object.getInt("SexID"));
									entity.setSexName(object.getString("SexName"));
									entity.setCostID(object.getInt("CostID"));
									entity.setCostName(object.getString("CostName"));
									entity.setReserveDate(object.getString("ReserveDate"));
									entity.setLocationName(object.getString("LocationName"));
									entity.setLocationX(object.getDouble("LocationX"));
									entity.setLocationY(object.getDouble("LocationY"));
									entity.setDescription(object.getString("Description"));
									entity.setApplyUserCount(object.getInt("ApplyUserCount"));
									entity.setReplyCount(object.getInt("ReplyCount"));
									entity.setState(object.getString("State"));
									entity.setPublisherID(object.getInt("PublisherID"));
									entity.setPublisherName(object.getString("PublisherName"));
									entity.setPublisherPhoto(object.getString("PublisherPhoto"));
									entity.setPublisherBirthday(object.getString("PublisherBirthday"));
									entity.setPublisherAge(object.getInt("PublisherAge"));
									entity.setPublisherAstro(object.getString("PublisherAstro"));
									entity.setPublishDate(object.getString("PublishDate"));
									entitys.add(entity);
								}
							} else {
								Utils.toast(PersonalDetailActivity.this, "网络状况不佳");
								cdlg.dismissDlg();
								return;
							}
							add_lv.setAdapter(new Dlg_ListView_Adapter<GetIssueInfoEntity>(entitys, PersonalDetailActivity.this));
							if (add_lv.getAdapter().getCount() == 0) {
								// Utils.toast(PersonalDetailActivity.this, "暂无邀约");
								// cdlg.dismissDlg();
								title_tv.setText("您现在没有发起中的邀约");
								ok_btn.setText("马上发布");
								cancel_btn.setText("取消");
								ok_btn.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										cdlg.dismissDlg();
										mIssueInfos = mJoocolaApplication.getIssueInfos();
										if (mIssueInfos == null || mIssueInfos.size() == 0) {
											JoocolaApplication.getInstance().initAddData(mIssueInfos, mJoocolaApplication, new InitAddInfo() {

												@Override
												public void initAddInfook() {
													showIssueDialog(mIssueInfos, bitmapCache);
												}
											});
										} else {
											showIssueDialog(mIssueInfos, bitmapCache);
										}

									}
								});
								cancel_btn.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										cdlg.dismissDlg();
									}
								});
							} else {
								// 点击选中当前被选中的邀约
								add_lv.setOnItemClickListener(new OnItemClickListener() {

									@SuppressWarnings("rawtypes")
									@Override
									public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
										((Dlg_ListView_Adapter) arg0.getAdapter()).setPos(arg2);
									}
								});
								if (add_lv.getAdapter().getCount() > 5)
									add_lv.getLayoutParams().height = Utils.dip2px(PersonalDetailActivity.this, 200);
								ok_btn.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										// 这里处理邀请加入邀约的工作
										Dlg_ListView_Adapter<GetIssueInfoEntity> adapter = ((Dlg_ListView_Adapter<GetIssueInfoEntity>) add_lv.getAdapter());
										GetIssueInfoEntity data = (GetIssueInfoEntity) adapter.getItem(adapter.getPos());
										Map<String, String> params = new HashMap<String, String>();
										params.put("appointID", data.getPID() + "");
										params.put("publisherID", JoocolaApplication.getInstance().getPID());
										params.put("inviteUserID", userId);
										getHttpResult(params, Constants.INVITE_URL, new HttpPostCallBack() {

											@Override
											public void httpPostResolveData(String result) {
												try {
													JSONObject object = new JSONObject(result);
													Utils.toast(PersonalDetailActivity.this, object.getString("Item2"));
												} catch (JSONException e) {
													e.printStackTrace();
												}

											}

											@Override
											public void onNetWorkError() {
												// TODO Auto-generated method stub

											}
										});
										cdlg.dismissDlg();
									}
								});
								cancel_btn.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										cdlg.dismissDlg();
									}
								});
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onNetWorkError() {
						// TODO Auto-generated method stub

					}
				});
			}
		});
		cdlg.showDlg();
	}

	private void processUnlike() {
		final CustomerDialog cdlg = new CustomerDialog(this, R.layout.dlg_message);
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				TextView title_tv = (TextView) window.findViewById(R.id.dlg_pe_title);
				TextView ok_btn = (TextView) window.findViewById(R.id.dlg_pe_ok);
				TextView cancel_btn = (TextView) window.findViewById(R.id.dlg_pe_cancel);
				TextView message_tv = (TextView) window.findViewById(R.id.dlg_message);
				message_tv.setText(getResources().getString(R.string.unlikeprompt));
				title_tv.setText("取消关注");
				ok_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						unlikeUser();
						cdlg.dismissDlg();
					}
				});
				cancel_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cdlg.dismissDlg();
					}
				});
			}
		});
		cdlg.showDlg();
	}

	private void processLike() {
		final CustomerDialog cdlg = new CustomerDialog(this, R.layout.dlg_message);
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				TextView title_tv = (TextView) window.findViewById(R.id.dlg_pe_title);
				TextView ok_btn = (TextView) window.findViewById(R.id.dlg_pe_ok);
				TextView cancel_btn = (TextView) window.findViewById(R.id.dlg_pe_cancel);
				TextView message_tv = (TextView) window.findViewById(R.id.dlg_message);
				message_tv.setText(getResources().getString(R.string.likeprompt));
				title_tv.setText("确认关注");
				ok_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						likeUser();
						cdlg.dismissDlg();
					}
				});
				cancel_btn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						cdlg.dismissDlg();
					}
				});
			}
		});
		cdlg.showDlg();
	}

	private void processTalk() {
		// 判断用户是否点击和自己聊天
		if (userId.equals(JoocolaApplication.getInstance().getPID())) {
			Utils.toast(this, "想要和自己聊天？直接说吧，别打字了");
			return;
		}
		// 判断用户是否有和该用户聊天的权限
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("userID1", JoocolaApplication.getInstance().getPID());
		interface1.addParams("userID2", userId);
		interface1.getData(Constants.IS_TALK_URL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result.equals("true")) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Intent intent = new Intent(PersonalDetailActivity.this, ChatActivity.class);
							intent.putExtra("userId", userId);
							intent.putExtra("isSingle", true);
							intent.putExtra("userNickName", nickName_tv.getText().toString());
							intent.putExtra("type", Constants.CHAT_TYPE_SINGLE);
							startActivity(intent);
						}
					});
				} else {
					handler.post(new Runnable() {

						@Override
						public void run() {
							// 这里说明没有相关聊天权限，将弹出聊天请求对话框
							final CustomerDialog cdlg = new CustomerDialog(PersonalDetailActivity.this, R.layout.dlg_message);
							cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

								@Override
								public void getCustomerView(Window window, AlertDialog dlg) {
									TextView tv_title = (TextView) window.findViewById(R.id.dlg_pe_title);
									TextView tv_message = (TextView) window.findViewById(R.id.dlg_message);
									TextView tv_ok = (TextView) window.findViewById(R.id.dlg_pe_ok);
									TextView tv_cancel = (TextView) window.findViewById(R.id.dlg_pe_cancel);
									tv_title.setText("权限不足");
									tv_message.setText("女性用户受特殊保护。她和你未说过话或报名过同一个活动，因此你的聊天不能发送给她。如果还想联系，可向她 发送聊天请求。 ");
									tv_ok.setText("发送邀请");
									tv_ok.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// 这里等待客户端调用接口，获取请求信息
											Map<String, String> params = new HashMap<String, String>();
											params.put("fromUserID", JoocolaApplication.getInstance().getPID());
											params.put("toUserID", userId);
											getHttpResult(params, Constants.CHAT_SEND_REQUEST_URL, new HttpPostCallBack() {

												@Override
												public void httpPostResolveData(String result) {
													if (result.equals("true"))
														Utils.toast(PersonalDetailActivity.this, "申请已发送，等待对方验证");
													else
														Utils.toast(PersonalDetailActivity.this, "申请发送失败");
													cdlg.dismissDlg();
												}

												@Override
												public void onNetWorkError() {
													// TODO Auto-generated method stub

												}
											});
										}
									});
									tv_cancel.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											cdlg.dismissDlg();
										}
									});
								}
							});
							cdlg.showDlg();
						}
					});
				}
			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});

	}

	private void showIssueDialog(final ArrayList<IssueInfo> issueInfos, final BitmapCache bitmapCache) {
		customerDialog = new CustomerDialog(PersonalDetailActivity.this, R.layout.dialog_issuedinvitation);
		customerDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.TOP);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
				GridView gridView = (GridView) dlg.findViewById(R.id.issue_gridview);
				IssueAdapter issueAdapter = new IssueAdapter(PersonalDetailActivity.this, issueInfos, bitmapCache);
				gridView.setAdapter(issueAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						Intent intent = new Intent(PersonalDetailActivity.this, IssuedinvitationActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("PID", issueInfos.get(arg2).getPID());
						bundle.putString("title", issueInfos.get(arg2).getTypeName());
						intent.putExtras(bundle);
						startActivity(intent);
						customerDialog.dismissDlg();
					}
				});

			}
		});
		customerDialog.showDlg();
	}
}

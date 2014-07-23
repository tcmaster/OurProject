package com.android.joocola.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.PC_Edit_GridView_Adapter;
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.utils.Utils;
import com.android.joocola.view.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
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
	 * 用于向主线程发送数据的handler
	 */
	private static Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_detail);
		ViewUtils.inject(this);
		userId = getIntent().getStringExtra("userId");
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
		getActionBarRight().setCompoundDrawablesWithIntrinsicBounds(null, null,
				getResources().getDrawable(R.drawable.edit), null);
		getActionBarRight().setText("");
		getActionBarTitle().setText("");
		getActionBarRight().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PersonalDetailActivity.this,
						PersonalInfoEditActivity.class);
				startActivity(intent);
			}
		});

	}

	/**
	 * 得到用户信息，并且进行展示
	 */
	private void getAndShowUserInfo() {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("UserIDs", userId);
		interface1.getData(Constans.USERINFOURL, new HttpPostCallBack() {

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
								ageAndStar_tv.setText(info.getAge() + " "
										+ info.getAstro());
								if (info.getSexName().contains("男")) {
									ageAndStar_tv.setTextColor(Color.BLUE);
									sex_iv.setImageResource(R.drawable.boy);
								} else {
									ageAndStar_tv.setTextColor(Color.RED);
									sex_iv.setImageResource(R.drawable.girl);
								}
								nickName_tv.setText(Utils.getNewStr(
										info.getNickName(), "未填写"));
								hobby_tv.setText(Utils.getNewStr(
										info.getHobbyNames(), "未填写"));
								signIn_tv.setText(Utils.getNewStr(
										info.getSignature(), "未填写"));
								phone_tv.setText(Utils.getNewStr(
										info.getPhone(), "未填写"));
								startDate_tv.setText("0");
								commitCount.setText("0");
								addCommitCount.setText("0");
								location_tv.setText(Utils.getNewStr(
										info.getNewCityName(), "未填写"));
								profession_tv.setText(Utils.getNewStr(
										info.getProfessionName(), "未填写"));
								height_tv.setText(Utils.getNewStr(
										info.getHeightName(), "未填写"));
								annualSalary_tv.setText(Utils.getNewStr(
										info.getRevenueName(), "未填写"));
								emotion_tv.setText(Utils.getNewStr(
										info.getMarryName(), "未填写"));
								smoke_tv.setText(Utils.getNewStr(
										info.getSmokeName(), "未填写"));
								drink_tv.setText(Utils.getNewStr(
										info.getDrinkName(), "未填写"));
								String[] imgUrls = info.getAlbumPhotoUrls()
										.split(",");
								pic_gv.setAdapter(new PC_Edit_GridView_Adapter(
										PersonalDetailActivity.this, false));
								for (int i = 0; i < imgUrls.length; i++) {
									if (imgUrls[i].equals(""))
										continue;
									else
										((PC_Edit_GridView_Adapter) pic_gv
												.getAdapter())
												.addImgUrls(imgUrls[i]);
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
		});
	}

	@OnItemClick(R.id.pd_myPicShow_gv)
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, WatchBigPicActivity.class);
		intent.putStringArrayListExtra("imgUrls",
				((PC_Edit_GridView_Adapter) pic_gv.getAdapter()).getImageUrls());
		intent.putExtra("position", position);
		startActivity(intent);
	}
}

package com.android.joocola.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
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
public class PersonalDetailActivity extends Activity {
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
		if (userId != null) {
			getAndShowUserInfo();
		} else {
			Utils.toast(this, "获取用户信息失败");
		}
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
								if (info.getSexName().contains("男"))
									ageAndStar_tv.setTextColor(Color.BLUE);
								else
									ageAndStar_tv.setTextColor(Color.RED);
								nickName_tv.setText(info.getNickName());
								hobby_tv.setText(info.getHobbyNames());
								signIn_tv.setText(info.getSignature());
								phone_tv.setText(info.getPhone());
								startDate_tv.setText(0);
								commitCount.setText("0");
								addCommitCount.setText("0");
								location_tv.setText(info.getNewCityName());
								profession_tv.setText(info.getProfessionName());
								annualSalary_tv.setText(info.getRevenueName());
								emotion_tv.setText(info.getMarryName());
								smoke_tv.setText(info.getSmokeName());
								drink_tv.setText(info.getDrinkName());
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

package com.android.joocola.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.joocola.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 个人资料编辑界面 这个页面用了xUtils
 * 
 * @author lixiaosong
 * 
 */
public class PersonalInfoEditActivity extends Activity {
	/**
	 * 昵称
	 */
	@ViewInject(R.id.nickName_et)
	private EditText nickName;
	/**
	 * 生日
	 */
	@ViewInject(R.id.birthday_tv)
	private TextView birthday_tv;
	/**
	 * 爱好
	 */
	@ViewInject(R.id.hobby)
	private TextView hobby_tv;
	/**
	 * 个性签名
	 */
	@ViewInject(R.id.signin)
	private TextView signin_tv;
	/**
	 * 电话
	 */
	@ViewInject(R.id.phone)
	private TextView phone_tv;
	/**
	 * 现居地
	 */
	@ViewInject(R.id.location)
	private TextView location_tv;
	/**
	 * 职业
	 */
	@ViewInject(R.id.profession)
	private TextView profession_tv;
	/**
	 * 年收入
	 */
	@ViewInject(R.id.annualSalary)
	private TextView annualSalary_tv;
	/**
	 * 身高
	 */
	@ViewInject(R.id.height)
	private TextView height_tv;
	/**
	 * 情感状态
	 */
	@ViewInject(R.id.emotion)
	private TextView emotion_tv;
	/**
	 * 是否抽烟
	 */
	@ViewInject(R.id.smoke)
	private TextView smoke_tv;
	/**
	 * 是否喝酒
	 */
	@ViewInject(R.id.drink)
	private TextView drink_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info_edit);
		ViewUtils.inject(this);
	}

	@OnClick({ R.id.hobby, R.id.signin, R.id.phone, R.id.location,
			R.id.profession, R.id.annualSalary, R.id.height, R.id.emotion,
			R.id.smoke, R.id.drink })
	public void onViewClick(View v) {

	}
}

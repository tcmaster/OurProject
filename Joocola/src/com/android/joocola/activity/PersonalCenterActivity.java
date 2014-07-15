package com.android.joocola.activity;

import com.android.joocola.R;
import com.android.joocola.R.layout;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author lixiaosong
 * @category个人中心界面暂时不知道最终结构，先用activity来写
 * 
 */
public class PersonalCenterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);
	}
}

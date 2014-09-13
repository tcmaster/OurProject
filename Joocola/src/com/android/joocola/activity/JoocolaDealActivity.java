package com.android.joocola.activity;

import android.os.Bundle;
import android.view.View;

import com.android.joocola.R;

/**
 * 用来显示聚可乐协议的activity
 * 
 * @author:LiXiaoSong
 * @see:
 * @since:
 * @copyright © joocola.com
 * @Date:2014-9-11
 */
public class JoocolaDealActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joocola_deal);
		useCustomerActionBar();
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.VISIBLE);
		getActionBarleft().setText("");
	}

}

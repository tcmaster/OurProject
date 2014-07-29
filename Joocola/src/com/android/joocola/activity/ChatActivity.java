package com.android.joocola.activity;

import android.app.Activity;
import android.os.Bundle;

import com.android.joocola.R;

/**
 * 聊天界面
 * 
 * @author lixiaosong
 * 
 */
public class ChatActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
	}
}

package com.example.huanxindemo;

import android.app.Application;

import com.example.chat.EaseMobChat;

public class HuanXinApp extends Application {
	public static HuanXinApp self;

	@Override
	public void onCreate() {
		// 聊天的初始化
		EaseMobChat.getInstance().init(this);
		self = this;
		super.onCreate();
	}

	public static HuanXinApp getSelf() {
		return self;
	}
}

package com.example.huanxindemo;

import android.app.Application;

import com.example.chat.EaseMobChat;

public class HuanXinApp extends Application {
	public static HuanXinApp self;

	@Override
	public void onCreate() {
		// ����ĳ�ʼ��
		self = this;
		EaseMobChat.getInstance().init(this);
		EaseMobChat.getInstance().beginWork();
		super.onCreate();
	}

	public static HuanXinApp getSelf() {
		return self;
	}
}

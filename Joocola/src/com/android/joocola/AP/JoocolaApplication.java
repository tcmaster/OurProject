package com.android.joocola.AP;

import android.app.Application;

public class JoocolaApplication extends Application {
	private static JoocolaApplication instance;

	@Override
	public void onCreate() {
		instance = this;
		super.onCreate();
	}

	public static JoocolaApplication getInstance() {
		return instance;
	}
}

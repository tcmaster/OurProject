package com.android.doubanmovie.provider;

public final class ProviderData {
	static final String AUTHORITIES = "com.android.doubanmovie.provider.myprovider";

	// 第一个表的各项数据
	static final String SHOWPATHALL = "show";
	static final String SHOWPATHITEM = "show/#";
	static final int MATCHSHOWALL = 1;
	static final int MATCHSHOWITEM = 2;
	static final String SHOW_TABLE_NAME = "show";
	public static final String SHOW_ALL_URL = "content://" + AUTHORITIES + "/"
			+ SHOWPATHALL;
	public static final String SHOW_ITEM_URL = "content://" + AUTHORITIES + "/"
			+ SHOWPATHITEM;

	// 第二个表的各项数据
	static final String INTRODUCEPATHALL = "introduce";
	static final String INTRODUCEITEM = "introduce/#";
	static final int MATCHINTRODUCEALL = 3;
	static final int MATCHINTRODUCEITEM = 4;
	static final String INTRODUCE_TABLE_NAME = "introduce";
	public static final String INTRODUCE_ALL_URL = "content://" + AUTHORITIES
			+ "/" + INTRODUCEPATHALL;
	public static final String INTRODUCE_ITEM_URL = "content://" + AUTHORITIES
			+ "/" + INTRODUCEITEM;
}

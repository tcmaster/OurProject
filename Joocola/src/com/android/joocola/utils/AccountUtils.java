package com.android.joocola.utils;

public class AccountUtils {
	/*
	 * 判断账户是否是邮箱或者11位数字
	 */
	public static boolean judgeAccount(String account) {

		return (account
				.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*") || account
				.matches("\\d{11}"));
	}
}

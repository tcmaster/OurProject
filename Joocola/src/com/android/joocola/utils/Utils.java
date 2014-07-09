package com.android.joocola.utils;

import java.util.regex.Pattern;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * 
 * @author lixiaosong
 * @category 常用工具集合类
 */
public class Utils {
	/*
	 * 简易版的 判断账户是否是邮箱或者11位数字 暂时没发现错误
	 * 
	 * @author lizhe
	 */
	public static boolean judgeAccount(String account) {

		return (account
				.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*") || account
				.matches("\\d{11}"));
	}

	// /**
	// * @category 判断当期字符串格式是否为电话号码或者邮箱
	// * @return 如果是为true，否则为false
	// */
	// public static boolean isPhoneNumberOrEmail(String str) {
	// return isEmail(str) || isPhoneNumber(str);
	// }
	//
	// public static boolean isEmail(String str) {
	// String strPattern =
	// "^[a-zA-Z][//w//.-]*[a-zA-Z0-9]@[a-zA-Z0-9][//w//.-]*[a-zA-Z0-9]//.[a-zA-Z][a-zA-Z//.]*[a-zA-Z]$";
	// Pattern p = Pattern.compile(strPattern);
	// Matcher m = p.matcher(str);
	// return m.matches();
	//
	// }
	//
	// public static boolean isPhoneNumber(String str) {
	// boolean isValid = false;
	// String expression = "^//(?(//d{3})//)?[- ]?(//d{3})[- ]?(//d{5})$";
	// String expression2 = "^//(?(//d{3})//)?[- ]?(//d{4})[- ]?(//d{4})$";
	// CharSequence inputStr = str;
	// /* 创建Pattern */
	// Pattern pattern = Pattern.compile(expression);
	// /* 将Pattern 以参数传入Matcher作Regular expression */
	// Matcher matcher = pattern.matcher(inputStr);
	// /* 创建Pattern2 */
	// Pattern pattern2 = Pattern.compile(expression2);
	// /* 将Pattern2 以参数传入Matcher2作Regular expression */
	// Matcher matcher2 = pattern2.matcher(inputStr);
	// if (matcher.matches() || matcher2.matches()) {
	// isValid = true;
	// }
	// return isValid;
	// }

	public static void toast(Context context, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * @param pswd
	 *            密码
	 * @category 判断密码是否符合6-20位字母或数字
	 */
	public static boolean isGoodPassword(String pswd) {
		Pattern pattern = Pattern.compile("[0-9a-zA-Z]{6,20}");
		return pattern.matcher(pswd).matches();
	}

	/**
	 * 判断该字符串是否为空
	 */
	public static boolean stringIsNullOrEmpty(String str) {
		if (str == null || str.equals(""))
			return true;
		else
			return false;
	}

	/**
	 * 
	 * 隐藏软键盘
	 */

	public static void hideSoftInputMode(Context context, View windowToken) {

		InputMethodManager imm = ((InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE));

		imm.hideSoftInputFromWindow(windowToken.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

	}

	/**
	 * 
	 * 弹出软键盘
	 */

	public static void showSoftInputMode(Context context, View windowToken) {

		final InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.showSoftInput(windowToken, InputMethodManager.SHOW_FORCED);

	}
}

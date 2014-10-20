package com.android.joocola.sms;

import java.util.List;

import android.app.Activity;
import android.database.ContentObserver;
import android.os.Handler;

/**
 * 该方法主要用于获取短信数据库变化，从而对短信息进行处理
 * 
 * @author:LiXiaoSong
 * @see:
 * @since:
 * @copyright © joocola.com
 * @Date:2014-10-20
 */
public class SmsObserver extends ContentObserver {

	private Activity activity;
	/**
	 * 所有的短信
	 */
	public static final String SMS_URI_ALL = "content://sms/";
	/**
	 * 收件箱短信
	 */
	public static final String SMS_URI_INBOX = "content://sms/inbox";
	/**
	 * 发件箱短信
	 */
	public static final String SMS_URI_SEND = "content://sms/sent";
	/**
	 * 草稿箱短信
	 */
	public static final String SMS_URI_DRAFT = "content://sms/draft";
	/**
	 * 短信的内容
	 */
	List<SmsInfo> infos;
	/**
	 * 回调，用于对获得到的短信进行处理
	 */
	private SmsProcesser callback;

	public SmsObserver(Handler handler, Activity activity, SmsProcesser smsProcesser) {
		super(handler);
		this.activity = activity;
		this.callback = smsProcesser;
	}

	@Override
	public void onChange(boolean selfChange) {
		SmsContent smscontent = new SmsContent(activity);
		infos = smscontent.getSmsInfo();
		if (infos != null && infos.size() != 0)
			callback.processSms(infos);
		super.onChange(selfChange);
	}

	public interface SmsProcesser {

		public void processSms(List<SmsInfo> infos);
	}
}

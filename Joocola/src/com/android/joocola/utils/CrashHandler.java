package com.android.joocola.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/*
 * 程序崩溃时 记录
 */
public class CrashHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";
	public static String PATH = android.os.Environment
			.getExternalStorageDirectory()  + "/"; // 返回的主要外部存储目录。
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	private static CrashHandler INSTANCE = new CrashHandler();

	private Context mContext;

	private final Map<String, String> infos = new HashMap<String, String>();

	private final DateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm-ss");

	private CrashHandler() {
	}

	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;

		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
     *
     */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ex.printStackTrace();
		infos.put("出错线程名称", thread.getName());
		if (!handleException(ex) && mDefaultHandler != null) {

			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}

			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 
	 * 
	 * @param ex
	 * @return true:
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}

		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "程序崩溃了,错误信息保存在"+PATH+"下", 1).show();
				Looper.loop();
			}
		}.start();
	 
		collectDeviceInfo(mContext);

		saveCrashInfo2File(ex);
		return true;
	}

	/**
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 
	 * 把异常信息写到sdcard里。
	 * 
	 * @param ex
	 * @return
	 */
	private String saveCrashInfo2File(Throwable ex) {
		String time = formatter.format(new Date());
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		sb.append("出错时间:" + time + "\n");

		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			// cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		String result = info.toString();
		printWriter.close();
		sb.append(result);
		// long timestamp = System.currentTimeMillis();

		String fileName = "crash-" + time + ".txt";
		File file = new File(PATH + fileName);
		Log.e("bb", PATH + fileName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			try {
				 
					file.createNewFile();
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					fileOutputStream.write(sb.toString().getBytes());
					fileOutputStream.close();
				 
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}
}
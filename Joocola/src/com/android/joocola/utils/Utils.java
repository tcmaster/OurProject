package com.android.joocola.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
	 * @category 将Uri转换为绝对地址
	 * @param contentUri
	 *            Uri内容
	 * @param context
	 *            上下文
	 * @return 绝对地址
	 */
	public static String getRealPathFromURI(Uri contentUri, Context context) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj,
				null, null, null);
		if (cursor.moveToFirst()) {
			;
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
		}
		cursor.close();
		return res;
	}

	/**
	 * @category 将bitmap转换成文件
	 * @param bm
	 *            想转换的图片
	 * @return 转换好的文件
	 */
	public static File createBitmapFile(Bitmap bm) {
		ByteArrayOutputStream bAO = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, bAO);
		File file = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DCIM).getAbsolutePath()
				+ File.separator + System.currentTimeMillis() + ".jpeg");
		FileOutputStream fOS = null;
		try {
			fOS = new FileOutputStream(file);
			fOS.write(bAO.toByteArray());
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fOS != null)
				try {
					fOS.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
}

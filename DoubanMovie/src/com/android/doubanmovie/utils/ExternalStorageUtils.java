package com.android.doubanmovie.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * 使用该类时，应该先调用init()方法一次
 * 
 * @author tc
 * @see init()
 */
public class ExternalStorageUtils {

	private static final String FILENAME = "/mypicture";

	public static boolean isMount() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	public static void init() {
		if (isMount()) {
			File file = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
							+ FILENAME);
			file.mkdir();
		}
	}

	public static String parseName(String name) {
		String imgName = null;
		if (name.lastIndexOf("/") != -1) {
			imgName = name.substring(name.lastIndexOf("/") + 1, name.length());
			return imgName;
		}
		return name;
	}

	public static boolean saveImageToExternalStorage(byte[] data,
			String fileName) {
		if (isMount()) {
			File file = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DCIM).getAbsolutePath()
					+ FILENAME + File.separator + fileName);
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(file);
				outputStream.write(data);
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return false;
		}
		return false;
	}

	public static Bitmap getImageFromExternalStorage(String imgName) {
		Bitmap result = null;
		if (isMount()) {
			File file = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DCIM).getAbsolutePath()
					+ FILENAME + File.separator + imgName);
			FileInputStream inputStream = null;
			ByteArrayOutputStream bAO = new ByteArrayOutputStream();
			try {
				inputStream = new FileInputStream(file);
				byte[] temp = new byte[1024];
				int len = 0;
				while ((len = inputStream.read(temp)) != -1) {
					bAO.write(temp, 0, len);
				}
				byte[] res = bAO.toByteArray();
				result = BitmapFactory.decodeByteArray(res, 0, res.length);
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return result;
		}
		return null;
	}

}

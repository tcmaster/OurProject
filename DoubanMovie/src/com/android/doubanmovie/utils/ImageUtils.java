package com.android.doubanmovie.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

//本类主要将图片转换成适当大小进行显示
public class ImageUtils {
	public static Bitmap getSpecifiedImage(byte[] data, int width, int height) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		options.inSampleSize = calculateSomeValue(width, height, options);
		options.inJustDecodeBounds = false;
		// options.inPurgeable = true;
		options.inInputShareable = true;
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}

	private static int calculateSomeValue(int width, int height,
			BitmapFactory.Options options) {
		int sampleValue = 1;
		if (options.outHeight > height || options.outWidth > width) {
			if (width > height) {
				sampleValue = Math.round((float) options.outHeight
						/ (float) height);
			} else {
				sampleValue = Math.round((float) options.outWidth
						/ (float) width);
			}
		}
		return sampleValue;
	}
}

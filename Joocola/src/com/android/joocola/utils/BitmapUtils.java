package com.android.joocola.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

/**
 * 专门为了处理大图片oom问题
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-9-28
 */
public class BitmapUtils {

	/**
	 * 将文件中的图片以某个比例压缩并显示
	 * 
	 * @param source
	 *            源文件
	 * @param width
	 *            想要压缩的宽度
	 * @param height
	 *            想要压缩的高度
	 * @return 压缩后的图片
	 * @author: LiXiaosong
	 * @date:2014-9-28
	 */
	public static Bitmap decodeFile(String source, int width, int height) {
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(source, options);
		options.inSampleSize = calculateInSampleSize(options, width, height);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(source, options);
	}

	/**
	 * 根据配置文件更改相关inSampleSize
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 * @see:
	 * @since:
	 * @author: LiXiaosong
	 * @date:2014-9-28
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}
}

package com.android.doubanmovie.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

//¥Ê∑≈Õº∆¨µƒ»Ìª∫¥Ê
public class MyCache {
	private static LruCache<String, Bitmap> cache;
	static {
		cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
	}

	public static void saveData(String key, Bitmap bm) {
		if (key == null || bm == null)
			return;
		cache.put(key, bm);
	}

	public static Bitmap getData(String key) {
		return cache.get(key);
	}

	public static void clearCache() {
		cache.evictAll();
	}
}

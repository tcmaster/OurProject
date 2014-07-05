package com.android.doubanmovie.httptask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.ViewGroup;

import com.android.doubanmovie.utils.ExternalStorageUtils;
import com.android.doubanmovie.utils.HttpUtils;
import com.android.doubanmovie.utils.ImageUtils;

public class GetImageTask {
	private int width, height;

	public GetImageTask(int wantwidth, int wantheight) {
		width = wantwidth;
		height = wantheight;
	}

	public void execTask(final String path, final ViewGroup parent,
			final ImageCallBack callBack, final Boolean isSave) {
		new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Void... params) {
				byte[] bmData = HttpUtils.getImgData(path);
				Bitmap bm = null;
				if (bmData != null) {
					bm = ImageUtils.getSpecifiedImage(bmData, width, height);
					if (isSave) {
						ExternalStorageUtils.saveImageToExternalStorage(bmData,
								ExternalStorageUtils.parseName(path));
					}
				}
				return bm;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				callBack.getData(path, parent, result);
			}
		}.execute();
	}

	public interface ImageCallBack {
		public void getData(String path, ViewGroup parent, Bitmap bm);
	}
}

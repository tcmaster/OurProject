package com.android.doubanmovie.myadapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.datasrc.ImageData;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;

public class MyImageAdapter extends BaseAdapter {
	List<ImageData> datas;
	Context context;
	ImageCache cache;
	RequestQueue queue;

	public MyImageAdapter(List<ImageData> datas, Context context) {
		this.datas = datas;
		this.context = context;
	}

	public void bindData(List<ImageData> datas) {
		this.datas = datas;
	}

	public void setCache(ImageCache cache, RequestQueue queue) {
		this.cache = cache;
		this.queue = queue;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imgView = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.iimagegriditem, null, false);
		}
		imgView = (ImageView) convertView.findViewById(R.id.imagegriditem);
		// datas.get(position).thumb;
		ImageLoader loader = new ImageLoader(queue, cache);
		ImageListener listener = ImageLoader.getImageListener(imgView,
				R.drawable.mainpagebackground, R.drawable.defaultcovers1);
		loader.get(datas.get(position).thumb, listener);
		return convertView;
	}

}

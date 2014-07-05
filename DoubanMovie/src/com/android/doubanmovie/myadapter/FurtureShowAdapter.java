package com.android.doubanmovie.myadapter;

import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.datasrc.ShowData;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;

public class FurtureShowAdapter extends BaseAdapter {
	ShowData data;
	Context context;
	ImageCache cache;
	RequestQueue queue;

	public FurtureShowAdapter(ShowData data, Context context, ImageCache cache,
			RequestQueue queue) {
		this.data = data;
		this.context = context;
		this.cache = cache;
		this.queue = queue;
	}

	public void bindData(ShowData data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.getSubjects().size();
	}

	@Override
	public Object getItem(int position) {
		return data.getSubjects().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.furtureshowitem, null, false);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.fshowimg);
			holder.name = (TextView) convertView
					.findViewById(R.id.fshowitemtext);
			holder.date = (TextView) convertView.findViewById(R.id.fdate);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.name.setText((String) data.getSubjects().get(position)
				.get("title"));
		holder.date.setText((String) data.getSubjects().get(position)
				.get("mainland_pubdate"));
		ImageLoader loader = new ImageLoader(queue, cache);
		ImageListener listener = ImageLoader.getImageListener(holder.img,
				R.drawable.defaultcovers, R.drawable.defaultcovers1);
		loader.get(
				((Map<String, String>) data.getSubjects().get(position)
						.get("images")).get("medium"), listener);
		return convertView;
	}

	private class ViewHolder {
		ImageView img;
		TextView name;
		TextView date;
	}

}

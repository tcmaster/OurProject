package com.android.doubanmovie.myadapter;

import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.cache.MyCache;
import com.android.doubanmovie.datasrc.ShowData;
import com.android.doubanmovie.httptask.GetImageTask;
import com.android.doubanmovie.httptask.GetImageTask.ImageCallBack;

public class MyGridAdapter extends BaseAdapter {
	ShowData data;
	Context context;

	public MyGridAdapter(ShowData data, Context context) {
		this.data = data;
		this.context = context;
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.showitem, null, false);
			holder = new ViewHolder();
			holder.showImg = (ImageView) convertView.findViewById(R.id.showimg);
			holder.showMovieText = (TextView) convertView
					.findViewById(R.id.showitemtext);
			holder.ratingBar = (RatingBar) convertView
					.findViewById(R.id.ratingBar);
			holder.ratingText = (TextView) convertView
					.findViewById(R.id.ratingtext);
			holder.ratingBar.setMax(50);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.showMovieText.setText((String) data.getSubjects().get(position)
				.get("title"));
		holder.ratingText.setText((String) ((Map<String, String>) data
				.getSubjects().get(position).get("rating")).get("average"));
		holder.ratingBar.setProgress(Integer
				.valueOf((String) ((Map<String, String>) data.getSubjects()
						.get(position).get("rating")).get("stars")));
		holder.showImg.setImageResource(R.drawable.defaultcovers1);
		final String path = ((Map<String, String>) data.getSubjects()
				.get(position).get("images")).get("small");
		Bitmap bm = MyCache.getData(path);
		if (bm != null) {
			holder.showImg.setImageBitmap(bm);
		} else {
			holder.showImg.setTag(path);
			new GetImageTask(100, 140).execTask(path, parent,
					new ImageCallBack() {

						@Override
						public void getData(String path, ViewGroup parent,
								Bitmap bm) {
							ImageView img = (ImageView) parent
									.findViewWithTag(path);
							if (img != null) {
								img.setImageBitmap(bm);
								MyCache.saveData(path, bm);
							}

						}
					}, true);
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView showImg;
		TextView showMovieText;
		RatingBar ratingBar;
		TextView ratingText;
	}

}

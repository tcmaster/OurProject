package com.android.doubanmovie.myadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.cache.MyCache;
import com.android.doubanmovie.httptask.GetImageTask;
import com.android.doubanmovie.httptask.GetImageTask.ImageCallBack;

public class MyIntroGridAdp extends BaseAdapter {
	List<Map<String, String>> writers;
	List<Map<String, String>> casts;
	List<Map<String, String>> directors;
	List<Map<String, String>> total;
	Context context;

	public MyIntroGridAdp(Context context, List<Map<String, String>> writers,
			List<Map<String, String>> casts, List<Map<String, String>> directors) {
		total = new ArrayList<Map<String, String>>();
		total.addAll(directors);
		total.addAll(writers);
		total.addAll(casts);
		this.context = context;
	}

	@Override
	public int getCount() {
		return total.size();
	}

	@Override
	public Object getItem(int position) {
		return total.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.peoplegriditem, null, false);
			holder = new ViewHolder();
			holder.peopleimg = (ImageView) convertView
					.findViewById(R.id.peopleimg);
			holder.peopleName = (TextView) convertView
					.findViewById(R.id.peopleName);
			holder.peopleEName = (TextView) convertView
					.findViewById(R.id.peopleEName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.peopleName.setText(total.get(position).get("name"));
		holder.peopleEName.setText(total.get(position).get("name_en"));
		String path = total.get(position).get("avatars");

		final int mypos = position;
		Bitmap bm = MyCache.getData(path);
		if (bm != null) {
			holder.peopleimg.setImageBitmap(bm);
		} else {
			holder.peopleimg.setImageResource(R.drawable.defaultcovers1);
			holder.peopleimg.setTag(path + position);
			new GetImageTask(110, 140).execTask(path, parent,
					new ImageCallBack() {

						@Override
						public void getData(String path, ViewGroup parent,
								Bitmap bm) {
							ImageView image = (ImageView) parent
									.findViewWithTag(path + mypos);
							if (image != null) {
								image.setImageBitmap(bm);
							}
							MyCache.saveData(path, bm);
						}
					}, true);
		}
		return convertView;
	}

	public class ViewHolder {
		TextView peopleName;
		TextView peopleEName;
		ImageView peopleimg;
	}

}

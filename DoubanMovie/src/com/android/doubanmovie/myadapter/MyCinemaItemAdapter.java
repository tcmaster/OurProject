package com.android.doubanmovie.myadapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.doubanmovie.R;
import com.android.doubanmovie.layout.MyTextView;

public class MyCinemaItemAdapter extends BaseAdapter {
	List<Map<String, String>> cinemas;
	Context context;

	public MyCinemaItemAdapter(List<Map<String, String>> cinemas,
			Context context) {
		this.cinemas = cinemas;
		this.context = context;
	}

	public void bindData(List<Map<String, String>> cinemas) {
		this.cinemas = cinemas;
	}

	@Override
	public int getCount() {
		return cinemas.size();
	}

	@Override
	public Object getItem(int position) {
		return cinemas.get(position);
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
					R.layout.cinemaitem, null, false);
			holder = new ViewHolder();
			holder.cinemaName = (MyTextView) convertView
					.findViewById(R.id.cinemaName);
			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.cinemaName.setText(cinemas.get(position).get("name"));
		String distance = (Double
				.valueOf(cinemas.get(position).get("distance")) / 1000) + "¹«Àï";
		holder.distance.setText(distance);
		return convertView;
	}

	private class ViewHolder {
		MyTextView cinemaName;
		TextView distance;
	}

}

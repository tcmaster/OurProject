package com.android.doubanmovie.myadapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.doubanmovie.R;

public class MyCDAdapter extends BaseAdapter {
	List<Map<String, String>> data;
	Context context;

	public MyCDAdapter(List<Map<String, String>> data, Context context) {
		this.data = data;
		this.context = context;
	}

	public void bindData(List<Map<String, String>> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.commitemdeep, null, false);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.cTitle);
			View view = convertView.findViewById(R.id.comment);
			holder.author = (TextView) view.findViewById(R.id.cAuthor);
			holder.rating = (RatingBar) view.findViewById(R.id.cratingbar);
			holder.summary = (TextView) view.findViewById(R.id.Connent);
			holder.useCount = (TextView) view.findViewById(R.id.cUse);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(data.get(position).get("title"));
		holder.author.setText(data.get(position).get("name"));
		holder.rating.setMax(5);
		holder.rating.setProgress(Integer.valueOf(data.get(position).get(
				"value")));
		holder.useCount.setText(data.get(position).get("useful_count") + "”–”√");
		holder.summary.setText(data.get(position).get("summary"));
		return convertView;
	}

	private class ViewHolder {
		TextView title;
		TextView author;
		RatingBar rating;
		TextView useCount;
		TextView summary;
	}

}

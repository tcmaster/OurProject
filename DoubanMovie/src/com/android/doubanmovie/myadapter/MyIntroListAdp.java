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

public class MyIntroListAdp extends BaseAdapter {
	List<Map<String, String>> popular_comments;
	Context context;

	public MyIntroListAdp(Context context,
			List<Map<String, String>> popular_comments) {
		this.popular_comments = popular_comments;
		this.context = context;
	}

	@Override
	public int getCount() {
		return popular_comments.size();
	}

	@Override
	public Object getItem(int position) {
		return popular_comments.get(position);
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
					R.layout.commentitem, null, false);
			holder = new ViewHolder();
			holder.cAuthor = (TextView) convertView.findViewById(R.id.cAuthor);
			holder.cRatingBar = (RatingBar) convertView
					.findViewById(R.id.cratingbar);
			holder.cUse = (TextView) convertView.findViewById(R.id.cUse);
			holder.cContent = (TextView) convertView.findViewById(R.id.Connent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.cAuthor.setText(popular_comments.get(position).get("name"));
		holder.cRatingBar.setMax(5);
		holder.cRatingBar.setProgress(Integer.valueOf(popular_comments.get(
				position).get("value")));
		holder.cUse.setText(popular_comments.get(position).get("useful_count")
				+ "”–”√");
		holder.cContent.setText(popular_comments.get(position).get("content"));
		return convertView;
	}

	private class ViewHolder {
		TextView cAuthor;
		RatingBar cRatingBar;
		TextView cUse;
		TextView cContent;
	}
}

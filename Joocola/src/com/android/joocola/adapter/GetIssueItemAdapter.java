package com.android.joocola.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.GetIssueInfoEntity;


public class GetIssueItemAdapter extends BaseAdapter {
	private List<GetIssueInfoEntity> infos;
	private Context mContext;
	private LayoutInflater inflater;
	private ViewHolder holder;
	public GetIssueItemAdapter(List<GetIssueInfoEntity> info, Context context) {
		this.infos = info;
		this.mContext = context;
		inflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return infos.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView =inflater.inflate(
R.layout.getissueitem, null);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// holder.title.setText(infos.get(position).getTitle());
		return convertView;
	}

	class ViewHolder {
		TextView title;
	}
}

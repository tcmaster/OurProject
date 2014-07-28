package com.android.joocola.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;

public class Fg_Chat_List_Adapter extends BaseAdapter {
	private LayoutInflater mInflater = null;

	public Fg_Chat_List_Adapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 5;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_list_message_fg,
					null, false);
			holder = new ViewHolder();
			holder.tv_nickName = (TextView) convertView
					.findViewById(R.id.ml_nickName_tv);
			holder.tv_date = (TextView) convertView
					.findViewById(R.id.ml_date_tv);
			holder.iv_photo = (ImageView) convertView.findViewById(R.id.ml_iv);
			holder.tv_message = (TextView) convertView
					.findViewById(R.id.ml_chatInfo_tv);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		return convertView;
	}

	private class ViewHolder {
		TextView tv_nickName;
		ImageView iv_photo;
		TextView tv_date;
		TextView tv_message;
	}

}

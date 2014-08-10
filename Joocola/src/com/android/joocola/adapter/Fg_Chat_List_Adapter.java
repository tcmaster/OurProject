package com.android.joocola.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.ChatOfflineInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

public class Fg_Chat_List_Adapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	private String[] data;
	private DbUtils db;

	public Fg_Chat_List_Adapter(Context context, String[] data) {
		mInflater = LayoutInflater.from(context);
		db = JoocolaApplication.getInstance().getDB();
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.length;
	}

	public void bindData(String[] datas) {
		this.data = datas;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return data[position];
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
			holder.iv_redPoint = (ImageView) convertView
					.findViewById(R.id.redPoint);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.tv_nickName.setText(data[position].split("-")[1]);
		String message = haveNoReadAndShow(data[position]);
		if (message == null) {
			holder.iv_redPoint.setVisibility(View.INVISIBLE);
			holder.tv_message.setText(getReadData(data[position]));
		} else {
			holder.iv_redPoint.setVisibility(View.VISIBLE);
			holder.tv_message.setText(message);
		}
		return convertView;
	}

	private String haveNoReadAndShow(String key) {
		try {
			List<ChatOfflineInfo> infos = db.findAll(Selector
					.from(ChatOfflineInfo.class)
					.where("isRead", "=", "0")
					.and("key", "=", key)
					.and("user",
							"=",
							JoocolaApplication.getInstance().getUserInfo()
									.getUserName()));
			if (infos == null || infos.size() == 0)
				return null;
			return infos.get(infos.size() - 1).getContent();
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getReadData(String key) {
		try {
			List<ChatOfflineInfo> infos = db.findAll(Selector
					.from(ChatOfflineInfo.class)
					.where("isRead", "=", "1")
					.and("key", "=", key)
					.and("user",
							"=",
							JoocolaApplication.getInstance().getUserInfo()
									.getUserName()));
			if (infos == null || infos.size() == 0)
				return "";
			return infos.get(infos.size() - 1).getContent();
		} catch (DbException e) {
			e.printStackTrace();
		}
		return "";
	}

	private class ViewHolder {
		TextView tv_nickName;
		ImageView iv_photo;
		TextView tv_date;
		TextView tv_message;
		ImageView iv_redPoint;
	}

}

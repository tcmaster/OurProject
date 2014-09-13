package com.android.joocola.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.ChatOfflineInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

public class Fg_Chat_List_Adapter extends BaseAdapter {
	private LayoutInflater mInflater = null;
	private String[] data;
	private DbUtils db;
	private Map<String, String> photos;
	private Map<String, String> names;
	private BitmapUtils utils;

	public Fg_Chat_List_Adapter(Context context, String[] data) {
		mInflater = LayoutInflater.from(context);
		db = JoocolaApplication.getInstance().getDB();
		photos = new HashMap<String, String>();
		names = new HashMap<String, String>();
		utils = new BitmapUtils(context);
		utils.configDefaultLoadFailedImage(context.getResources().getDrawable(
				R.drawable.logo));
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

	public void addName(String key, String value) {
		names.put(key, value);
		notifyDataSetChanged();
	}

	public void addPhotos(String key, String value) {
		photos.put(key, value);
		notifyDataSetChanged();
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
		if (names.get(data[position].split("-")[1]) != null)
			holder.tv_nickName.setText(names.get(data[position].split("-")[1]));
		else
			holder.tv_nickName.setText(data[position].split("-")[1]);
		String imgUrl = photos.get(data[position].split("-")[1]) != null ? Utils
				.processResultStr(
						Constans.URL + photos.get(data[position].split("-")[1]),
						"_150_")
				: "";
		Log.v("lixiaosong", imgUrl);
		utils.display(holder.iv_photo, imgUrl);
		ChatOfflineInfo noReadInfo = haveNoReadAndShow(data[position]);
		if (noReadInfo == null) {
			ChatOfflineInfo readInfo = getReadData(data[position]);
			holder.iv_redPoint.setVisibility(View.INVISIBLE);
			holder.tv_message.setText(readInfo.getContent());
			holder.tv_date.setText(readInfo.getTime());
		} else {
			holder.iv_redPoint.setVisibility(View.VISIBLE);
			holder.tv_message.setText(noReadInfo.getContent());
			holder.tv_date.setText(noReadInfo.getTime());
		}
		return convertView;
	}

	private ChatOfflineInfo haveNoReadAndShow(String key) {
		try {
			List<ChatOfflineInfo> infos = db.findAll(Selector
					.from(ChatOfflineInfo.class)
					.where("isRead", "=", "0")
					.and("key", "=", key)
					.and("user",
							"=",
							"u"
									+ JoocolaApplication.getInstance()
											.getUserInfo().getPID()));
			if (infos == null || infos.size() == 0)
				return null;
			return infos.get(infos.size() - 1);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ChatOfflineInfo getReadData(String key) {
		try {
			List<ChatOfflineInfo> infos = db.findAll(Selector
					.from(ChatOfflineInfo.class)
					.where("isRead", "=", "1")
					.and("key", "=", key)
					.and("user",
							"=",
							"u"
									+ JoocolaApplication.getInstance()
											.getUserInfo().getPID()));
			if (infos == null || infos.size() == 0)
				return null;
			return infos.get(infos.size() - 1);
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class ViewHolder {
		TextView tv_nickName;
		ImageView iv_photo;
		TextView tv_date;
		TextView tv_message;
		ImageView iv_redPoint;
	}

}

package com.android.joocola.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.activity.PersonalDetailActivity;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.chat.XMPPChat;
import com.android.joocola.entity.MultiChatInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * 多人聊天会话适配器
 * 
 * @author lixiaosong
 * 
 */
public class MultiChatAdapter extends BaseAdapter {
	private Context context;
	private DbUtils db;
	/**
	 * 用户头像存放地址，key为用户名，values为地址
	 */
	private Map<String, String> photos;
	/**
	 * 用户昵称存放地址，key为用户名，values为地址
	 */
	private Map<String, String> names;
	/**
	 * 未读消息
	 */
	private List<MultiChatInfo> noReadData;
	/**
	 * 历史消息
	 */
	private List<MultiChatInfo> readData;
	/**
	 * 需要显示的内容
	 */
	private List<MultiChatInfo> data;
	/**
	 * 这个字段代表用户在那组会话中
	 */
	private String key;
	/**
	 * 图像下载工具
	 */
	private BitmapUtils bmUtils;

	public MultiChatAdapter(String key, Context context) {
		this.context = context;
		db = JoocolaApplication.getInstance().getDB();
		this.key = key;
		photos = new HashMap<String, String>();
		names = new HashMap<String, String>();
		bmUtils = new BitmapUtils(context);
		bmUtils.configDefaultLoadFailedImage(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.logo));
		initData();
	}

	public void initData() {
		// 下面三句防止报空
		data = new ArrayList<MultiChatInfo>();
		noReadData = new ArrayList<MultiChatInfo>();
		readData = new ArrayList<MultiChatInfo>();
		noReadData = getNoReadData();
		data.addAll(readData);
		data.addAll(noReadData);
	}

	/**
	 * 得到当前未读的消息
	 */
	public List<MultiChatInfo> getNoReadData() {
		try {
			List<MultiChatInfo> noReadData = db.findAll(Selector
					.from(MultiChatInfo.class)
					.where("multikey", "=", key)
					.and("isRead", "=", 0)
					.and("user",
							"=",
							"u"
									+ JoocolaApplication.getInstance()
											.getUserInfo().getPID()));
			if (noReadData == null)
				noReadData = new ArrayList<MultiChatInfo>();
			return noReadData;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return new ArrayList<MultiChatInfo>();
	}

	/**
	 * 得到当前已读的消息
	 */
	public List<MultiChatInfo> getReadData() {
		try {
			List<MultiChatInfo> ReadData = db.findAll(Selector
					.from(MultiChatInfo.class)
					.where("multikey", "=", key)
					.and("isRead", "=", 1)
					.and("user",
							"=",
							"u"
									+ JoocolaApplication.getInstance()
											.getUserInfo().getPID()));
			if (ReadData == null)
				ReadData = new ArrayList<MultiChatInfo>();
			return ReadData;
		} catch (DbException e) {
			e.printStackTrace();
		}

		return new ArrayList<MultiChatInfo>();
	}

	/**
	 * 当有新的信息显示时，调用该方法
	 */
	public void showHistory() {
		saveHistory();
		this.readData = getReadData();
		data.clear();
		data.addAll(readData);
		notifyDataSetChanged();
	}

	/**
	 * 隐藏历史信息
	 */
	public void hideHistory() {
		this.noReadData = getNoReadData();
		data.clear();
		data.addAll(noReadData);
		notifyDataSetChanged();
	}

	public void saveHistory() {
		// 到这里说明即将显示未读信息，未读信息变为已读信息
		for (int i = 0; i < noReadData.size(); i++)
			noReadData.get(i).setIsRead(1);
		try {
			db.updateAll(noReadData, "isRead");
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到当前未读的消息
	 */
	public void updateNoReadData() {
		this.noReadData = getNoReadData();
		data.clear();
		data.addAll(noReadData);
		notifyDataSetChanged();
	}

	/**
	 * 用户头像获取
	 * 
	 * @param key
	 * @param value
	 */
	public void addPhotos(String key, String value) {
		photos.put(key, value);
		notifyDataSetChanged();
	}

	public void addNames(String key, String value) {
		names.put(key, value);
		notifyDataSetChanged();
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
		MultiChatInfo info = data.get(position);
		ViewHolder holder = null;
		boolean flag = info.getIsFrom().equals(
				XMPPChat.getInstance().getConnection().getUser().split("@")[0]) ? true
				: false;
		if (convertView == null) {
			holder = new ViewHolder();
			if (flag)
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_chat_me, null, false);
			else
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_chat_other, null, false);
			holder.flag = flag;
			holder.iv_photo = (ImageView) convertView
					.findViewById(R.id.chat_photo);
			holder.tv_content = (TextView) convertView
					.findViewById(R.id.chat_content);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.chat_name);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.chat_time);
			holder.iv_getImg = (ImageView) convertView
					.findViewById(R.id.chat_img);
			holder.fl = (FrameLayout) convertView
					.findViewById(R.id.chat_img_container);
			convertView.setTag(R.id.viewholder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.viewholder);
			if (holder.flag == flag) {
			} else {
				holder = new ViewHolder();
				if (flag)
					convertView = LayoutInflater.from(context).inflate(
							R.layout.item_chat_me, null, false);
				else
					convertView = LayoutInflater.from(context).inflate(
							R.layout.item_chat_other, null, false);
				holder.flag = flag;
				holder.iv_photo = (ImageView) convertView
						.findViewById(R.id.chat_photo);
				holder.tv_content = (TextView) convertView
						.findViewById(R.id.chat_content);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.chat_name);
				holder.tv_time = (TextView) convertView
						.findViewById(R.id.chat_time);
				holder.iv_getImg = (ImageView) convertView
						.findViewById(R.id.chat_img);
				holder.fl = (FrameLayout) convertView
						.findViewById(R.id.chat_img_container);
				convertView.setTag(R.id.viewholder, holder);
			}
		}
		String imgUrls = photos.get(info.getIsFrom());
		if (imgUrls == null)
			imgUrls = "";
		bmUtils.display(holder.iv_photo,
				Utils.processResultStr(Constans.URL + imgUrls, "_150_"));
		// 时间需进行处理，如果两次间隔5分钟以内，则无需示时间
		if (position == 0) {
			// 第一个语句必须显示时间
			holder.tv_time.setVisibility(View.VISIBLE);
			holder.tv_time.setText(info.getTime());
		} else {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
				Date nowDate = format.parse(data.get(position).getTime());
				Date lastDate = format.parse(data.get(position - 1).getTime());
				long second = Math
						.abs((lastDate.getTime() - nowDate.getTime()) / 1000);
				if (second > 300) {
					// 两次间隔大于300秒
					holder.tv_time.setVisibility(View.VISIBLE);
					holder.tv_time.setText(info.getTime());
				} else {
					// 两次间隔小于300秒，无需显示时间
					holder.tv_time.setVisibility(View.INVISIBLE);
					holder.tv_time.setText("");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		holder.tv_name.setVisibility(View.VISIBLE);
		if (names.get(info.getIsFrom()) != null)
			holder.tv_name.setText(names.get(info.getIsFrom()));
		else
			holder.tv_name.setText(info.getIsFrom());
		if (!info.getImgUrl().equals("")) {

			holder.fl.setVisibility(View.VISIBLE);
			holder.tv_content.setVisibility(View.INVISIBLE);
			holder.iv_getImg.setVisibility(View.VISIBLE);
			bmUtils.display(holder.iv_getImg, info.getImgUrl());
		} else {
			holder.tv_content.setText(info.getContent());
		}
		final String userId = info.getIsFrom().split("@")[0].substring(1);
		holder.iv_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						PersonalDetailActivity.class);
				intent.putExtra("userId", userId);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	private class ViewHolder {
		// 这个flag代表该视图发起的对象是“我”，还是“其他人”，如果是“我”，为true，其他人为false
		boolean flag;
		TextView tv_name;
		ImageView iv_photo;
		TextView tv_time;
		TextView tv_content;
		ImageView iv_getImg;
		FrameLayout fl;
	}

}

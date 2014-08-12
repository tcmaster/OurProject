package com.android.joocola.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.chat.XMPPChat;
import com.android.joocola.entity.ChatOfflineInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

/**
 * 用于更新得到消息的视图信息
 * 
 * @author lixiaosong
 * 
 */
public class SingleChatAdapter extends BaseAdapter {
	private LinearLayout container;
	private Context context;
	private DbUtils db;
	/**
	 * 用户头像存放地址，key为用户名，values为地址
	 */
	private Map<String, String> photos;
	/**
	 * 未读消息
	 */
	private List<ChatOfflineInfo> noReadData;
	/**
	 * 历史消息
	 */
	private List<ChatOfflineInfo> readData;
	/**
	 * 需要显示的内容
	 */
	private List<ChatOfflineInfo> data;
	/**
	 * 这个字段代表用户在和谁聊天
	 */
	private String user;

	/**
	 * 图像下载工具
	 */
	private BitmapUtils bmUtils;

	public SingleChatAdapter(Context context, String user) {
		this.context = context;
		db = JoocolaApplication.getInstance().getDB();
		this.user = user;
		photos = new HashMap<String, String>();
		bmUtils = new BitmapUtils(context);
		bmUtils.configDefaultLoadFailedImage(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_launcher));
		initData();
	}

	public void initData() {
		// 下面三句防止报空
		data = new ArrayList<ChatOfflineInfo>();
		noReadData = new ArrayList<ChatOfflineInfo>();
		readData = new ArrayList<ChatOfflineInfo>();
		noReadData = getNoReadData();
		data.addAll(readData);
		data.addAll(noReadData);
	}

	/**
	 * 得到当前未读的消息
	 */
	public List<ChatOfflineInfo> getNoReadData() {
		try {
			List<ChatOfflineInfo> noReadData = db.findAll(Selector
					.from(ChatOfflineInfo.class)
					.where("key",
							"=",
							XMPPChat.getInstance().getConnection().getUser()
									.split("@")[0]
									+ "-" + user)
					.and("isRead", "=", 0)
					.and("user",
							"=",
							JoocolaApplication.getInstance().getUserInfo()
									.getUserName()));
			if (noReadData == null)
				noReadData = new ArrayList<ChatOfflineInfo>();
			return noReadData;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return new ArrayList<ChatOfflineInfo>();
	}

	/**
	 * 得到当前已读的消息
	 */
	public List<ChatOfflineInfo> getReadData() {
		try {
			List<ChatOfflineInfo> ReadData = db.findAll(Selector
					.from(ChatOfflineInfo.class)
					.where("key",
							"=",
							XMPPChat.getInstance().getConnection().getUser()
									.split("@")[0]
									+ "-" + user)
					.and("isRead", "=", 1)
					.and("user",
							"=",
							JoocolaApplication.getInstance().getUserInfo()
									.getUserName()));
			if (ReadData == null)
				ReadData = new ArrayList<ChatOfflineInfo>();
			return ReadData;
		} catch (DbException e) {
			e.printStackTrace();
		}

		return new ArrayList<ChatOfflineInfo>();
	}

	public void addPhotos(String key, String value) {
		photos.put(key, value);
		notifyDataSetChanged();
	}

	/**
	 * 当有新的信息显示时，调用该方法
	 */
	public void showHistory() {
		saveHistory();
		this.readData = getReadData();
		data.clear();
		data.addAll(readData);
		data.addAll(noReadData);
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

	public void updateNoReadData() {
		this.noReadData = getNoReadData();
		data.clear();
		data.addAll(noReadData);
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
		/**
		 * 由于并不知道是什么样的布局，所以每次都必须从新得到,不可布局重用
		 */
		ChatOfflineInfo info = data.get(position);
		if (info.getIsFrom().equals(
				XMPPChat.getInstance().getConnection().getUser().split("@")[0])) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_chat_me, null, false);
		} else {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_chat_other, null, false);
		}
		TextView name = (TextView) convertView.findViewById(R.id.chat_name);
		ImageView photo = (ImageView) convertView.findViewById(R.id.chat_photo);
		String imgUrls = photos.get(info.getIsFrom());
		if (imgUrls == null)
			imgUrls = "";
		bmUtils.display(photo,
				Utils.processResultStr(Constans.URL + imgUrls, "_150_"));
		TextView content = (TextView) convertView
				.findViewById(R.id.chat_content);
		name.setText(info.getIsFrom());
		content.setText(info.getContent());
		return convertView;
	}
}

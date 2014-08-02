package com.android.joocola.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
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
	 * 未读消息
	 */
	List<ChatOfflineInfo> noReadData;
	/**
	 * 历史消息
	 */
	List<ChatOfflineInfo> readData;
	/**
	 * 需要显示的内容
	 */
	List<ChatOfflineInfo> data;
	/**
	 * 这个字段代表用户在和谁聊天
	 */
	private String from;

	/**
	 * 这个字段判断当前是否需要历史记录
	 */

	public SingleChatAdapter(Context context, String from) {
		this.context = context;
		db = JoocolaApplication.getInstance().getDB();
		this.from = from;
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
			List<ChatOfflineInfo> noreadData = db.findAll(Selector
					.from(ChatOfflineInfo.class)
					.where(WhereBuilder.b("isFrom", "=", from).or("isTo", "=",
							from)).and("isRead", "=", "0"));
			if (noreadData == null)
				return new ArrayList<ChatOfflineInfo>();
			else
				return noreadData;
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
			List<ChatOfflineInfo> readData = db.findAll(Selector
					.from(ChatOfflineInfo.class)
					.where(WhereBuilder.b("isFrom", "=", from).or("isTo", "=",
							from)).and("isRead", "=", "1"));
			if (readData == null)
				return new ArrayList<ChatOfflineInfo>();
			else
				return readData;
		} catch (DbException e) {
			e.printStackTrace();
		}
		return new ArrayList<ChatOfflineInfo>();
	}

	/**
	 * 当有新的信息显示时，调用该方法
	 */
	public void updateReadData() {
		this.readData = getReadData();
		data.clear();
		data.addAll(readData);
		data.addAll(noReadData);
		notifyDataSetChanged();
	}

	public void updateNoReadData() {
		this.noReadData = getNoReadData();
		data.clear();
		data.addAll(readData);
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
		TextView content = (TextView) convertView
				.findViewById(R.id.chat_content);
		name.setText(info.getIsFrom());
		content.setText(info.getContent());
		return convertView;
	}
}
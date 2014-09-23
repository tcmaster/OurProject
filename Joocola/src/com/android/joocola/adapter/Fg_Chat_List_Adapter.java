package com.android.joocola.adapter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.MyChatInfo;
import com.android.joocola.utils.Utils;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.TextMessageBody;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;

/**
 * 消息列表界面列表的适配器
 * 
 * @author:LiXiaoSong
 * @see: {@link MessageFragment}
 * @since:
 * @copyright © joocola.com
 * @Date:2014-9-23
 */
public class Fg_Chat_List_Adapter extends BaseAdapter {

	private LayoutInflater mInflater = null;
	// 消息列表的数据源
	private List<MyChatInfo> data;
	// 数据库
	private DbUtils db;
	// 列表界面显示的图片
	private Map<String, String> photos;
	// 列表界面显示的用户昵称
	private Map<String, String> names;
	// 下载图片的工具类
	private BitmapUtils utils;

	public Fg_Chat_List_Adapter(Context context, List<MyChatInfo> data) {
		mInflater = LayoutInflater.from(context);
		db = JoocolaApplication.getInstance().getDB();
		photos = new HashMap<String, String>();
		names = new HashMap<String, String>();
		utils = new BitmapUtils(context);
		utils.configDefaultLoadFailedImage(context.getResources().getDrawable(R.drawable.logo));
		this.data = data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	public void bindData(List<MyChatInfo> datas) {
		this.data = datas;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
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
		// 得到当前的用户
		String user = data.get(position).getUser();
		// 得到当前用户的会话
		EMConversation conversation = EMChatManager.getInstance().getConversation(user);
		// 得到当前用户会话的最后一条消息
		EMMessage lastMessage = conversation.getLastMessage();
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_list_message_fg, null, false);
			holder = new ViewHolder();
			holder.tv_nickName = (TextView) convertView.findViewById(R.id.ml_nickName_tv);
			holder.tv_date = (TextView) convertView.findViewById(R.id.ml_date_tv);
			holder.iv_photo = (ImageView) convertView.findViewById(R.id.ml_iv);
			holder.tv_message = (TextView) convertView.findViewById(R.id.ml_chatInfo_tv);
			holder.iv_redPoint = (ImageView) convertView.findViewById(R.id.redPoint);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		holder.tv_nickName.setText(names.get(user));
		holder.tv_date.setText(Utils.formatDate(new Date(lastMessage.getMsgTime())));
		utils.display(holder.iv_photo, photos.get(user));
		// 看类型
		if (lastMessage.getType() == Type.TXT) {
			holder.tv_message.setText(((TextMessageBody) lastMessage.getBody()).getMessage());
		} else if (lastMessage.getType() == Type.IMAGE) {
			holder.tv_message.setText("[图片文件]");
		}
		return convertView;
	}

	private class ViewHolder {

		// 用户昵称
		TextView tv_nickName;
		// 用户头像
		ImageView iv_photo;
		// 最后消息的时间
		TextView tv_date;
		// 最后消息的内容
		TextView tv_message;
		// 用于显示是否有未读消息
		ImageView iv_redPoint;
	}

}

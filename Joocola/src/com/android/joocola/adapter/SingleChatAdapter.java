package com.android.joocola.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.activity.BigPicActivity;
import com.android.joocola.activity.PersonalDetailActivity;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.Utils;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.util.LogUtils;

/**
 * 单人聊天的适配器
 * 
 * @author lixiaosong
 * 
 */
public class SingleChatAdapter extends BaseAdapter {

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
	 * 这个字段代表用户在和谁聊天
	 */
	private String user;

	/**
	 * 图像下载工具
	 */
	private BitmapUtils bmUtils;
	/**
	 * 数据源，用于存放当前需要显示的消息
	 */
	private List<EMMessage> data;
	/**
	 * 本界面的会话，根据这个会话得到聊天数据
	 */
	private EMConversation conversation;
	/**
	 * 在消息内存中最早的一条消息msgId
	 * 
	 * @describe messageInMemory
	 */
	private EMMessage messageInM;
	/**
	 * 当前页面最早的一条消息
	 * 
	 * @describe messageInCurrentPage
	 */
	private EMMessage messageInCP;
	/**
	 * 当前的剩余未展示历史消息数量
	 */
	private int currentCount;
	/**
	 * 当前已展示的页面
	 */
	private int page;
	/**
	 * 填充器
	 */
	private LayoutInflater mInflater;
	/**
	 * 本界面的TAG标志
	 */
	private final String TAG = "SingleChatAdapter";
	public boolean DEBUG = true;

	public SingleChatAdapter(Context context, String user) {
		this.context = context;
		db = JoocolaApplication.getInstance().getDB();
		this.user = user;
		photos = new HashMap<String, String>();
		names = new HashMap<String, String>();
		mInflater = LayoutInflater.from(context);
		bmUtils = new BitmapUtils(context);
		bmUtils.configDefaultLoadingImage(R.drawable.logo);
		bmUtils.configDefaultLoadFailedImage(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo));
		initData();
	}

	public void initData() {
		data = new ArrayList<EMMessage>();
		page = 1;
		currentCount = 0;
		// test2仅作为测试使用
		conversation = EMChatManager.getInstance().getConversation(user);
		// 这里的逻辑，先从内存中取消息记录，没有的话从数据库中取
		List<EMMessage> temp = conversation.getAllMessages();
		if (temp == null || temp.size() == 0) {// 到这里说明没有消息记录，为最初时的状态

		} else {
			data.addAll(temp);
			messageInM = data.get(0);
			// newestMessage = data.get(data.size() - 1);
		}
	}

	/**
	 * 更新当前最后一条消息，根据这条消息查找消息记录
	 * 
	 * @author: LiXiaoSong
	 * @date:2014-9-19
	 */
	private void updateLast(String msgId) {
		// newestMessage = conversation.getMessage(msgId);
	}

	/**
	 * 以最新的一条消息更新最后一条消息
	 */
	public void updateLast() {
		// newestMessage = conversation.getLastMessage();
	}

	/**
	 * 将当前显示的数据更新
	 * 
	 * @author: LiXiaoSong
	 * @date:2014-9-19
	 */
	public void updateChatList() {
		data.clear();
		data.addAll(conversation.getAllMessages());
		messageInM = data.get(0);
		notifyDataSetChanged();
	}

	/**
	 * 清除当前聊天列表的信息
	 * 
	 * @author: LiXiaoSong
	 * @date:2014-9-19
	 */
	public void clearChatList() {
		data.clear();
		notifyDataSetChanged();
	}

	/**
	 * 将当前最新的消息重置
	 * 
	 * @author: LiXiaoSong
	 * @date:2014-9-19
	 */
	public void resetLast() {
		// newestMessage = conversation.getLastMessage();
	}

	/**
	 * 得到更早的历史记录
	 * 
	 * @author: LiXiaosong
	 * @date:2014-9-23
	 */
	public int getHistory() {
		if (messageInCP == null || messageInM == null || messageInCP.getMsgId() == null || messageInM.getMsgId() == null) {
			Utils.toast(context, "没有更多历史消息了");
		}
		if (messageInCP.getMsgId().equals(messageInM.getMsgId())) {// 当前的页面已经将内存中所有的消息展示了
			List<EMMessage> msg = conversation.loadMoreMsgFromDB(messageInM.getMsgId(), 40);
			if (msg == null || msg.size() == 0) {
				Utils.toast(context, "没有更多历史消息了");
				return 0;
			}
			// 增加新的数据
			data = conversation.getAllMessages();
			messageInM = data.get(0);
		}
		page++;
		notifyDataSetChanged();
		return data.size() - currentCount;
	}

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
		if (data.size() > 15 * page) {
			currentCount = data.size() - 15 * page;
			messageInCP = data.get(currentCount + 0);
			return 15 * page;
		} else if (data.size() <= 15 * page) {
			currentCount = 0;
			if (data.size() != 0)
				messageInCP = data.get(0);
			return data.size();
		} else {
			// 还能有这情况？
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		return data.get(position + currentCount);
	}

	@Override
	public long getItemId(int position) {
		return position + currentCount;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final EMMessage message = data.get(position + currentCount);
		if (DEBUG) {
			LogUtils.e(message.getFrom() + " is from " + message.getTo() + " is to " + message.getBody() + " is body");
			LogUtils.e(conversation.getUserName());
		}
		// ***************************系统消息判断逻辑************************************//
		// 判断是否为系统消息
		if (message.getBooleanAttribute("isSystem", false)) {
			convertView = mInflater.inflate(R.layout.item_system_info_prompt, null, false);
			// 有这个tag代表该消息是系统消息
			String systemTag = "系统消息标志";
			convertView.setTag(systemTag);
			TextView content = (TextView) convertView.findViewById(R.id.system_msg_info);
			TextView time = (TextView) convertView.findViewById(R.id.system_msg_time);
			if (message.getBooleanAttribute("isBegin", false)) {
				if (("u" + JoocolaApplication.getInstance().getPID()).equals(message.getFrom())) {
					content.setText(((TextMessageBody) message.getBody()).getMessage());
				} else {
					content.setText("你已经通过了他的验证请求，开始聊天吧");
				}

				time.setText(new Date(message.getMsgTime()).toLocaleString());
			}
			return convertView;
		}
		if (convertView != null && convertView.getTag() instanceof String) {
			// 消除系统消息的View
			convertView = null;
		}
		// ***************************系统消息判断逻辑end************************************//
		boolean flag = ("u" + JoocolaApplication.getInstance().getPID()).equals(message.getFrom()) ? true : false;

		if (convertView == null) {
			holder = new ViewHolder();
			if (flag)
				convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_me, null, false);
			else
				convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_other, null, false);
			holder.flag = flag;
			holder.iv_photo = (ImageView) convertView.findViewById(R.id.chat_photo);
			holder.tv_content = (TextView) convertView.findViewById(R.id.chat_content);
			holder.tv_name = (TextView) convertView.findViewById(R.id.chat_name);
			holder.tv_time = (TextView) convertView.findViewById(R.id.chat_time);
			holder.iv_getImg = (ImageView) convertView.findViewById(R.id.chat_img);
			holder.fl = (FrameLayout) convertView.findViewById(R.id.chat_img_container);
			convertView.setTag(R.id.viewholder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.viewholder);
			if (holder.flag == flag) {
			} else {
				holder = new ViewHolder();
				if (flag)
					convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_me, null, false);
				else
					convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_other, null, false);
				holder.flag = flag;
				holder.iv_photo = (ImageView) convertView.findViewById(R.id.chat_photo);
				holder.tv_content = (TextView) convertView.findViewById(R.id.chat_content);
				holder.tv_name = (TextView) convertView.findViewById(R.id.chat_name);
				holder.tv_time = (TextView) convertView.findViewById(R.id.chat_time);
				holder.iv_getImg = (ImageView) convertView.findViewById(R.id.chat_img);
				holder.fl = (FrameLayout) convertView.findViewById(R.id.chat_img_container);
				convertView.setTag(R.id.viewholder, holder);
			}
		}
		// 这里要做一个处理，将一条消息的View恢复到默认的状态
		holder.tv_content.setVisibility(View.VISIBLE);
		holder.fl.setVisibility(View.GONE);
		holder.tv_name.setVisibility(View.GONE);
		holder.tv_time.setVisibility(View.INVISIBLE);
		if (photos.get(message.getFrom()) != null) {
			String url = Constants.URL + Utils.processResultStr(photos.get(message.getFrom()), "_150_");
			LogUtils.e(url);
			bmUtils.display(holder.iv_photo, url);
		}
		// 显示时间的逻辑：
		// 1.当前显示的第一条，必定显示时间
		// 2.两条消息间隔5分钟以内，第二条不必显示时间
		// 3.两条消息间隔5分钟以上，第二条需要显示时间
		if (position == 0) {// 如果是首条的情况
			holder.tv_time.setVisibility(View.VISIBLE);
			holder.tv_time.setText(new Date(message.getMsgTime()).toLocaleString());
		} else {// 非首条的情况，需要判断时间间隔，确定本条时间是否显示
			long beforeTime = data.get(position - 1 + currentCount).getMsgTime();
			long afterTime = message.getMsgTime();
			if ((afterTime - beforeTime) / 1000 > 300) {// 进入这里说明两条消息间隔超过5分钟,需要显示时间
				holder.tv_time.setVisibility(View.VISIBLE);
				holder.tv_time.setText(new Date(message.getMsgTime()).toLocaleString());
			}
		}
		if (message.getChatType() == ChatType.GroupChat) {// 群组聊天的话，需要将用户的名字显示出来
			holder.tv_name.setVisibility(View.VISIBLE);
			holder.tv_name.setText(names.get(message.getFrom()));
		}
		if (message.getType() == Type.TXT) {
			holder.tv_content.setText(((TextMessageBody) message.getBody()).getMessage());

		} else if (message.getType() == Type.IMAGE) {
			holder.fl.setVisibility(View.VISIBLE);
			holder.tv_content.setVisibility(View.GONE);
			ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
			String remoteUrl = imgBody.getThumbnailUrl();
			String localUrl = imgBody.getLocalUrl();
			final String big_url;
			Bitmap bm = com.android.joocola.utils.BitmapUtils.decodeFile(localUrl, Utils.dip2px(context, 100), Utils.dip2px(context, 100));
			// 本地若没这张图片，从网络获取
			if (bm != null) {
				holder.iv_getImg.setImageBitmap(ThumbnailUtils.extractThumbnail(bm, Utils.dip2px(context, 100), Utils.dip2px(context, 100)));
				big_url = imgBody.getLocalUrl();
			} else {
				bmUtils.display(holder.iv_getImg, remoteUrl);
				big_url = imgBody.getRemoteUrl();
			}
			// 如果有图片，需要为图片设置一个点击事件（点击看大图)
			holder.iv_getImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, BigPicActivity.class);
					intent.putExtra("big_url", big_url);
					context.startActivity(intent);
				}
			});
		}
		// 这里为聊天用户的头像设置点击事件,点击进入用户详情
		holder.iv_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, PersonalDetailActivity.class);
				// 发送用户id
				intent.putExtra("userId", message.getFrom().substring(1));
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

package com.android.joocola.adapter;

import java.util.HashMap;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.activity.BaseActivity;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.AdminMessage;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

/**
 * 系统消息listView的适配器
 * 
 * @author:LiXiaoSong
 * @copyright © joocola.com
 * @Date:2014-10-13
 */
public class SystemMessageAdapter extends BaseAdapter {

	/**
	 * 数据源
	 */
	private List<AdminMessage> data;
	/**
	 * 视图填充器
	 */
	private LayoutInflater inflater;
	/**
	 * 图片下载工具
	 */
	private BitmapUtils bmUtils;
	/**
	 * 传入的activity上下文
	 */
	BaseActivity activity;
	/**
	 * 数据库工具
	 */
	DbUtils db;

	public SystemMessageAdapter(List<AdminMessage> dataSource, BaseActivity context) {
		data = dataSource;
		db = JoocolaApplication.getInstance().getDB();
		inflater = LayoutInflater.from(context);
		bmUtils = new BitmapUtils(context);
		bmUtils.configDefaultLoadingImage(R.drawable.logo);
		bmUtils.configDefaultLoadFailedImage(R.drawable.logo);
		activity = context;
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
		final AdminMessage msg = data.get(position);
		convertView = null;
		if (msg.getMsgType().equals("SM_Normal")) {// 两种不同的消息类型，分别进行处理
			convertView = inflater.inflate(R.layout.item_system_info_prompt, null, false);
			TextView msgInfo = (TextView) convertView.findViewById(R.id.system_msg_info);
			TextView msgTime = (TextView) convertView.findViewById(R.id.system_msg_time);
			msgInfo.setText(msg.getMsgContent());
			msgTime.setText(msg.getSendDate());
		} else if (msg.getMsgType().equals("SM_VerifyTalk")) {
			convertView = inflater.inflate(R.layout.item_system_info_agree, null, false);
			ImageView msgPhoto = (ImageView) convertView.findViewById(R.id.iv_sm);
			TextView msgInfo = (TextView) convertView.findViewById(R.id.tv_title_sm);
			TextView msgTime = (TextView) convertView.findViewById(R.id.tv_time_sm);
			final Button msgAgree = (Button) convertView.findViewById(R.id.btn_agree_sm);
			bmUtils.display(msgPhoto, Utils.processResultStr(Constants.URL + msg.getRelateUserPhoto(), "_150_"));
			msgInfo.setText(msg.getMsgContent());
			msgTime.setText(msg.getSendDate());
			if (msg.getCallUrl().equals("ok")) {
				msgAgree.setEnabled(false);
				msgAgree.setText("已同意");
			} else {
				msgAgree.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						activity.getHttpResult(new HashMap<String, String>(), Constants.URL + msg.getCallUrl(), new HttpPostCallBack() {

							@Override
							public void httpPostResolveData(String result) {
								if (result.equals("true")) {// 发送的请求被接受
									msg.setCallUrl("ok");
									try {
										// 将数据库内容更新，代表该请求已经被同意
										db.update(msg, "CallUrl");
									} catch (DbException e) {
										e.printStackTrace();
									}
								}
								msgAgree.setEnabled(false);
								msgAgree.setText("已同意");
							}
						});
					}
				});
			}
		}
		return convertView;
	}

}

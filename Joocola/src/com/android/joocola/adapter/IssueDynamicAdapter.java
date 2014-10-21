package com.android.joocola.adapter;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.AdminMessageContentButtonEntity;
import com.android.joocola.entity.AdminMessageContentEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * 邀约动态的适配器
 * 
 * @see:
 * @since:
 * @copyright © joocola.com
 * @Date:2014年10月21日
 */
public class IssueDynamicAdapter extends BaseAdapter {

	private ArrayList<AdminMessageContentEntity> mList;
	private LayoutInflater inflater;
	private ImageLoader mImageLoader;
	private ViewHolder viewHolder;
	private Handler mHandler;
	private Context mContext;

	public IssueDynamicAdapter(ArrayList<AdminMessageContentEntity> list, Context context, BitmapCache bitmapCache, Handler handler) {
		mList = list;
		mContext = context;
		inflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader(Volley.newRequestQueue(context), bitmapCache);
		mHandler = handler;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_issuedynamite, null);
			viewHolder.AssistContent = (TextView) convertView.findViewById(R.id.itemid_assistcontent);
			viewHolder.btn = (Button) convertView.findViewById(R.id.itemid_btn);
			viewHolder.locationInfo = (TextView) convertView.findViewById(R.id.itemid_locationinfo);
			viewHolder.mainContent = (TextView) convertView.findViewById(R.id.itemid_maincontent);
			viewHolder.name = (TextView) convertView.findViewById(R.id.itemid_name);
			viewHolder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.itemid_iv);
			viewHolder.SendDateStr = (TextView) convertView.findViewById(R.id.itemid_senddatestr);
			viewHolder.SenderDateStr = (TextView) convertView.findViewById(R.id.itemid_senderdatestr);
			viewHolder.sex = (ImageView) convertView.findViewById(R.id.itemid_seximg);
			viewHolder.personLL = (LinearLayout) convertView.findViewById(R.id.itemid_personll);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		AdminMessageContentEntity mContentEntity = mList.get(position);
		AdminMessageContentButtonEntity mButtonEntity = mContentEntity.getAdminMessageContentButtonEntity();
		int type = mContentEntity.getMsgTypeId();
		String imgUrl = mContentEntity.getSenderPhoto();
		if (type == 12) {// 邀请
			viewHolder.mainContent.setVisibility(View.GONE);
			viewHolder.name.setText(mContentEntity.getSenderName());
			if (mContentEntity.getSenderSexIsFemale()) {
				viewHolder.sex.setImageResource(R.drawable.girl);
			} else {
				viewHolder.sex.setImageResource(R.drawable.boy);
			}
			viewHolder.locationInfo.setText(mContentEntity.getSenderLocationInfo());
			viewHolder.SenderDateStr.setText(mContentEntity.getSenderDateStr());
			viewHolder.SendDateStr.setText(mContentEntity.getSendDateStr());
			viewHolder.btn.setText(mButtonEntity.getCaption());
			viewHolder.AssistContent.setText(mContentEntity.getAssistContent());
			viewHolder.btn.setOnClickListener(new btnOnclick(mButtonEntity.getCallUrl()));
			viewHolder.networkImageView.setErrorImageResId(R.drawable.photobg);
			viewHolder.networkImageView.setDefaultImageResId(R.drawable.photobg);
			viewHolder.networkImageView.setImageUrl(Utils.processResultStr(Constants.URL + imgUrl, "_150_"), mImageLoader);
			if (mButtonEntity.getCallUrl() == null) {
				viewHolder.btn.setText(mButtonEntity.getCaption());
			} else {
				viewHolder.btn.setText(mButtonEntity.getCaption());
				viewHolder.btn.setOnClickListener(new btnOnclick(mButtonEntity.getCaption()));
			}
		} else if (type == 10) {// 报名
			if (mContentEntity.getSenderID() != 1) {// 如果button有内容的
				viewHolder.name.setText(mContentEntity.getSenderName());
				if (mContentEntity.getSenderSexIsFemale()) {
					viewHolder.sex.setImageResource(R.drawable.girl);
				} else {
					viewHolder.sex.setImageResource(R.drawable.boy);
				}
				viewHolder.locationInfo.setText(mContentEntity.getSenderLocationInfo());
				viewHolder.SenderDateStr.setText(mContentEntity.getSenderDateStr());
				viewHolder.SendDateStr.setText(mContentEntity.getSendDateStr());
				viewHolder.btn.setText(mButtonEntity.getCaption());
				viewHolder.AssistContent.setText(mContentEntity.getAssistContent());
				viewHolder.btn.setOnClickListener(new btnOnclick(mButtonEntity.getCallUrl()));
				viewHolder.networkImageView.setErrorImageResId(R.drawable.photobg);
				viewHolder.networkImageView.setDefaultImageResId(R.drawable.photobg);
				viewHolder.networkImageView.setImageUrl(Utils.processResultStr(Constants.URL + imgUrl, "_150_"), mImageLoader);
			} else {
				viewHolder.name.setText("您已经接受了该邀请:");
				viewHolder.networkImageView.setVisibility(View.GONE);
				viewHolder.personLL.setVisibility(View.GONE);
				viewHolder.mainContent.setText("此处需要后台调整");
				viewHolder.btn.setVisibility(View.GONE);
				viewHolder.SendDateStr.setText(mContentEntity.getSendDateStr());
			}
		} else if (type == 11) {// 回复
			viewHolder.AssistContent.setText(mContentEntity.getAssistContent());
			viewHolder.mainContent.setText(mContentEntity.getMainContent());
			viewHolder.AssistContent.setVisibility(View.VISIBLE);
			viewHolder.mainContent.setVisibility(View.VISIBLE);
			viewHolder.personLL.setVisibility(View.GONE);
			viewHolder.name.setText(mContentEntity.getSenderName());
			viewHolder.SendDateStr.setText(mContentEntity.getSendDateStr());
			viewHolder.btn.setVisibility(View.GONE);
			viewHolder.networkImageView.setErrorImageResId(R.drawable.photobg);
			viewHolder.networkImageView.setDefaultImageResId(R.drawable.photobg);
			viewHolder.networkImageView.setImageUrl(Utils.processResultStr(Constants.URL + imgUrl, "_150_"), mImageLoader);
		} else if (type == 13) {// 评论
			viewHolder.name.setText(mContentEntity.getMainContent());
			viewHolder.AssistContent.setText(mContentEntity.getAssistContent());
			viewHolder.SendDateStr.setText(mContentEntity.getSendDateStr());
			viewHolder.btn.setText(mButtonEntity.getCaption());
			viewHolder.personLL.setVisibility(View.GONE);
			viewHolder.networkImageView.setVisibility(View.GONE);
			// 此处需要跟我哥交流1下。
		}

		return convertView;
	}

	class btnOnclick implements OnClickListener {

		private String url;

		public btnOnclick(String mString) {
			url = mString;
		}

		@Override
		public void onClick(View v) {
			Log.e("bb", url);
			HttpPostInterface httpPostInterface = new HttpPostInterface();
			httpPostInterface.getData(url, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					try {
						JSONObject jsonObject = new JSONObject(result);
						boolean isSuccees = jsonObject.getBoolean("item1");
						String content = jsonObject.getString("item2");
						if (isSuccees) {
							mHandler.sendEmptyMessage(0);
						}
						Utils.toast(mContext, content);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onNetWorkError() {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	class ViewHolder {

		LinearLayout personLL;
		NetworkImageView networkImageView;
		TextView name;// 名字
		TextView locationInfo;// 发送者距离
		ImageView sex;
		Button btn;// 接受
		TextView AssistContent;// 辅助信息
		TextView mainContent;// 主要信息
		TextView SenderDateStr;// 发送者的时间
		TextView SendDateStr;// 消息发送时间
	}
}

package com.android.joocola.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.activity.PersonalDetailActivity;
import com.android.joocola.entity.ReplyEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class IssueReplyAdapter extends BaseAdapter {
	private List<ReplyEntity> mReplys;
	private LayoutInflater inflater;
	private ViewHolder holder;
	private ImageLoader mImageLoader;
	private Context mContext;

	public IssueReplyAdapter(List<ReplyEntity> replys, Context context,
			BitmapCache bitmapCache) {
		mReplys = replys;
		mContext = context;
		inflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader(Volley.newRequestQueue(context),
				bitmapCache);
	}

	@Override
	public int getCount() {
		return mReplys.size();
	}

	@Override
	public Object getItem(int position) {
		return mReplys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.replyitem, null);
			holder.touxiang = (NetworkImageView) convertView
					.findViewById(R.id.reply_img);
			holder.content = (TextView) convertView
					.findViewById(R.id.reply_content);
			holder.name = (TextView) convertView.findViewById(R.id.reply_name);
			holder.time = (TextView) convertView.findViewById(R.id.reply_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ReplyEntity replyEntity = mReplys.get(position);
		final int publishID = replyEntity.getPublisherID();
		holder.content.setText(replyEntity.getContent());
		holder.name.setText(replyEntity.getPublisherName());
		holder.time.setText(replyEntity.getPublishDate());
		String touxiangUrl = replyEntity.getPublisherPhotoString();
		holder.touxiang.setErrorImageResId(R.drawable.photobg);
		holder.touxiang.setDefaultImageResId(R.drawable.photobg);
		holder.touxiang.setImageUrl(Constans.URL + touxiangUrl, mImageLoader);
		holder.touxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						PersonalDetailActivity.class);
				intent.putExtra("userId", publishID + "");
				Log.e("跳转的pid", publishID + "");
				mContext.startActivity(intent);

			}
		});
		return convertView;
	}

	private class ViewHolder {
		private TextView content, name, time;
		private NetworkImageView touxiang;
	}
}

package com.android.joocola.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.activity.PersonalDetailActivity;
import com.android.joocola.entity.GetIssueInfoEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.Utils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class GetIssueItemAdapter extends BaseAdapter {

	private List<GetIssueInfoEntity> infos = new ArrayList<GetIssueInfoEntity>();
	private LayoutInflater inflater;
	private ViewHolder holder;
	private ImageLoader mImageLoader;
	private Context mContext;

	public GetIssueItemAdapter(Context context, BitmapCache bitmapCache, ImageLoader imageLoader) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		mImageLoader = imageLoader;
	}

	public GetIssueItemAdapter(List<GetIssueInfoEntity> info, Context context, BitmapCache bitmapCache, ImageLoader imageLoader) {
		this.infos = info;
		mContext = context;
		inflater = LayoutInflater.from(context);
		mImageLoader = imageLoader;

	}

	public void setData(List<GetIssueInfoEntity> mInfos) {
		this.infos = mInfos;
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return infos.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.getissueitem, null);
			holder.title = (TextView) convertView.findViewById(R.id.issueitem_title);
			holder.name = (TextView) convertView.findViewById(R.id.issueitem_name);
			holder.age = (TextView) convertView.findViewById(R.id.issueitem_age);
			holder.astro = (TextView) convertView.findViewById(R.id.issueitem_astro);
			holder.issuetime = (TextView) convertView.findViewById(R.id.issueitem_time);
			holder.issuesex = (TextView) convertView.findViewById(R.id.issueitem_issuesex);
			holder.issuecost = (TextView) convertView.findViewById(R.id.issueitem_issuecost);
			holder.location = (TextView) convertView.findViewById(R.id.issueitem_location);
			holder.description = (TextView) convertView.findViewById(R.id.issueitem_description);
			holder.state = (TextView) convertView.findViewById(R.id.issueitem_state);
			holder.usercount = (TextView) convertView.findViewById(R.id.issueitem_usercount);
			holder.replycount = (TextView) convertView.findViewById(R.id.issueitem_replycount);
			holder.touxiang = (NetworkImageView) convertView.findViewById(R.id.issueitem_img);
			holder.sexImageView = (ImageView) convertView.findViewById(R.id.issueitem_seximg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GetIssueInfoEntity entity = infos.get(position);
		final String publishID = entity.getPublisherID() + "";
		holder.title.setText(entity.getTitle());
		holder.name.setText(entity.getPublisherName());
		holder.age.setText(entity.getPublisherAge() + "");
		holder.astro.setText(entity.getPublisherAstro());
		holder.issuetime.setText(entity.getReserveDate());
		holder.issuesex.setText(entity.getSexName());
		holder.issuecost.setText(entity.getCostName());
		holder.location.setText(entity.getLocationName());
		holder.description.setText(entity.getDescription());
		holder.state.setText("浏览" + entity.getBrowseCount() + "次");
		holder.usercount.setText("报名(" + entity.getApplyUserCount() + ")");
		holder.replycount.setText("回复(" + entity.getReplyCount() + ")");
		String touxiangUrl = entity.getPublisherPhoto();
		if (entity.getPublisherSexID() == 1) {
			holder.sexImageView.setImageResource(R.drawable.boy);
			holder.age.setTextColor(mContext.getResources().getColor(R.color.lanse));
			holder.astro.setTextColor(mContext.getResources().getColor(R.color.lanse));
		} else {
			holder.sexImageView.setImageResource(R.drawable.girl);
			holder.age.setTextColor(mContext.getResources().getColor(R.color.fense));
			holder.astro.setTextColor(mContext.getResources().getColor(R.color.fense));
		}
		holder.touxiang.setErrorImageResId(R.drawable.photobg);
		holder.touxiang.setDefaultImageResId(R.drawable.photobg);
		Log.e("bb", Utils.processResultStr(Constants.URL + touxiangUrl, "_150_"));
		holder.touxiang.setImageUrl(Utils.processResultStr(Constants.URL + touxiangUrl, "_150_"), mImageLoader);
		holder.touxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PersonalDetailActivity.class);
				intent.putExtra("userId", publishID + "");
				mContext.startActivity(intent);
			}
		});

		return convertView;
	}

	private class ViewHolder {

		TextView title, name, age, astro, issuetime, issuesex, issuecost, location, description, state,
				usercount, replycount;
		NetworkImageView touxiang;
		ImageView sexImageView;
	}
}

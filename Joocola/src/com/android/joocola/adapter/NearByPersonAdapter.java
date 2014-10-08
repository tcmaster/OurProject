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
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.Utils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class NearByPersonAdapter extends BaseAdapter {

	private List<UserInfo> mUserInfos = new ArrayList<UserInfo>();
	private Context ctx;
	private LayoutInflater inflater;
	private ImageLoader mImageLoader;
	private ViewHolder holder;

	public NearByPersonAdapter(List<UserInfo> userInfos, Context context, BitmapCache bitmapCache) {
		ctx = context;
		mUserInfos = userInfos;
		inflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader(Volley.newRequestQueue(context), bitmapCache);
	}

	public void setUsers(ArrayList<UserInfo> userInfos) {
		this.mUserInfos = userInfos;
	}

	@Override
	public int getCount() {
		return mUserInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mUserInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_nearby_person, null);
			holder.img = (NetworkImageView) convertView.findViewById(R.id.nearbyper__img);
			holder.age = (TextView) convertView.findViewById(R.id.nearbyper__age);
			holder.astro = (TextView) convertView.findViewById(R.id.nearbyper__astro);
			holder.distance = (TextView) convertView.findViewById(R.id.nearbyper_distance);
			holder.name = (TextView) convertView.findViewById(R.id.nearbyper_name);
			holder.sexImg = (ImageView) convertView.findViewById(R.id.nearbyper__seximg);
			holder.signature = (TextView) convertView.findViewById(R.id.nearbyper__signature);
			holder.time = (TextView) convertView.findViewById(R.id.nearbyper_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UserInfo userInfo = mUserInfos.get(position);
		final String publishID = userInfo.getPID();
		String imgUrl = userInfo.getPhotoUrl();
		holder.age.setText(userInfo.getAge());
		holder.astro.setText(userInfo.getAstro());
		holder.distance.setText(userInfo.getLocDistince());
		holder.name.setText(userInfo.getNickName());
		Log.e("bb--->getview时", userInfo.getNickName());
		holder.signature.setText(userInfo.getSignature());
		holder.time.setText(userInfo.getLocDate());
		if (userInfo.getSexID().equals("1")) {
			holder.sexImg.setImageResource(R.drawable.boy);
			holder.age.setTextColor(ctx.getResources().getColor(R.color.lanse));
			holder.astro.setTextColor(ctx.getResources().getColor(R.color.lanse));
		} else {
			holder.sexImg.setImageResource(R.drawable.girl);
			holder.age.setTextColor(ctx.getResources().getColor(R.color.fense));
			holder.astro.setTextColor(ctx.getResources().getColor(R.color.fense));
		}
		holder.img.setErrorImageResId(R.drawable.photobg);
		holder.img.setDefaultImageResId(R.drawable.photobg);
		holder.img.setImageUrl(Utils.processResultStr(Constants.URL + imgUrl, "_150_"), mImageLoader);
		holder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ctx, PersonalDetailActivity.class);
				intent.putExtra("userId", publishID + "");
				Log.e("bb", publishID + "");
				ctx.startActivity(intent);

			}
		});
		return convertView;
	}

	private class ViewHolder {

		NetworkImageView img;
		ImageView sexImg;
		TextView name;
		TextView distance;
		TextView time;
		TextView age;
		TextView astro;
		TextView signature;
	}

}

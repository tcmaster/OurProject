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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.activity.PersonalDetailActivity;
import com.android.joocola.entity.SimpleUserInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class SimpleApllyUserAdapter extends BaseAdapter {
	private Context mContext;
	private List<SimpleUserInfo> mUsers;
	private LayoutInflater inflater;
	private ImageLoader mImageLoader;
	private ViewHolder holder;

	public SimpleApllyUserAdapter(Context context, BitmapCache bitmapCache) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader(Volley.newRequestQueue(context),
				bitmapCache);
	}

	@Override
	public int getCount() {
		return mUsers.size();
	}

	@Override
	public Object getItem(int position) {
		return mUsers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.simpleapply_item, null);
			holder.accept = (Button) convertView
					.findViewById(R.id.simple_accept);
			holder.age = (TextView) convertView.findViewById(R.id.simple_age);
			holder.evaluate = (Button) convertView
					.findViewById(R.id.simple_evaluate);
			holder.img = (NetworkImageView) convertView
					.findViewById(R.id.simple_img);
			holder.name = (TextView) convertView.findViewById(R.id.simple_name);
			holder.sex = (ImageView) convertView
					.findViewById(R.id.simple_seximg);
			holder.signature = (TextView) convertView
					.findViewById(R.id.simple_signature);
			holder.state = (TextView) convertView
					.findViewById(R.id.simple_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SimpleUserInfo simpleUserInfo = mUsers.get(position);
		holder.name.setText(simpleUserInfo.getUserName());
		holder.signature.setText(simpleUserInfo.getSignature());
		String url = simpleUserInfo.getPhotoUrl();
		final String publishID = simpleUserInfo.getPid() + "";
		holder.img.setErrorImageResId(R.drawable.photobg);
		holder.img.setDefaultImageResId(R.drawable.photobg);
		holder.img.setImageUrl(Constans.URL + url, mImageLoader);
		holder.img.setOnClickListener(new OnClickListener() {

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

	public void setmUsers(List<SimpleUserInfo> mUsers) {
		this.mUsers = mUsers;
	}

	private class ViewHolder {
		NetworkImageView img;
		TextView name, age, signature;
		ImageView sex;
		Button evaluate;// 评价
		Button accept;// 接受
		TextView state;// 状态
	}

}

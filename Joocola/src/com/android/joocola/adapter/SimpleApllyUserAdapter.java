package com.android.joocola.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class SimpleApllyUserAdapter extends BaseAdapter {
	private Context mContext;
	private List<SimpleUserInfo> mUsers;
	private LayoutInflater inflater;
	private ImageLoader mImageLoader;
	private ViewHolder holder;
	private int state;
	private String issue_pid;
	private String publish_id;
	private final String myUrl = "Bus.AppointController.ApproveAppoint.ashx";
	private Handler mHandler;

	// 0 代表，该用户未申请当前邀约。即当前邀约无任何关系
	// 10 代表，用户已经申请加入当前邀约。
	// 20 代表，用户自己已经取消
	// 30 代表，用户已经被批准加入当前邀约

	public SimpleApllyUserAdapter(Context context, BitmapCache bitmapCache,
			Handler handler) {
		mContext = context;
		mHandler = handler;
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
			holder.astro = (TextView) convertView
					.findViewById(R.id.simple_astro);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SimpleUserInfo simpleUserInfo = mUsers.get(position);

		holder.name.setText(simpleUserInfo.getUserName());
		holder.signature.setText(simpleUserInfo.getSignature());
		String url = simpleUserInfo.getPhotoUrl();
		final String publishID = simpleUserInfo.getPid() + "";
		holder.astro.setText(simpleUserInfo.getAstro());
		holder.age.setText(simpleUserInfo.getAge() + "");
		if (simpleUserInfo.getSexID() == 1) {
			holder.sex.setImageResource(R.drawable.boy);
			holder.age.setTextColor(mContext.getResources().getColor(
					R.color.lanse));
			holder.astro.setTextColor(mContext.getResources().getColor(
					R.color.lanse));
		} else {
			holder.sex.setImageResource(R.drawable.girl);
			holder.age.setTextColor(mContext.getResources().getColor(
					R.color.fense));
			holder.astro.setTextColor(mContext.getResources().getColor(
					R.color.fense));
		}
		holder.img.setErrorImageResId(R.drawable.photobg);
		holder.img.setDefaultImageResId(R.drawable.photobg);
		holder.img.setImageUrl(Constans.URL + url, mImageLoader);
		holder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,
						PersonalDetailActivity.class);
				intent.putExtra("userId", publishID + "");
				mContext.startActivity(intent);

			}
		});
		if (state == 10) {
			holder.accept.setVisibility(View.VISIBLE);
			holder.accept.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					HttpPostInterface httpPostInterface = new HttpPostInterface();
					httpPostInterface.addParams("appointID", issue_pid);
					httpPostInterface.addParams("opUserID", publish_id);
					httpPostInterface.addParams("applyUserID", publishID);
					httpPostInterface.getData(myUrl, new HttpPostCallBack() {

						@Override
						public void httpPostResolveData(String result) {
							Message message = Message.obtain();
							message.what = 3;
							message.obj = result;
							mHandler.sendMessage(message);
						}
					});
				}
			});
		} else if (state == 30) {
			// holder.evaluate.setVisibility(View.VISIBLE);
			// if (publishID.equals(publish_id)) {
			// holder.evaluate.setText("查看自己的评价");
			// holder.evaluate.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Utils.toast(mContext, "暂时不知道到底应该在哪查看评价");
			// }
			// });
			// } else {
			//
			// holder.evaluate.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// showEvaluateDialog();
			// }
			// });
			// }
		}
		return convertView;
	}

	public void setmUsers(List<SimpleUserInfo> mUsers) {
		this.mUsers = mUsers;
	}

	public List<SimpleUserInfo> getmUsers() {
		return mUsers;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setIssue_pid(String issue_pid) {
		this.issue_pid = issue_pid;
	}

	public void setPublish_id(String publish_id) {
		this.publish_id = publish_id;
	}

	private class ViewHolder {
		NetworkImageView img;
		TextView name, age, signature, astro;// 签名
		ImageView sex;
		Button evaluate;// 评价
		Button accept;// 接受
		TextView state;// 状态
	}

	private void showEvaluateDialog() {
		CustomerDialog customerDialog = new CustomerDialog();
	}

}

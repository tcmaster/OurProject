package com.android.joocola.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.AppointScoreEntity;
import com.android.joocola.utils.BitmapCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * 评价item的适配器
 * 
 * @author:LiXiaoSong
 * @see:
 * @since:
 */
public class ShowEvaluateAdapter extends BaseAdapter {

	private ArrayList<AppointScoreEntity> mList;
	private Context ctx;
	private LayoutInflater inflater;
	private ImageLoader mImageLoader;
	private ViewHolder holder;

	public ShowEvaluateAdapter(ArrayList<AppointScoreEntity> list, Context context, BitmapCache bitmapCache) {
		ctx = context;
		mList = list;
		inflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader(Volley.newRequestQueue(context), bitmapCache);
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
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_appointscore, null);
			holder.comment_neirong = (TextView) convertView.findViewById(R.id.comment_neirong);
			holder.comment_pj = (TextView) convertView.findViewById(R.id.comment_pjtxt);
			holder.comment_pjimg = (ImageView) convertView.findViewById(R.id.comment_pjimg);
			holder.comment_time = (TextView) convertView.findViewById(R.id.comment_time);
			holder.comment_user = (TextView) convertView.findViewById(R.id.comment_user);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AppointScoreEntity appointScoreEntity = mList.get(position);
		holder.comment_user.setText(appointScoreEntity.getFromUserName() + " 评价 " + appointScoreEntity.getToUserName());
		holder.comment_neirong.setText(appointScoreEntity.getComment());
		if (appointScoreEntity.getScoreID().equals("10")) {
			holder.comment_pj.setText("好评");
			holder.comment_pjimg.setImageResource(R.drawable.score_haoping);
		} else if (appointScoreEntity.getScoreID().equals("20")) {
			holder.comment_pj.setText("中评");
			holder.comment_pjimg.setImageResource(R.drawable.score_zhongping);
		} else if (appointScoreEntity.getScoreID().equals("30")) {
			holder.comment_pj.setText("差评");
			holder.comment_pjimg.setImageResource(R.drawable.score_chaping);
		}
		holder.comment_time.setText(appointScoreEntity.getCommentDateStr());

		return convertView;
	}

	class ViewHolder {

		TextView comment_user;// 显示是谁跟谁的评价
		TextView comment_pj;// 显示是什么评价
		TextView comment_neirong;// 内容
		TextView comment_time;// 时间
		ImageView comment_pjimg;// 评价的图

	}

}

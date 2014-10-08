package com.android.joocola.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.IssueInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constants;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class IssueAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private ImageLoader mImageLoader;
	private ArrayList<IssueInfo> issueInfos;
	private boolean[] pos;

	public IssueAdapter(Context mContext, ArrayList<IssueInfo> infos, BitmapCache bitmapCache) {
		layoutInflater = LayoutInflater.from(mContext);
		issueInfos = infos;
		mImageLoader = new ImageLoader(Volley.newRequestQueue(mContext), bitmapCache);
		pos = new boolean[issueInfos.size()];
	}

	@Override
	public int getCount() {
		return issueInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return issueInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setPos(int position) {
		for (int i = 0; i < pos.length; i++) {
			if (i == position)
				pos[i] = true;
			else
				pos[i] = false;
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.issue_grid_item, null);
			viewHodler = new ViewHodler();
			viewHodler.issueIcon = (NetworkImageView) convertView.findViewById(R.id.issue_griditem_img);
			viewHodler.issueText = (TextView) convertView.findViewById(R.id.issue_griditem_txt);
			viewHodler.rl = (RelativeLayout) convertView.findViewById(R.id.issue_griditem_rl);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}
		IssueInfo issueInfo = issueInfos.get(position);
		String imageUrl = Constants.URL + issueInfo.getPhotoUrl();
		String name = issueInfo.getTypeName();
		if (pos[position])
			viewHodler.rl.setBackgroundResource(R.drawable.btnclickfalse);
		else
			viewHodler.rl.setBackgroundResource(R.color.white);
		viewHodler.issueIcon.setDefaultImageResId(R.drawable.ic_launcher);
		viewHodler.issueIcon.setErrorImageResId(R.drawable.ic_launcher);
		viewHodler.issueIcon.setImageUrl(imageUrl, mImageLoader);
		viewHodler.issueText.setText(name);
		return convertView;
	}

	private class ViewHodler {

		/**
		 * 类别LOGO
		 */
		private NetworkImageView issueIcon;
		/**
		 * 类别名称
		 */
		private TextView issueText;
		/**
		 * 外边的布局
		 */
		private RelativeLayout rl;
	}

}

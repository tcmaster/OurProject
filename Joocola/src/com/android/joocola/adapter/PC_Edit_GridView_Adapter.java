package com.android.joocola.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.joocola.R;
import com.lidroid.xutils.BitmapUtils;

public class PC_Edit_GridView_Adapter extends BaseAdapter {
	private List<String> imgUrls;
	private LayoutInflater mInflater;
	private Context mContext;

	public PC_Edit_GridView_Adapter(Context context) {
		mInflater = LayoutInflater.from(context);
		imgUrls = new ArrayList<String>();
		mContext = context;
	}

	@Override
	public int getCount() {
		return imgUrls.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position != imgUrls.size())
			return imgUrls.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = mInflater.inflate(R.layout.item_pc_edit_gridview,
					null);
		ImageView iV = (ImageView) convertView.findViewById(R.id.imageView);
		if (position == imgUrls.size()) {
			iV.setImageResource(R.drawable.ic_launcher);
		} else {
			BitmapUtils utils = new BitmapUtils(mContext);
			utils.display(iV, imgUrls.get(position));
		}
		return convertView;
	}

	public void addImgUrls(String url) {
		imgUrls.add(url);
		notifyDataSetChanged();
	}

}

package com.android.joocola.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.joocola.R;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;

public class PC_Edit_GridView_Adapter extends BaseAdapter {
	private ArrayList<String> imgUrls;
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
			iV.setImageResource(R.drawable.addpic);
		} else {
			iV.setBackgroundResource(R.drawable.photobg);
			iV.setPadding(Utils.dip2px(mContext, 3), Utils.dip2px(mContext, 3),
					Utils.dip2px(mContext, 3), Utils.dip2px(mContext, 3));
			Log.v("图片即将下载", imgUrls.get(position));
			BitmapUtils utils = new BitmapUtils(mContext);
			BitmapDisplayConfig config = new BitmapDisplayConfig();
			config.setBitmapMaxSize(new BitmapSize(iV.getWidth() - 5, iV
					.getHeight() - 5));
			utils.display(iV, imgUrls.get(position), config);
		}
		return convertView;
	}

	public void addImgUrls(String url) {
		imgUrls.add(url);
		notifyDataSetChanged();
	}

	public void deleteImgUrls(int position) {
		imgUrls.remove(position);
		notifyDataSetChanged();
	}

	public ArrayList<String> getImageUrls() {
		return imgUrls;
	}

}

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
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;

public class PC_Edit_GridView_Adapter extends BaseAdapter {

	private ArrayList<String> imgUrls;
	private ArrayList<String> bigimgUrls;
	private LayoutInflater mInflater;
	private Context mContext;
	/**
	 * true 为编辑界面，false为详情界面
	 */
	private boolean flag = false;

	public PC_Edit_GridView_Adapter(Context context, boolean flag) {
		mInflater = LayoutInflater.from(context);
		imgUrls = new ArrayList<String>();
		bigimgUrls = new ArrayList<String>();
		mContext = context;
		this.flag = flag;
	}

	@Override
	public int getCount() {
		if (flag)
			return imgUrls.size() + 1;
		else
			return imgUrls.size();
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
			convertView = mInflater.inflate(R.layout.item_pc_edit_gridview, null);
		ImageView iV = (ImageView) convertView.findViewById(R.id.imageView);
		if (position == imgUrls.size()) {
			iV.setImageResource(R.drawable.addpic);
		} else {
			iV.setBackgroundResource(R.drawable.photobg);
			iV.setPadding(Utils.dip2px(mContext, 3), Utils.dip2px(mContext, 3), Utils.dip2px(mContext, 3), Utils.dip2px(mContext, 3));
			Log.v("图片即将下载", imgUrls.get(position));
			BitmapUtils utils = new BitmapUtils(mContext);
			BitmapDisplayConfig config = new BitmapDisplayConfig();
			config.setBitmapMaxSize(new BitmapSize(iV.getWidth() - 5, iV.getHeight() - 5));
			utils.display(iV, Constants.URL + imgUrls.get(position), config);
		}
		return convertView;
	}

	public void addImgUrls(String url) {
		imgUrls.add(Utils.processResultStr(url, "_150_"));
		bigimgUrls.add(url);
		notifyDataSetChanged();
	}

	public void deleteImgUrls(int position) {
		imgUrls.remove(position);
		bigimgUrls.remove(position);
		notifyDataSetChanged();
	}

	/**
	 * @return 大图地址
	 */
	public ArrayList<String> getImageUrls() {
		return bigimgUrls;
	}

}

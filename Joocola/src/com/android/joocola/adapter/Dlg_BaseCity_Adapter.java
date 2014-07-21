package com.android.joocola.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.BaseCityInfo;

public class Dlg_BaseCity_Adapter extends BaseAdapter {
	private List<BaseCityInfo> infos;
	private Context mContext;
	/**
	 * 确定哪个元素被选中，哪个元素未被选中
	 */
	private boolean[] selectionArray;
	private LayoutInflater inflater;

	public Dlg_BaseCity_Adapter(Context context) {
		infos = new ArrayList<BaseCityInfo>();
		this.mContext = context;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.dlg_list_item, null);
			holder.iv_arrow = (ImageView) convertView
					.findViewById(R.id.list_arrow);
			holder.tv_text = (TextView) convertView
					.findViewById(R.id.list_content);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.layout);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		if (selectionArray[position]) {
			holder.iv_arrow.setVisibility(View.VISIBLE);
			holder.layout.setBackgroundResource(R.color.lessgray2);
		} else {
			holder.iv_arrow.setVisibility(View.INVISIBLE);
			holder.layout.setBackgroundResource(R.color.white);
		}
		holder.tv_text.setText(infos.get(position).getCityName());
		return convertView;
	}

	public void bindData(List<BaseCityInfo> infos) {
		this.infos = infos;
		selectionArray = new boolean[infos.size()];
		for (int i = 0; i < selectionArray.length; i++)
			selectionArray[i] = false;
		notifyDataSetChanged();
	}

	public void setPos(int pos) {
		for (int i = 0; i < selectionArray.length; i++) {
			if (i == pos)
				selectionArray[i] = true;
			else
				selectionArray[i] = false;
		}
		notifyDataSetChanged();
	}

	private class ViewHolder {
		ImageView iv_arrow;
		TextView tv_text;
		LinearLayout layout;
	}

}

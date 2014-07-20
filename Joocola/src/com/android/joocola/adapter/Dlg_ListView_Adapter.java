package com.android.joocola.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.BaseDataInfo;

/**
 * 编辑界面Dialog ListView的选择适配器
 * 
 * @author lixiaosong
 * 
 */
public class Dlg_ListView_Adapter extends BaseAdapter {
	/**
	 * 确定哪个元素被选中，哪个元素未被选中
	 */
	private boolean[] selectionArray;
	/**
	 * 该界面需要显示的数据
	 */
	private List<BaseDataInfo> baseDataInfos;
	private LayoutInflater inflater;

	public Dlg_ListView_Adapter(List<BaseDataInfo> baseDataInfos,
			Context context) {
		this.baseDataInfos = baseDataInfos;
		selectionArray = new boolean[baseDataInfos.size()];
		for (int i = 0; i < selectionArray.length; i++)
			selectionArray[i] = false;
		if (selectionArray.length > 0) {
			selectionArray[0] = true;
		}
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return baseDataInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return baseDataInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 设置当前被选中的项目的表现形式，仅能单选
	 */
	public void setPos(int pos) {
		for (int i = 0; i < selectionArray.length; i++) {
			if (i == pos)
				selectionArray[i] = true;
			else
				selectionArray[i] = false;
		}
		notifyDataSetChanged();
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
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		if (selectionArray[position])
			holder.iv_arrow.setVisibility(View.VISIBLE);
		else
			holder.iv_arrow.setVisibility(View.INVISIBLE);
		holder.tv_text.setText(baseDataInfos.get(position).getItemName());
		return convertView;
	}

	private class ViewHolder {
		ImageView iv_arrow;
		TextView tv_text;
	}
}

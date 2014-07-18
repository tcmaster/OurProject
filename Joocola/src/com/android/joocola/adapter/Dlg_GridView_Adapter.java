package com.android.joocola.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.BaseDataInfo;

/**
 * 编辑界面Dialog girdView的选择适配器
 * 
 * @author lixiaosong
 * 
 */
public class Dlg_GridView_Adapter extends BaseAdapter {
	/**
	 * 确定哪个元素被选中，哪个元素未被选中
	 */
	private boolean[] selectionArray;
	/**
	 * 该界面需要显示的数据
	 */
	private List<BaseDataInfo> baseDataInfos;
	private LayoutInflater inflater;

	public Dlg_GridView_Adapter(List<BaseDataInfo> baseDataInfos,
			Context context) {
		this.baseDataInfos = baseDataInfos;
		selectionArray = new boolean[baseDataInfos.size()];
		for (int i = 0; i < selectionArray.length; i++)
			selectionArray[i] = false;
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

	public void setPos(int position) {
		for (int i = 0; i < baseDataInfos.size(); i++) {
			if (i == position) {
				selectionArray[i] = !selectionArray[i];
			}
		}
		notifyDataSetChanged();
	}

	public boolean[] getSelection() {
		return selectionArray;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv_text = null;
		if (convertView == null)
			convertView = inflater.inflate(R.layout.dlg_gird_item, null);
		tv_text = (TextView) convertView.findViewById(R.id.dlg_list_item);
		if (selectionArray[position])
			tv_text.setBackgroundColor(Color.GREEN);
		else
			tv_text.setBackgroundColor(Color.WHITE);
		tv_text.setText(baseDataInfos.get(position).getItemName());
		return convertView;
	}

}

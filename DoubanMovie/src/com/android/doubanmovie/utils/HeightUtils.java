package com.android.doubanmovie.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class HeightUtils {
	public static void setViewGroupHeight(ListView group) {
		ListAdapter adp = group.getAdapter();
		int totalHeight = 0;
		Log.v("test", adp.getCount() + "�ҵĴ�С��");
		for (int i = 0; i < adp.getCount(); i++) {
			View view = adp.getView(i, null, group);
			// view Ҫ��layout������linearLayout
			view.measure(0, 0);
			totalHeight += view.getMeasuredHeight();
			Log.v("test", totalHeight + "�߶�+" + i);
		}
		totalHeight += group.getDividerHeight() * (adp.getCount() - 1);
		ViewGroup.LayoutParams params = group.getLayoutParams();
		params.height = totalHeight;
		// ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		group.setLayoutParams(params);
	}

}

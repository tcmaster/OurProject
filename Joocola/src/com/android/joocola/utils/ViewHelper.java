package com.android.joocola.utils;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.joocola.R;
import com.android.joocola.entity.BaseDataInfo;

/**
 * 所有控件的辅助操作类
 * 
 * @author bb
 * 
 */
public class ViewHelper {
	public static int radioGroupFillItems(Context context, RadioGroup rg_group,
			List<BaseDataInfo> items) {
		return radioGroupFillItems(context, rg_group, items, -1);
	}

	public static int radioGroupFillItems(Context context, RadioGroup rg_group,
			List<BaseDataInfo> items, int selectedPID) {
		if (items.size() == 0) {
			return -1;
		}
		int[] ids = { R.id.radioButton1, R.id.radioButton2, R.id.radioButton3,
				R.id.radioButton4, R.id.radioButton5 };
		boolean hasExists = false;
		int selID = selectedPID;

		// Fill Items
		for (int i = 0; i < items.size(); i++) {
			RadioButton button = new RadioButton(context);
			BaseDataInfo info = items.get(i);
			button.setText(info.getItemName());
			button.setTag(info.getPID());
			button.setId(ids[i]);
			button.setButtonDrawable(R.drawable.radiobutton);
			RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(
					RadioGroup.LayoutParams.WRAP_CONTENT,
					RadioGroup.LayoutParams.WRAP_CONTENT);
			lp.gravity = Gravity.CENTER_VERTICAL;
			lp.rightMargin = Utils.dip2px(context, 20);
			button.setLayoutParams(lp);
			button.setPadding(Utils.dip2px(context, 20), 1, 1, 1);
			rg_group.addView(button);

			if (!hasExists && info.getPID() == selID) {
				hasExists = true;
			}

			// if (selButton == null && info.getPID() == selID) {
			// selButton = button;
			// }
		}

		if (!hasExists) {
			selID = items.get(0).getPID();
		}
		rg_group.check(selID);
		// Log.e("selButton", "selButton = "
		// + (selButton == null ? "null" : selButton.getTag().toString()));
		// Log.e("selID = ", selID + "");
		// if (selButton != null) {
		// rg_group.check(selButton.getId());
		// }

		return selID;
	}
}

package com.soufun.widget.wheel;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.joocola.R;

public class BottomPopWindow extends PopupWindow {
	private Button btn_take_photo, btn_pick_photo, btn_cancel, btn_bo_video;
	private TextView tv_choose;
	public View mMenuView;

	public BottomPopWindow(Activity context, View.OnClickListener itemsOnClick,
			String... flag) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_bottom, null);
		btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_video);
		btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_video);
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		tv_choose = (TextView) mMenuView.findViewById(R.id.tv_choose);
		btn_bo_video = (Button) mMenuView.findViewById(R.id.btn_bo_video);

		switch (flag.length) {
		case 5:
			tv_choose.setText(flag[4]);
		case 4:
			btn_cancel.setText(flag[3]);
		case 3:
			btn_pick_photo.setText(flag[2]);
		case 2:
			btn_take_photo.setText(flag[1]);
		case 1:
			if (flag[0] != null && !flag[0].equals("")) {
				btn_bo_video.setText(flag[0]);
				btn_bo_video.setVisibility(View.VISIBLE);
			}
			break;
		}

		// 取消按钮
		btn_cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// 销毁弹出框
				dismiss();
			}
		});
		// 设置按钮监听
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
		btn_bo_video.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}
}
package com.android.doubanmovie.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.android.doubanmovie.R;
import com.android.doubanmovie.utils.NetWorkUtils;

//应用载入界面
public class DoubanMovie extends Activity {
	private static final int[] BG = { R.drawable.main1, R.drawable.main2,
			R.drawable.main3, R.drawable.main4, R.drawable.mainpage };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_douban_movie);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainlayout);
		layout.setBackgroundResource(BG[(int) (Math.random() * 5)]);
		if (NetWorkUtils.isConnectionToInternet(this)) {// 联网状态
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					Intent intent = new Intent(DoubanMovie.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				}
			}, 3000);

		} else {// 非联网模式
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("网络状态提示").setMessage("当前网络不可用，请打开网络使用本应用 ")
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 目前点确定无用，还未实现，目前离线就关应用
							DoubanMovie.this.finish();
							System.exit(0);

						}
					}).create().show();
		}

	}

}

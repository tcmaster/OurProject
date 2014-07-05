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

//Ӧ���������
public class DoubanMovie extends Activity {
	private static final int[] BG = { R.drawable.main1, R.drawable.main2,
			R.drawable.main3, R.drawable.main4, R.drawable.mainpage };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_douban_movie);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainlayout);
		layout.setBackgroundResource(BG[(int) (Math.random() * 5)]);
		if (NetWorkUtils.isConnectionToInternet(this)) {// ����״̬
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

		} else {// ������ģʽ
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("����״̬��ʾ").setMessage("��ǰ���粻���ã��������ʹ�ñ�Ӧ�� ")
					.setPositiveButton("ȷ��", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Ŀǰ��ȷ�����ã���δʵ�֣�Ŀǰ���߾͹�Ӧ��
							DoubanMovie.this.finish();
							System.exit(0);

						}
					}).create().show();
		}

	}

}

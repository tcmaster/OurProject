package com.android.doubanmovie.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.doubanmovie.R;
import com.android.doubanmovie.httptask.GetImageTask;
import com.android.doubanmovie.httptask.GetImageTask.ImageCallBack;

public class BigImageActivity extends Activity implements OnClickListener {
	private ImageView image;
	private ImageButton left, right;
	private Bundle data;
	private int currPos;
	private String currImg;
	private String currDesc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_image);
		Log.v("test", "it 's ok");
		image = (ImageView) findViewById(R.id.bigImage);
		left = (ImageButton) findViewById(R.id.left);
		right = (ImageButton) findViewById(R.id.right);
		data = getIntent().getBundleExtra("data");
		currPos = getIntent().getIntExtra("position", 0);
		currImg = getIntent().getStringExtra("image");
		currDesc = getIntent().getStringExtra("name");
		showImg();
		left.setOnClickListener(this);
		right.setOnClickListener(this);
	}

	private void showImg() {
		image.setVisibility(View.INVISIBLE);
		new GetImageTask(800, 800).execTask(currImg, null, new ImageCallBack() {

			@Override
			public void getData(String path, ViewGroup parent, Bitmap bm) {
				image.setImageBitmap(bm);
				image.setVisibility(View.VISIBLE);
			}
		}, false);
		getActionBar().setTitle(currDesc);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left:
			if (currPos == 0) {
				Toast.makeText(this, "�Ѿ��ǵ�һ����", Toast.LENGTH_SHORT).show();
				return;
			}
			currPos--;
			currImg = data.getStringArray(currPos + "")[0];
			currDesc = data.getStringArray(currPos + "")[1];
			showImg();
			break;
		case R.id.right:
			if (currPos == data.size() - 1) {
				Toast.makeText(this, "�Ѿ������һ����", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			currPos++;
			currImg = data.getStringArray(currPos + "")[0];
			currDesc = data.getStringArray(currPos + "")[1];
			showImg();
			break;
		default:
			break;
		}

	}
}

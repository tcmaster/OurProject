package com.android.joocola.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.android.joocola.R;
import com.android.joocola.utils.Utils;

public class RegisterTwoActivity extends BaseActivity implements
		OnClickListener {
	// 用户头像
	private ImageView iv_userPhoto;
	// 从相册获取头像，拍照获取头像，完成注册
	private Button b_fromGallery, b_takePicture, b_finishRegister;
	// 昵称，生日
	private EditText et_nickName, et_birthday;
	// 性别选择
	private RadioGroup rg_group;
	// 标识位
	private static final int PICKPICTURE = 1;
	private static final int TAKEPHOTO = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interface_register2);
		initView();
		registerListener();
	}

	private void initView() {
		iv_userPhoto = (ImageView) findViewById(R.id.photoImg);
		b_fromGallery = (Button) findViewById(R.id.selectFromGallery);
		b_takePicture = (Button) findViewById(R.id.takePicture);
		b_finishRegister = (Button) findViewById(R.id.registerButton);
		et_nickName = (EditText) findViewById(R.id.NickName_text);
		et_birthday = (EditText) findViewById(R.id.birthdayText);
		rg_group = (RadioGroup) findViewById(R.id.Group);
	}

	private void registerListener() {
		b_finishRegister.setOnClickListener(this);
		b_takePicture.setOnClickListener(this);
		iv_userPhoto.setOnClickListener(this);
		b_fromGallery.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectFromGallery:
			getPhotoFromGallery();
			break;
		case R.id.takePicture:
			getPhotoByTakePicture();
			break;
		case R.id.registerButton:
			register();
			break;
		case R.id.photoImg:
			clickPhoto();
			break;
		default:
			break;
		}
	}

	private void getPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PICKPICTURE);
	}

	private void getPhotoByTakePicture() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent getImageByCamera = new Intent(
					"android.media.action.IMAGE_CAPTURE");
			startActivityForResult(getImageByCamera, TAKEPHOTO);
		} else {
			Utils.toast(this, "请确定已经插入SD卡");
		}

	}

	private void register() {

	}

	private void clickPhoto() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 图片路径
		Bitmap result = null;
		Uri uri = data.getData();
		if (requestCode == PICKPICTURE) {
			Log.v("lixiaosong", uri.toString());
			iv_userPhoto.setImageURI(uri);
			result = iv_userPhoto.getDrawingCache();
		} else if (requestCode == TAKEPHOTO) {
			if (uri == null) {
				Bundle bundle = data.getExtras();
				Bitmap photo = (Bitmap) bundle.get("data");
				result = ThumbnailUtils.extractThumbnail(photo,
						iv_userPhoto.getWidth(), iv_userPhoto.getHeight());
				iv_userPhoto.setImageBitmap(result);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}

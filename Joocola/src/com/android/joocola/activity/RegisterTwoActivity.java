package com.android.joocola.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.joocola.R;

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

	}

	private void getPhotoByTakePicture() {

	}

	private void register() {

	}

	private void clickPhoto() {

	}
}

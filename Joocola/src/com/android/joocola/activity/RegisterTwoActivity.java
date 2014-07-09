package com.android.joocola.activity;

import java.io.File;

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
import com.android.joocola.entity.RegisterInfo;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
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
	private static final String REGISTERURL = "Sys.UserController.AppRegist.ashx";
	// 本页需要上传的数据
	// 图片地址
	private String imgUrl = "";
	// 昵称
	private String nickName = "";
	// 性别id
	private String pid = "";
	// 生日
	private String birthday = "";

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
		nickName = et_nickName.getText().toString();
		birthday = et_birthday.getText().toString();
		HttpPostInterface interface1 = new HttpPostInterface();
		RegisterInfo info = (RegisterInfo) getIntent().getSerializableExtra(
				"info");
		info.setBirthday(birthday);
		info.setSex(rg_group.getCheckedRadioButtonId() + "");
		info.setPhotoUrl(imgUrl);
		info.setNickName(nickName);
		interface1.addParams("userName", info.getUserName());
		interface1.addParams("password", info.getPassword());
		interface1.addParams("introduceUserName", info.getIntroducer());
		interface1.addParams("photoUrl", info.getPhotoUrl());
		interface1.addParams("nickName", info.getNickName());
		interface1.addParams("sexID", info.getSex());
		interface1.addParams("birthDay", info.getBirthday());
		interface1.addParams("verifyCode", info.getAutoCode());
		interface1.getData(REGISTERURL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {

			}
		});
	}

	private void clickPhoto() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 图片路径
		Bitmap result = null;
		if (data != null) {
			Uri uri = data.getData();
			if (requestCode == PICKPICTURE) {
				Log.v("lixiaosong", uri.toString());
				iv_userPhoto.setImageURI(uri);
				String path = Utils.getRealPathFromURI(uri,
						RegisterTwoActivity.this);
				File file = new File(path);
				uploadImage(file);
			} else if (requestCode == TAKEPHOTO) {
				if (uri == null) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						Bitmap photo = (Bitmap) bundle.get("data");
						if (photo != null) {
							result = ThumbnailUtils.extractThumbnail(photo,
									iv_userPhoto.getWidth(),
									iv_userPhoto.getHeight());
							iv_userPhoto.setImageBitmap(result);
							File file = Utils.createBitmapFile(result);
							uploadImage(file);
						}
					}
				} else {
					iv_userPhoto.setImageURI(uri);
					result = iv_userPhoto.getDrawingCache();
					String path = Utils.getRealPathFromURI(uri,
							RegisterTwoActivity.this);
					File file = new File(path);
					uploadImage(file);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void uploadImage(File file) {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.uploadImageData(file, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Log.v("lixiaosong", result);
				if (result != null) {
					imgUrl = processResultStr(result, "_50");
				}
			}
		});
	}

	public String processResultStr(String result, String size) {
		return result;
	}

}

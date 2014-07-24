package com.android.joocola.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.BaseDataInfo;
import com.android.joocola.entity.RegisterInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.android.joocola.utils.ViewHelper;

public class RegisterTwoActivity extends BaseActivity implements
		OnClickListener {
	// 用户头像
	private ImageView iv_userPhoto;
	// 从相册获取头像，拍照获取头像，完成注册
	private Button b_fromGallery, b_takePicture, b_finishRegister;
	// 昵称，生日
	private EditText et_nickName;
	private TextView tv_birthday;
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
	private Handler handler;
	/**
	 * 照相时的临时文件名
	 */
	private String tempName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.interface_register2);
		handler = new Handler();
		initView();
		initActionBar();
		registerListener();
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarleft().setText("注册");
		getActionBarRight().setText("注册2/2");

	}

	private void initView() {
		iv_userPhoto = (ImageView) findViewById(R.id.photoImg);
		b_fromGallery = (Button) findViewById(R.id.selectFromGallery);
		b_takePicture = (Button) findViewById(R.id.takePicture);
		b_finishRegister = (Button) findViewById(R.id.registerButton);
		et_nickName = (EditText) findViewById(R.id.NickName_text);
		tv_birthday = (TextView) findViewById(R.id.birthdayText);
		rg_group = (RadioGroup) findViewById(R.id.Group);
		initRadioGroup();
		// rg_group.post(new Runnable() {
		//
		// @Override
		// public void run() {
		// // 默认选中第一项
		// rg_group.check(R.id.radioButton1);
		// }
		// });
	}

	private void registerListener() {
		b_finishRegister.setOnClickListener(this);
		b_takePicture.setOnClickListener(this);
		iv_userPhoto.setOnClickListener(this);
		b_fromGallery.setOnClickListener(this);
		tv_birthday.setOnClickListener(this);
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
		case R.id.birthdayText:
			showDate();
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
			tempName = System.currentTimeMillis() + ".jpg";
			File file = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DCIM).getAbsolutePath()
					+ File.separator + tempName);
			Uri u = Uri.fromFile(file);
			Log.v("lixiaosong", "我要往这里放照片" + file.getAbsolutePath());
			Intent getImageByCamera = new Intent(
					"android.media.action.IMAGE_CAPTURE");
			getImageByCamera.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, u);
			startActivityForResult(getImageByCamera, TAKEPHOTO);
		} else {
			Utils.toast(this, "未检测到SD卡，无法拍照获取图片");
		}

	}

	private void register() {
		nickName = et_nickName.getText().toString();
		birthday = tv_birthday.getText().toString();
		if (birthday.equals("请选择生日")) {
			Utils.toast(this, "请填写生日字段");
			return;
		}
		if (Utils.isNickName(nickName)) {
			Utils.toast(this, "昵称请以非数字开头");
			return;
		}
		HttpPostInterface interface1 = new HttpPostInterface();
		RegisterInfo info = (RegisterInfo) getIntent().getSerializableExtra(
				"info");
		info.setBirthday(birthday);
		int pid = (Integer) rg_group.findViewById(
				rg_group.getCheckedRadioButtonId()).getTag();
		info.setSex(pid + "");
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
				try {
					final JSONObject object = new JSONObject(result);
					if (object.getBoolean("Item1")) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								try {
									Utils.toast(
											RegisterTwoActivity.this,
											"提示（用于测试），注册成功,PID为"
													+ object.getString("Item2"));
								} catch (JSONException e) {
									e.printStackTrace();
								}
								RegisterTwoActivity.this.finish();
							}
						});
					} else {
						handler.post(new Runnable() {

							@Override
							public void run() {
								try {
									Utils.toast(RegisterTwoActivity.this,
											object.getString("Item2"));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void clickPhoto() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 图片路径
		if (requestCode == TAKEPHOTO && resultCode == RESULT_OK) {
			File file = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DCIM).getAbsolutePath()
					+ File.separator + tempName);
			/**
			 * 这里需要对原图进行缩放
			 */
			Bitmap bm = ThumbnailUtils.extractThumbnail(
					BitmapFactory.decodeFile(file.getAbsolutePath()),
					iv_userPhoto.getWidth(), iv_userPhoto.getHeight());
			bm = Utils.rotaingImageView(
					Utils.rotateImg(file.getAbsolutePath()), bm);
			File resultFile = Utils.createBitmapFile(bm);
			iv_userPhoto.setImageBitmap(bm);
			uploadImage(resultFile);
		} else if (requestCode == PICKPICTURE && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				Bitmap bm = null;
				try {
					bm = ThumbnailUtils.extractThumbnail(
							MediaStore.Images.Media.getBitmap(
									this.getContentResolver(), uri),
							iv_userPhoto.getWidth(), iv_userPhoto.getHeight());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				iv_userPhoto.setImageBitmap(bm);
				String path = Utils.getRealPathFromURI(uri,
						RegisterTwoActivity.this);
				File file = new File(path);
				uploadImage(file);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void uploadImage(final File file) {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.uploadImageData(file, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result != null) {
					imgUrl = Utils.processResultStr(result, "_150_");
					file.delete();
				}
			}
		});
	}

	private void initRadioGroup() {
		List<BaseDataInfo> infos = JoocolaApplication.getInstance()
				.getBaseInfo(Constans.basedata_Sex);
		ViewHelper.radioGroupFillItems(RegisterTwoActivity.this, rg_group,
				infos);
	}

	public void showDate() {
		DatePickerDialog dlg = new DatePickerDialog(this,
				DatePickerDialog.THEME_HOLO_LIGHT, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						tv_birthday.setText(year + "-" + (monthOfYear + 1)
								+ "-" + dayOfMonth);
					}
				}, 1990, 1, 1);
		dlg.setCancelable(true);
		dlg.show();
	}

}

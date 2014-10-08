package com.android.joocola.activity;

import java.io.File;
import java.util.Map;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.Utils;

public class BaseActivity extends FragmentActivity {

	public ActionBar mActionBar;
	// 自定义ActionBar相关
	private TextView left, right, title;
	private ImageView arrow, logo;
	private LinearLayout backButton;
	// 标识位
	/**
	 * 图库
	 */
	protected static final int PICKPICTURE = 1;
	/**
	 * 相机
	 */
	protected static final int TAKEPHOTO = 2;
	/**
	 * 裁剪
	 */
	protected static final int CROP = 3;
	// 默认裁剪的宽高
	private static final int OUTPUT_X = 256;
	private static final int OUTPUT_Y = 256;
	/**
	 * 
	 * 照相时的临时文件名
	 */
	protected String tempName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionBar = getActionBar();
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;

		}
		return true;
	}

	/**
	 * 要使用自定义actionBar，必须调用这个方法
	 */
	public void useCustomerActionBar() {
		mActionBar.setCustomView(R.layout.layout_actionbar);
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		View view = mActionBar.getCustomView();
		left = (TextView) view.findViewById(R.id.leftText);
		right = (TextView) view.findViewById(R.id.rightText);
		logo = (ImageView) view.findViewById(R.id.logo);
		arrow = (ImageView) view.findViewById(R.id.arrow);
		title = (TextView) view.findViewById(R.id.title);
		backButton = (LinearLayout) view.findViewById(R.id.homelayout);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseActivity.this.onBackPressed();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 得到自定义actionBar左边的文字，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 左边的文字
	 */
	public TextView getActionBarleft() {
		return left;
	}

	/**
	 * 得到自定义actionBar右边的文字，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 右边的文字
	 */
	public TextView getActionBarRight() {
		return right;
	}

	/**
	 * 得到自定义actionBar标题文字，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 标题文字
	 */
	public TextView getActionBarTitle() {
		return title;
	}

	/**
	 * 得到自定义actionBar左边的箭头，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 左边的箭头
	 */
	public ImageView getActionBarArrow() {
		return arrow;
	}

	/**
	 * 得到自定义actionBar自定义的logo，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 自定义的logo
	 */
	public ImageView getActionBarLogo() {
		return logo;
	}

	/**
	 * 得到自定义actionBar自定义的返回布局，并进行设置,使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return 返回键的layout
	 */
	public LinearLayout getActionBarBackButton() {
		return backButton;
	}

	public View getActionBarCustomerView() {
		return mActionBar.getCustomView();
	}

	/**
	 * 得到ActionBar的CustomerView， 使用之前需调用{@link useCustomerActionBar}
	 * 
	 * @return ActionBar的CustomerView
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	/**
	 * 裁剪照片
	 * 
	 * @author: LiXiaosong
	 * @date:2014-10-8
	 */
	public void cropPicture(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// 可裁剪
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", OUTPUT_X);
		intent.putExtra("outputY", OUTPUT_Y);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);// 若为false则表示不返回数据
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, CROP);
	}

	/**
	 * 从图库获取图片
	 * 
	 * @author: LiXiaosong
	 * @date:2014-10-8
	 */
	public void getPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PICKPICTURE);
	}

	/**
	 * 拍照获取图片
	 * 
	 * @author: LiXiaosong
	 * @date:2014-10-8
	 */
	public void getPhotoByTakePicture() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			tempName = System.currentTimeMillis() + ".jpg";
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + tempName);
			Uri u = Uri.fromFile(file);
			Log.v("lixiaosong", "我要往这里放照片" + file.getAbsolutePath());
			Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
			getImageByCamera.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
			getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, u);
			startActivityForResult(getImageByCamera, TAKEPHOTO);
		} else {
			Utils.toast(this, "未检测到SD卡，无法拍照获取图片");
		}
	}

	/**
	 * 获取网络数据专用方法
	 * 
	 * @param params
	 *            要传递的接口参数
	 * @param url
	 *            网络请求地址
	 * @param callBack
	 *            回调方法
	 * @author: LiXiaosong
	 * @date:2014-10-8
	 */
	public void getHttpResult(Map<String, String> params, String url, HttpPostInterface.HttpPostCallBack callBack) {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		for (Map.Entry<String, String> elemnt : params.entrySet()) {
			httpPostInterface.addParams(elemnt.getKey(), elemnt.getValue());
		}
		httpPostInterface.getData(url, callBack);
	}

}

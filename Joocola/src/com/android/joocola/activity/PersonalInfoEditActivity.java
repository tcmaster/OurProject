package com.android.joocola.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.Dlg_GridView_Adapter;
import com.android.joocola.adapter.Dlg_ListView_Adapter;
import com.android.joocola.adapter.PC_Edit_GridView_Adapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.BaseDataInfo;
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.android.joocola.view.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnFocusChange;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 个人资料编辑界面 这个页面用了xUtils
 * 
 * @author lixiaosong
 * 
 */
public class PersonalInfoEditActivity extends BaseActivity {
	/**
	 * 昵称
	 */
	@ViewInject(R.id.nickName_et)
	private EditText nickName;
	/**
	 * 生日
	 */
	@ViewInject(R.id.birthday_tv)
	private TextView birthday_tv;
	/**
	 * 爱好
	 */
	@ViewInject(R.id.hobby)
	private TextView hobby_tv;
	/**
	 * 个性签名
	 */
	@ViewInject(R.id.signin)
	private EditText signin_tv;
	/**
	 * 电话
	 */
	@ViewInject(R.id.phone)
	private EditText phone_tv;
	/**
	 * 现居地
	 */
	@ViewInject(R.id.location)
	private TextView location_tv;
	/**
	 * 职业
	 */
	@ViewInject(R.id.profession)
	private TextView profession_tv;
	/**
	 * 年收入
	 */
	@ViewInject(R.id.annualSalary)
	private TextView annualSalary_tv;
	/**
	 * 身高
	 */
	@ViewInject(R.id.height)
	private TextView height_tv;
	/**
	 * 情感状态
	 */
	@ViewInject(R.id.emotion)
	private TextView emotion_tv;
	/**
	 * 是否抽烟
	 */
	@ViewInject(R.id.smoke)
	private TextView smoke_tv;
	/**
	 * 是否喝酒
	 */
	@ViewInject(R.id.drink)
	private TextView drink_tv;
	/**
	 * 昵称保存按钮
	 */
	@ViewInject(R.id.nickNameSave)
	private Button nickName_save_btn;
	/**
	 * 个性签名保存按钮
	 */
	@ViewInject(R.id.signinsave)
	private Button signin_save_btn;
	/**
	 * 电话保存按钮
	 */
	@ViewInject(R.id.phoneinitsave)
	private Button phone_save_btn;
	/**
	 * 图片
	 */
	@ViewInject(R.id.myPic)
	private MyGridView pic_gv;
	/**
	 * 基础数据
	 */
	private List<BaseDataInfo> baseDataInfos;
	/**
	 * 下面三个是用于临时存放选择数据的变量
	 */
	private String choiceData;
	private int choicePID;
	private String choicePIDS;
	/**
	 * 往主线程发送消息的handler
	 */
	private Handler handler;
	// 标识位
	private static final int PICKPICTURE = 1;
	private static final int TAKEPHOTO = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info_edit);
		baseDataInfos = JoocolaApplication.getInstance().getBaseInfo();
		ViewUtils.inject(this);
		initActionBar();
		handler = new Handler();
		initData();
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarleft().setText("编辑资料");
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private void initData() {
		UserInfo userInfo = JoocolaApplication.getInstance().getUserInfo();
		nickName.setText(userInfo.getNickName());
		birthday_tv.setText(userInfo.getSexName() + " "
				+ userInfo.getBirthday());
		if (userInfo.getHobbyNames().equals(""))
			hobby_tv.setText("请选择");
		else
			hobby_tv.setText(userInfo.getHobbyNames());
		signin_tv.setText(userInfo.getSignature());
		phone_tv.setText(userInfo.getPhone());
		if (userInfo.getNewCityName().equals(""))
			location_tv.setText("请选择");
		else
			location_tv.setText(userInfo.getNewCityName());
		if (userInfo.getProfessionName().equals(""))
			profession_tv.setText("请选择");
		else
			profession_tv.setText(userInfo.getProfessionName());
		if (userInfo.getRevenueName().equals(""))
			annualSalary_tv.setText("请选择");
		else
			annualSalary_tv.setText(userInfo.getRevenueName());
		if (userInfo.getHeightName().equals(""))
			height_tv.setText("请选择");
		else
			height_tv.setText(userInfo.getHeightName());
		if (userInfo.getMarryName().equals(""))
			emotion_tv.setText("请选择");
		else
			emotion_tv.setText(userInfo.getMarryName());
		if (userInfo.getSmokeName().equals(""))
			smoke_tv.setText("请选择");
		else
			smoke_tv.setText(userInfo.getSmokeName());
		if (userInfo.getDrinkName().equals(""))
			drink_tv.setText("请选择");
		else
			drink_tv.setText(userInfo.getDrinkName());
		String[] imgs = userInfo.getAlbumPhotoUrls().split(",");
		pic_gv.setAdapter(new PC_Edit_GridView_Adapter(
				PersonalInfoEditActivity.this));
		if (imgs != null) {
			for (int i = 0; i < imgs.length; i++) {
				if (imgs[i].equals(""))
					continue;
				((PC_Edit_GridView_Adapter) pic_gv.getAdapter())
						.addImgUrls(Constans.URL + imgs[i]);
			}
		}
	}

	@OnItemClick(R.id.myPic)
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == parent.getAdapter().getCount() - 1) {
			/**
			 * 增加图片并上传的逻辑
			 */
			AlertDialog.Builder builder = new AlertDialog.Builder(
					PersonalInfoEditActivity.this);
			builder.setTitle("添加照片")
					.setMessage("选择从哪添加照片")
					.setPositiveButton("相册",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									getPhotoFromGallery();
								}
							})
					.setNegativeButton("拍照",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									getPhotoByTakePicture();
								}
							});
			builder.create().show();
		}
	}

	@OnFocusChange({ R.id.nickName_et, R.id.signin, R.id.phone })
	public void onFocusChange(View v, boolean hasFocus) {
		int vis = hasFocus ? View.VISIBLE : View.INVISIBLE;
		switch (v.getId()) {
		case R.id.nickName_et:
			nickName_save_btn.setVisibility(vis);
			break;
		case R.id.signin:
			signin_save_btn.setVisibility(vis);
			break;
		case R.id.phone:
			phone_save_btn.setVisibility(vis);
			break;
		default:
			break;
		}
	}

	@OnClick({ R.id.location, R.id.profession, R.id.annualSalary, R.id.height,
			R.id.emotion, R.id.smoke, R.id.drink, R.id.nickNameSave,
			R.id.signinsave, R.id.phoneinitsave, R.id.birthday_tv, R.id.hobby })
	public void onViewClick(View v) {
		List<BaseDataInfo> resultInfos = new ArrayList<BaseDataInfo>();
		TextView display = null;
		String title = "";
		String url = "";
		switch (v.getId()) {
		case R.id.hobby:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Hobby")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = hobby_tv;
			title = "兴趣爱好";
			showMultiChoiceDialog(resultInfos, title, display);
			return;
		case R.id.location:
			url = Constans.NEWCITYURL;
			Utils.toast(PersonalInfoEditActivity.this, "待实现");
			return;
		case R.id.birthday_tv:
			Utils.toast(PersonalInfoEditActivity.this, "待实现");
			return;
		case R.id.profession:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Profession")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = profession_tv;
			title = "职业";
			url = Constans.PROFESSIONURL;
			break;
		case R.id.annualSalary:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Revenue")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = annualSalary_tv;
			title = "年收入";
			url = Constans.USERREVENUEURL;
			break;
		case R.id.height:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Height")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = height_tv;
			title = "身高";
			url = Constans.HEIGHTURL;
			break;
		case R.id.emotion:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Marry")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = emotion_tv;
			title = "情感状态";
			url = Constans.MARRYURL;
			break;
		case R.id.smoke:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Smoke")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = smoke_tv;
			title = "抽烟";
			url = Constans.SMOKEURL;
			break;
		case R.id.drink:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Drink")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = drink_tv;
			title = "喝酒";
			url = Constans.DRINKURL;
			break;
		case R.id.nickNameSave:
			nickName_save_btn.setVisibility(View.INVISIBLE);
			saveEditText("nickName", Constans.NICKNAMEURL, nickName.getText()
					.toString());
			return;
		case R.id.signinsave:
			signin_save_btn.setVisibility(View.INVISIBLE);
			saveEditText("newSign", Constans.SIGNINURL, signin_tv.getText()
					.toString());
			return;
		case R.id.phoneinitsave:
			phone_save_btn.setVisibility(View.INVISIBLE);
			saveEditText("phone", Constans.PHONEURL, phone_tv.getText()
					.toString());
			return;
		default:
			break;
		}
		showSingleChoiceDialog(resultInfos, title, display, url);
	}

	private void saveEditText(String params, final String url, String contant) {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("userID", JoocolaApplication.getInstance()
				.getUserInfo().getPID());
		interface1.addParams(params, contant);
		interface1.getData(url, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				String data = "";
				if (url == Constans.NICKNAMEURL) {
					JSONObject object;
					try {
						object = new JSONObject(result);
						data = object.getString("Item1");
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else
					data = result;

				if (data.equals("true")) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(PersonalInfoEditActivity.this,
									"用户资料修改成功");
						}
					});
					/**
					 * 联网重新进行用户资料的获取
					 */
					JoocolaApplication.getInstance().initUserInfo(
							JoocolaApplication.getInstance().getUserInfo()
									.getPID());
				}

			}
		});
	}

	private void showSingleChoiceDialog(final List<BaseDataInfo> info,
			final String title, final TextView tv, final String url) {
		final CustomerDialog cdlg = new CustomerDialog(this,
				R.layout.dlg_singlechoice);
		choiceData = "";
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				TextView tv_title = (TextView) window
						.findViewById(R.id.dlg_pe_title);
				final ListView lv_items = (ListView) window
						.findViewById(R.id.dlg_pe_listview);
				TextView btn_ok = (TextView) window
						.findViewById(R.id.dlg_pe_ok);
				TextView btn_cacel = (TextView) window
						.findViewById(R.id.dlg_pe_cancel);
				lv_items.setAdapter(new Dlg_ListView_Adapter(info,
						PersonalInfoEditActivity.this));
				tv_title.setText(title);
				choiceData = ((BaseDataInfo) lv_items.getAdapter().getItem(0))
						.getItemName();
				choicePID = ((BaseDataInfo) lv_items.getAdapter().getItem(0))
						.getPID();
				lv_items.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						choiceData = ((BaseDataInfo) arg0.getAdapter().getItem(
								arg2)).getItemName();
						choicePID = ((BaseDataInfo) arg0.getAdapter().getItem(
								arg2)).getPID();
						((Dlg_ListView_Adapter) arg0.getAdapter()).setPos(arg2);
					}
				});
				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.dlg_pe_ok:
							/**
							 * 这里要执行网络请求
							 */
							tv.setText(choiceData);
							HttpPostInterface interface1 = new HttpPostInterface();
							interface1.addParams("userID", JoocolaApplication
									.getInstance().getUserInfo().getPID());
							if (title.equals("职业"))
								interface1.addParams("professionID", ""
										+ choicePID);
							else if (title.equals("年收入"))
								interface1.addParams("revenueID", ""
										+ choicePID);
							else if (title.equals("身高"))
								interface1
										.addParams("heightID", "" + choicePID);
							else if (title.equals("情感状态"))
								interface1.addParams("marryID", "" + choicePID);
							else if (title.equals("抽烟"))
								interface1.addParams("smokeID", "" + choicePID);
							else if (title.equals("喝酒"))
								interface1.addParams("drinkID", "" + choicePID);
							interface1.getData(url, new HttpPostCallBack() {

								@Override
								public void httpPostResolveData(String result) {
									final String data = result;
									if (data.equals("true")) {
										handler.post(new Runnable() {

											@Override
											public void run() {
												Utils.toast(
														PersonalInfoEditActivity.this,
														"用户资料修改成功");
											}
										});
										/**
										 * 联网重新进行用户资料的获取
										 */
										JoocolaApplication.getInstance()
												.initUserInfo(
														JoocolaApplication
																.getInstance()
																.getUserInfo()
																.getPID());
									}

								}
							});
							cdlg.dismissDlg();
							break;
						case R.id.dlg_pe_cancel:
							cdlg.dismissDlg();
							break;
						default:
							break;
						}

					}
				};
				btn_ok.setOnClickListener(listener);
				btn_cacel.setOnClickListener(listener);

			}
		});
		cdlg.showDlg();
	}

	private void showMultiChoiceDialog(final List<BaseDataInfo> info,
			final String title, final TextView tv) {
		final CustomerDialog cdlg = new CustomerDialog(this,
				R.layout.dlg_multichoice);
		choiceData = "";
		choicePIDS = "";
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				TextView tv_title = (TextView) window
						.findViewById(R.id.dlg_pe_title);
				final GridView gv_items = (GridView) window
						.findViewById(R.id.dlg_pe_gridView);
				TextView btn_ok = (TextView) window
						.findViewById(R.id.dlg_pe_ok);
				TextView btn_cacel = (TextView) window
						.findViewById(R.id.dlg_pe_cancel);
				tv_title.setText(title);
				gv_items.setAdapter(new Dlg_GridView_Adapter(info,
						PersonalInfoEditActivity.this));
				gv_items.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						((Dlg_GridView_Adapter) parent.getAdapter())
								.setPos(position);
					}
				});
				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.dlg_pe_ok:
							/**
							 * 这里要执行网络请求
							 */
							boolean[] selection = ((Dlg_GridView_Adapter) gv_items
									.getAdapter()).getSelection();
							for (int i = 0; i < selection.length; i++) {
								if (selection[i]) {
									choiceData += info.get(i).getItemName()
											+ ",";
									choicePIDS += info.get(i).getPID() + ",";
								}
							}
							choiceData = choiceData.substring(0,
									choiceData.length() - 1);
							choicePIDS = choicePIDS.substring(0,
									choicePIDS.length() - 1);
							tv.setText(choiceData);
							HttpPostInterface interface1 = new HttpPostInterface();
							interface1.addParams("userID", JoocolaApplication
									.getInstance().getUserInfo().getPID());
							interface1.addParams("hobbyIDs", choicePIDS);
							interface1.getData(Constans.HOBBYURL,
									new HttpPostCallBack() {

										@Override
										public void httpPostResolveData(
												String result) {
											final String data = result;
											if (data.equals("true")) {
												handler.post(new Runnable() {

													@Override
													public void run() {
														Utils.toast(
																PersonalInfoEditActivity.this,
																"用户资料修改成功");
													}
												});
												/**
												 * 联网重新进行用户资料的获取
												 */
												JoocolaApplication
														.getInstance()
														.initUserInfo(
																JoocolaApplication
																		.getInstance()
																		.getUserInfo()
																		.getPID());
											}

										}
									});
							cdlg.dismissDlg();
							break;
						case R.id.dlg_pe_cancel:
							cdlg.dismissDlg();
							break;
						default:
							break;
						}

					}
				};
				btn_ok.setOnClickListener(listener);
				btn_cacel.setOnClickListener(listener);
			}
		});
		cdlg.showDlg();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 图片路径
		Bitmap result = null;
		if (data != null) {
			Uri uri = data.getData();
			if (requestCode == PICKPICTURE) {
				String path = Utils.getRealPathFromURI(uri,
						PersonalInfoEditActivity.this);
				File file = new File(path);
				uploadImage(file);
			} else if (requestCode == TAKEPHOTO) {
				if (uri == null) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						Bitmap photo = (Bitmap) bundle.get("data");
						if (photo != null) {
							File file = Utils.createBitmapFile(photo);
							uploadImage(file);
						}
					}
				} else {
					String path = Utils.getRealPathFromURI(uri,
							PersonalInfoEditActivity.this);
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
				final String res = result;
				if (result != null) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							((PC_Edit_GridView_Adapter) pic_gv.getAdapter())
									.addImgUrls(Constans.URL + res);
						}
					});

				}
			}
		});
	}
}

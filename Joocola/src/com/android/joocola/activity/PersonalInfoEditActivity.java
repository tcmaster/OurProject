package com.android.joocola.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.Dlg_BaseCity_Adapter;
import com.android.joocola.adapter.Dlg_City_Adapter;
import com.android.joocola.adapter.Dlg_GridView_Adapter;
import com.android.joocola.adapter.Dlg_ListView_Adapter;
import com.android.joocola.adapter.PC_Edit_GridView_Adapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.BaseCityInfo;
import com.android.joocola.entity.BaseDataInfo;
import com.android.joocola.entity.CityInfo;
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.android.joocola.view.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
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
	private TextView nickName;
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
	private EditText signin_et;
	/**
	 * 电话
	 */
	@ViewInject(R.id.phone)
	private EditText phone_et;
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
	@ViewInject(R.id.seximg)
	private ImageView sex_iv;
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
	 * 下面四个是用于临时存放选择数据的变量
	 */
	private String choiceData;
	private int choicePID;
	private String choicePIDS;
	private String newCityPID;
	/**
	 * 往主线程发送消息的handler
	 */
	private Handler handler;

	/**
	 * 用户信息
	 */
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info_edit);
		baseDataInfos = JoocolaApplication.getInstance().getBaseInfo();
		ViewUtils.inject(this);
		initActionBar();
		handler = new Handler();
		userInfo = JoocolaApplication.getInstance().getUserInfo();
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

	@SuppressLint("NewApi")
	private void initData() {

		nickName.setText(userInfo.getNickName());
		birthday_tv.setText(userInfo.getBirthday());
		if (userInfo.getSexName().contains("男"))
			sex_iv.setImageResource(R.drawable.boy);
		else
			sex_iv.setImageResource(R.drawable.girl);
		if (userInfo.getHobbyNames().equals(""))
			hobby_tv.setText("请选择");
		else
			hobby_tv.setText(userInfo.getHobbyNames());
		signin_et.setText(userInfo.getSignature());
		phone_et.setText(userInfo.getPhone());
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

		pic_gv.setAdapter(new PC_Edit_GridView_Adapter(PersonalInfoEditActivity.this, true));
		if (imgs != null) {
			for (int i = 0; i < imgs.length; i++) {
				if (imgs[i].equals(""))
					continue;
				((PC_Edit_GridView_Adapter) pic_gv.getAdapter()).addImgUrls(imgs[i]);
			}
		}
		pic_gv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				if (!(((AdapterContextMenuInfo) menuInfo).position == (pic_gv.getAdapter().getCount() - 1))) {
					menu.add(0, 0, 0, "删除");
					menu.add(0, 1, 1, "设置为头像");
				}
			}
		});
	}

	/**
	 * 在退出应用时，调用方法，将修改的个性签名和手机号保存
	 */
	@Override
	protected void onStop() {
		saveEditText("newSign", Constants.SIGNINURL, signin_et.getText().toString());
		saveEditText("phone", Constants.PHONEURL, phone_et.getText().toString());
		super.onStop();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		String url = ((PC_Edit_GridView_Adapter) pic_gv.getAdapter()).getImageUrls().get(menuInfo.position);
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("userID", JoocolaApplication.getInstance().getUserInfo().getPID());
		if (item.getItemId() == 0) {
			interface1.addParams("newUrls", "");
			interface1.addParams("delUrls", url);
			interface1.getData(Constants.ALBUMURL, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					if (result.equals("true")) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								Utils.toast(PersonalInfoEditActivity.this, "删除成功");
								((PC_Edit_GridView_Adapter) pic_gv.getAdapter()).deleteImgUrls(menuInfo.position);
								/**
								 * 更新用户信息
								 */
								JoocolaApplication.getInstance().initUserInfo(JoocolaApplication.getInstance().getUserInfo().getPID());
							}
						});
					} else {
						handler.post(new Runnable() {

							@Override
							public void run() {
								Utils.toast(PersonalInfoEditActivity.this, "删除失败，网络状况不佳,请在次尝试");
							}
						});
					}

				}

				@Override
				public void onNetWorkError() {
					// TODO Auto-generated method stub

				}
			});
		} else if (item.getItemId() == 1) {
			interface1.addParams("newPhotoUrl", url);
			interface1.getData(Constants.PHOTOURL, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					if (result != null) {
						handler.post(new Runnable() {

							@Override
							public void run() {
								Utils.toast(PersonalInfoEditActivity.this, "设置用户头像成功");
							}
						});
					} else {
						handler.post(new Runnable() {

							@Override
							public void run() {
								Utils.toast(PersonalInfoEditActivity.this, "用户头像设置失败,请重新设置");
							}
						});
					}

				}

				@Override
				public void onNetWorkError() {
					// TODO Auto-generated method stub

				}
			});
		}
		return super.onContextItemSelected(item);
	}

	@OnItemClick(R.id.myPic)
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == parent.getAdapter().getCount() - 1) {
			/**
			 * 增加图片并上传的逻辑
			 */
			AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInfoEditActivity.this);
			builder.setTitle("添加照片").setMessage("选择从哪添加照片").setPositiveButton("相册", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					getPhotoFromGallery();
				}
			}).setNegativeButton("拍照", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					getPhotoByTakePicture();
				}
			});
			builder.create().show();
		} else {
			Intent intent = new Intent(PersonalInfoEditActivity.this, WatchBigPicActivity.class);
			intent.putStringArrayListExtra("imgUrls", ((PC_Edit_GridView_Adapter) pic_gv.getAdapter()).getImageUrls());
			intent.putExtra("position", position);
			startActivity(intent);
		}

	}

	@OnClick({ R.id.location, R.id.profession, R.id.annualSalary, R.id.height, R.id.emotion, R.id.smoke, R.id.drink, R.id.hobby })
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
			showCityDialog();
			return;
		case R.id.profession:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Profession")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = profession_tv;
			title = "职业";
			url = Constants.PROFESSIONURL;
			break;
		case R.id.annualSalary:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Revenue")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = annualSalary_tv;
			title = "年收入";
			url = Constants.USERREVENUEURL;
			break;
		case R.id.height:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Height")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = height_tv;
			title = "身高";
			url = Constants.HEIGHTURL;
			break;
		case R.id.emotion:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Marry")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = emotion_tv;
			title = "情感状态";
			url = Constants.MARRYURL;
			break;
		case R.id.smoke:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Smoke")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = smoke_tv;
			title = "抽烟";
			url = Constants.SMOKEURL;
			break;
		case R.id.drink:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Drink")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = drink_tv;
			title = "喝酒";
			url = Constants.DRINKURL;
			break;
		default:
			break;
		}
		showSingleChoiceDialog(resultInfos, title, display, url);
	}

	private void saveEditText(String params, final String url, String contant) {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("userID", JoocolaApplication.getInstance().getUserInfo().getPID());
		interface1.addParams(params, contant);
		interface1.getData(url, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				String data = "";
				if (url == Constants.NICKNAMEURL) {
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
					/**
					 * 联网重新进行用户资料的获取
					 */
					JoocolaApplication.getInstance().initUserInfo(JoocolaApplication.getInstance().getUserInfo().getPID());
				}

			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}

	private void showSingleChoiceDialog(final List<BaseDataInfo> info, final String title, final TextView tv, final String url) {
		final CustomerDialog cdlg = new CustomerDialog(this, R.layout.dlg_singlechoice);
		choiceData = "";
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				TextView tv_title = (TextView) window.findViewById(R.id.dlg_pe_title);
				final ListView lv_items = (ListView) window.findViewById(R.id.dlg_pe_listview);
				TextView btn_ok = (TextView) window.findViewById(R.id.dlg_pe_ok);
				TextView btn_cacel = (TextView) window.findViewById(R.id.dlg_pe_cancel);
				Dlg_ListView_Adapter dlg_ListView_Adapter = new Dlg_ListView_Adapter(info, PersonalInfoEditActivity.this);
				lv_items.setAdapter(dlg_ListView_Adapter);
				if (lv_items.getAdapter().getCount() > 5)
					lv_items.getLayoutParams().height = Utils.dip2px(PersonalInfoEditActivity.this, 200);
				tv_title.setText(title);
				choiceData = ((BaseDataInfo) lv_items.getAdapter().getItem(0)).getItemName();
				choicePID = ((BaseDataInfo) lv_items.getAdapter().getItem(0)).getPID();
				for (int i = 0; i < info.size(); i++) {
					if (info.get(i).getItemName().equals(tv.getText().toString())) {
						choicePID = info.get(i).getPID();
						dlg_ListView_Adapter.setPos(i);
						break;
					}
				}
				lv_items.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						choiceData = ((BaseDataInfo) arg0.getAdapter().getItem(arg2)).getItemName();
						choicePID = ((BaseDataInfo) arg0.getAdapter().getItem(arg2)).getPID();
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
							interface1.addParams("userID", JoocolaApplication.getInstance().getUserInfo().getPID());
							if (title.equals("职业"))
								interface1.addParams("professionID", "" + choicePID);
							else if (title.equals("年收入"))
								interface1.addParams("revenueID", "" + choicePID);
							else if (title.equals("身高"))
								interface1.addParams("heightID", "" + choicePID);
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
										/**
										 * 联网重新进行用户资料的获取
										 */
										JoocolaApplication.getInstance().initUserInfo(JoocolaApplication.getInstance().getUserInfo().getPID());
									}

								}

								@Override
								public void onNetWorkError() {
									// TODO Auto-generated method stub

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

	private void showCityDialog() {
		newCityPID = "";
		final CustomerDialog cdlg = new CustomerDialog(this, R.layout.dlg_newcity_choice);
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				TextView tv_title = (TextView) window.findViewById(R.id.dlg_pe_title);
				TextView btn_ok = (TextView) window.findViewById(R.id.dlg_pe_ok);
				TextView btn_cacel = (TextView) window.findViewById(R.id.dlg_pe_cancel);
				tv_title.setText("现居地");
				final ListView baseCity_lv = (ListView) window.findViewById(R.id.dlg_basecity_listview);
				final ListView city_lv = (ListView) window.findViewById(R.id.dlg_city_listview);
				baseCity_lv.setAdapter(new Dlg_BaseCity_Adapter(PersonalInfoEditActivity.this));
				city_lv.setAdapter(new Dlg_City_Adapter(PersonalInfoEditActivity.this));
				((Dlg_BaseCity_Adapter) baseCity_lv.getAdapter()).bindData(JoocolaApplication.getInstance().getBaseCityInfo());
				for (int i = 0; i < baseCity_lv.getAdapter().getCount(); i++) {
					if (JoocolaApplication.getInstance().getUserInfo().getNewCityName().contains(((BaseCityInfo) baseCity_lv.getAdapter().getItem(i)).getCityName())) {
						((Dlg_BaseCity_Adapter) baseCity_lv.getAdapter()).setPos(i);
						getNewCityData(((BaseCityInfo) baseCity_lv.getAdapter().getItem(i)).getPID(), (Dlg_BaseCity_Adapter) baseCity_lv.getAdapter(), i, city_lv, true);
					}
				}
				baseCity_lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						newCityPID = "";
						getNewCityData(((BaseCityInfo) parent.getAdapter().getItem(position)).getPID(), (Dlg_BaseCity_Adapter) parent.getAdapter(), position, city_lv, false);
					}
				});
				city_lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						((Dlg_City_Adapter) parent.getAdapter()).setPos(position);
						newCityPID = ((CityInfo) parent.getAdapter().getItem(position)).getPID();
					}
				});
				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.dlg_pe_ok:
							if (newCityPID.equals("")) {
								Utils.toast(PersonalInfoEditActivity.this, "请选择具体城市");
								return;
							}
							HttpPostInterface interface1 = new HttpPostInterface();
							interface1.addParams("userID", JoocolaApplication.getInstance().getUserInfo().getPID());
							interface1.addParams("newCityID", newCityPID);
							interface1.getData(Constants.NEWCITYURL, new HttpPostCallBack() {

								@Override
								public void httpPostResolveData(String result) {
									if (result.equals("true")) {
										/**
										 * 这里需要获得城市显示名称,又一个网络请求
										 */
										getCityName(newCityPID);
										JoocolaApplication.getInstance().initUserInfo(JoocolaApplication.getInstance().getUserInfo().getPID());
									}

								}

								@Override
								public void onNetWorkError() {
									// TODO Auto-generated method stub

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

	private void getNewCityData(String baseCityPID, Dlg_BaseCity_Adapter base_adp, int pos, final ListView city_lv, final boolean init) {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("provinceID", baseCityPID);
		((Dlg_BaseCity_Adapter) base_adp).setPos(pos);
		interface1.getData(Constants.CITY_INFO_URL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				final List<CityInfo> cityInfos = new ArrayList<CityInfo>();
				JSONArray array = null;
				try {
					array = new JSONArray(result);
					for (int i = 0; i < array.length(); i++) {
						CityInfo temp = new CityInfo();
						JSONObject object = null;
						try {
							object = array.getJSONObject(i);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						temp.setCityName(object.getString("CityName"));
						temp.setParentID(object.getInt("ParentID") + "");
						temp.setParentname(object.getString("ParentName"));
						temp.setPID(object.getInt("PID") + "");
						cityInfos.add(temp);
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				handler.post(new Runnable() {

					@Override
					public void run() {
						((Dlg_City_Adapter) city_lv.getAdapter()).bindData(cityInfos);
						if (init) {
							for (int i = 0; i < city_lv.getAdapter().getCount(); i++) {
								if (((CityInfo) city_lv.getAdapter().getItem(i)).getPID().equals(JoocolaApplication.getInstance().getUserInfo().getNewCityID())) {
									((Dlg_City_Adapter) city_lv.getAdapter()).setPos(i);
									newCityPID = JoocolaApplication.getInstance().getUserInfo().getNewCityID();
								}
							}
						}
					}
				});

			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}

	private void getCityName(String cityPID) {
		HttpPostInterface interface1 = new HttpPostInterface();
		interface1.addParams("cityID", cityPID);
		interface1.getData(Constants.GETCITYNAME, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				final String cityName = result;
				handler.post(new Runnable() {

					@Override
					public void run() {
						location_tv.setText(cityName);
					}
				});

			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}

	private void showMultiChoiceDialog(final List<BaseDataInfo> info, final String title, final TextView tv) {
		final CustomerDialog cdlg = new CustomerDialog(this, R.layout.dlg_multichoice);
		choiceData = "";
		choicePIDS = "";
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				TextView tv_title = (TextView) window.findViewById(R.id.dlg_pe_title);
				final GridView gv_items = (GridView) window.findViewById(R.id.dlg_pe_gridView);
				TextView btn_ok = (TextView) window.findViewById(R.id.dlg_pe_ok);
				TextView btn_cacel = (TextView) window.findViewById(R.id.dlg_pe_cancel);
				tv_title.setText(title);
				gv_items.setAdapter(new Dlg_GridView_Adapter(info, PersonalInfoEditActivity.this));
				String[] maybeChoice = tv.getText().toString().split(",");
				if (maybeChoice != null) {
					for (int i = 0; i < maybeChoice.length; i++) {
						for (int j = 0; j < gv_items.getAdapter().getCount(); j++) {
							if (((BaseDataInfo) gv_items.getAdapter().getItem(j)).getItemName().equals(maybeChoice[i])) {
								((Dlg_GridView_Adapter) gv_items.getAdapter()).setPos(j);
							}
						}
					}
				}
				gv_items.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						((Dlg_GridView_Adapter) parent.getAdapter()).setPos(position);
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
							boolean[] selection = ((Dlg_GridView_Adapter) gv_items.getAdapter()).getSelection();
							for (int i = 0; i < selection.length; i++) {
								if (selection[i]) {
									choiceData += info.get(i).getItemName() + ",";
									choicePIDS += info.get(i).getPID() + ",";
								}
							}
							if (choiceData.equals("")) {
								Utils.toast(PersonalInfoEditActivity.this, "请选择一项爱好");
								return;
							}
							choiceData = choiceData.substring(0, choiceData.length() - 1);
							choicePIDS = choicePIDS.substring(0, choicePIDS.length() - 1);
							tv.setText(choiceData);
							HttpPostInterface interface1 = new HttpPostInterface();
							interface1.addParams("userID", JoocolaApplication.getInstance().getUserInfo().getPID());
							interface1.addParams("hobbyIDs", choicePIDS);
							interface1.getData(Constants.HOBBYURL, new HttpPostCallBack() {

								@Override
								public void httpPostResolveData(String result) {
									final String data = result;
									if (data.equals("true")) {
										/**
										 * 联网重新进行用户资料的获取
										 */
										JoocolaApplication.getInstance().initUserInfo(JoocolaApplication.getInstance().getUserInfo().getPID());
									}

								}

								@Override
								public void onNetWorkError() {
									// TODO Auto-generated method stub

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICKPICTURE && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				// 裁剪处理
				cropPicture(uri);
			}
		} else if (requestCode == TAKEPHOTO && resultCode == RESULT_OK) {
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + tempName);
			// 这里需要对照片的角度进行校正
			Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
			bm = Utils.rotaingImageView(Utils.rotateImg(file.getAbsolutePath()), bm);
			File resultFile = Utils.createBitmapFile(bm);
			Uri uri = Uri.fromFile(resultFile);
			cropPicture(uri);
			uploadImage(resultFile);
		} else if (requestCode == CROP && resultCode == RESULT_OK) {
			// 得到裁剪后的结果，将图片进行上传
			if (data.getData() != null) {
				Uri resultUri = data.getData();
				String path = Utils.getRealPathFromURI(resultUri, PersonalInfoEditActivity.this);
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
				final String res = result;
				if (result != null) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							((PC_Edit_GridView_Adapter) pic_gv.getAdapter()).addImgUrls(res);
							/**
							 * 将图片相册地址上传，这里还要调网络接口
							 * 
							 */
							HttpPostInterface interface2 = new HttpPostInterface();
							interface2.addParams("newUrls", res);
							interface2.addParams("userID", JoocolaApplication.getInstance().getUserInfo().getPID());
							interface2.addParams("delUrls", "");
							interface2.getData(Constants.ALBUMURL, new HttpPostCallBack() {

								@Override
								public void httpPostResolveData(String result) {
									if (result.equals("true")) {
										handler.post(new Runnable() {

											@Override
											public void run() {
												Utils.toast(PersonalInfoEditActivity.this, "图片已上传");
											}
										});
										/**
										 * 联网重新进行用户资料的获取
										 */
										JoocolaApplication.getInstance().initUserInfo(JoocolaApplication.getInstance().getUserInfo().getPID());
									}

								}

								@Override
								public void onNetWorkError() {
									// TODO Auto-generated method stub

								}
							});
						}
					});

				}
				file.delete();
			}

			@Override
			public void onNetWorkError() {
				// TODO Auto-generated method stub

			}
		});
	}
}

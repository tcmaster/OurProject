package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.Dlg_GridView_Adapter;
import com.android.joocola.adapter.Dlg_ListView_Adapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.BaseDataInfo;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.utils.HttpPostInterface;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 个人资料编辑界面 这个页面用了xUtils
 * 
 * @author lixiaosong
 * 
 */
public class PersonalInfoEditActivity extends Activity {
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
	private EditText hobby_tv;
	/**
	 * 个性签名
	 */
	@ViewInject(R.id.signin)
	private EditText signin_tv;
	/**
	 * 电话
	 */
	@ViewInject(R.id.phone)
	private TextView phone_tv;
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
	 * 基础数据
	 */
	private List<BaseDataInfo> baseDataInfos;
	/**
	 * 下面两个是用于临时存放选择数据的变量
	 */
	private String choiceData;
	private int choicePID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info_edit);
		baseDataInfos = JoocolaApplication.getInstance().getBaseInfo();
		ViewUtils.inject(this);
	}

	@OnClick({ R.id.phone, R.id.location, R.id.profession, R.id.annualSalary,
			R.id.height, R.id.emotion, R.id.smoke, R.id.drink })
	public void onViewClick(View v) {
		List<BaseDataInfo> resultInfos = new ArrayList<BaseDataInfo>();
		TextView display = null;
		String title = "";
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
		case R.id.signin:

			break;
		case R.id.phone:

			break;
		case R.id.location:

			break;
		case R.id.profession:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Profession")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = profession_tv;
			title = "职业";
			break;
		case R.id.annualSalary:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Revenue")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = annualSalary_tv;
			title = "年收入";
			break;
		case R.id.height:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Height")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = height_tv;
			title = "身高";
			break;
		case R.id.emotion:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Marry")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = emotion_tv;
			title = "情感状态";
			break;
		case R.id.smoke:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Smoke")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = smoke_tv;
			title = "抽烟";
			break;
		case R.id.drink:
			for (int i = 0; i < baseDataInfos.size(); i++) {
				if (baseDataInfos.get(i).getTypeName().equals("Drink")) {
					resultInfos.add(baseDataInfos.get(i));
				}
			}
			display = drink_tv;
			title = "喝酒";
			break;
		default:
			break;
		}
		showSingleChoiceDialog(resultInfos, title, display);
	}

	private void showSingleChoiceDialog(final List<BaseDataInfo> info,
			final String title, final TextView tv) {
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
							interface1.addParams("", "");
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
								if (selection[i])
									choiceData += info.get(i).getItemName()
											+ " ";
							}
							tv.setText(choiceData);
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
}

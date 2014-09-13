package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.Dlg_ListView_Adapter;
import com.android.joocola.adapter.IssueAdapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.IssueInfo;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.view.MyGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

/**
 * 筛选信息类，该类使用了XUtils
 * 
 * @author lixiaosong
 * 
 */
public class FilterActivity extends BaseActivity {

	/**
	 * 邀约类型gridView
	 */
	@ViewInject(R.id.filter_type_gv)
	private MyGridView type_gv;
	/**
	 * 邀约性别RadioGroup
	 */
	@ViewInject(R.id.filter_initator_rg)
	private RadioGroup initator_rg;
	/**
	 * 邀约性别不限
	 */
	@ViewInject(R.id.unlimited)
	private RadioButton nolimit_rb;
	/**
	 * 邀约性别男
	 */
	@ViewInject(R.id.man)
	private RadioButton man_rb;
	/**
	 * 邀约性别女
	 */
	@ViewInject(R.id.women)
	private RadioButton women_rb;
	/**
	 * 年龄
	 */
	@ViewInject(R.id.filter_choiceAge)
	private TextView age_tv;
	/**
	 * 时间
	 */
	@ViewInject(R.id.filter_choiceTime)
	private TextView time_tv;
	/**
	 * 本页需要返回的数据
	 */
	public String Timespan = "0";// 时间段，默认不限
	public String PublishSexID = "0";// 性别，默认不限
	public String TypeID = "-1";// 类型id
	public String PublisherAge = "0";// 年龄id，默认不限
	public int touch = -1;// 标记上次点击的哪一个item

	// 上次的筛选信息
	private static FilterInfo infos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_filter);
		ViewUtils.inject(this);
		initActionBar();
		initViews();
		initLastData();
	}

	@SuppressLint("NewApi")
	private void initActionBar() {
		useCustomerActionBar();
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarleft().setText("筛选");
		getActionBarRight().setText("确定");
		getActionBarRight().setTextColor(Color.BLACK);
		getActionBarRight().setGravity(Gravity.CENTER);
		getActionBarRight().setBackground(getResources().getDrawable(R.drawable.btnclick));
		getActionBarRight().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("Timespan", Timespan);
				intent.putExtra("PublishSexID", PublishSexID);
				intent.putExtra("TypeID", TypeID);
				intent.putExtra("PublisherAge", PublisherAge);
				if (infos == null)
					infos = new FilterInfo();
				// 保存上次的筛选结果
				infos.PublisherAge = PublisherAge;
				infos.PublishSexID = PublishSexID;
				infos.Timespan = Timespan;
				infos.TypeID = TypeID;
				infos.touch = touch;
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	/**
	 * 根据上次的筛选结果显示数据
	 */
	private void initLastData() {
		if (infos != null) {
			touch = infos.touch;
			if (touch != -1) {
				((IssueAdapter) type_gv.getAdapter()).setPos(touch);
				TypeID = infos.TypeID;
			}
			PublishSexID = infos.PublishSexID;
			PublisherAge = infos.PublisherAge;
			Timespan = infos.Timespan;
			if (PublishSexID.equals("0"))
				nolimit_rb.setChecked(true);
			else if (PublishSexID.equals("1"))
				man_rb.setChecked(true);
			else if (PublishSexID.equals("2"))
				women_rb.setChecked(true);
			String[] ageValues = getResources().getStringArray(R.array.AgeValue);
			for (int i = 0; i < ageValues.length; i++) {
				if (ageValues[i].equals(PublisherAge)) {
					age_tv.setText(getResources().getStringArray(R.array.AgeName)[i]);
					break;
				}
			}
			String[] timeValues = getResources().getStringArray(R.array.AppointTimespanValue);
			for (int i = 0; i < ageValues.length; i++) {
				if (timeValues[i].equals(Timespan)) {
					time_tv.setText(getResources().getStringArray(R.array.AppointTimespanType)[i]);
					break;
				}
			}
		}

	}

	@OnClick({ R.id.filter_choiceAge, R.id.filter_choiceTime })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.filter_choiceAge:
			showAge();
			break;
		case R.id.filter_choiceTime:
			showTime();
			break;
		default:
			break;
		}
	}

	@OnRadioGroupCheckedChange(R.id.filter_initator_rg)
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.unlimited:
			PublishSexID = "0";
			break;
		case R.id.man:
			PublishSexID = "1";
			break;
		case R.id.women:
			PublishSexID = "2";
			break;
		default:
			break;
		}
	}

	public void showAge() {
		final CustomerDialog cdlg = new CustomerDialog(this, R.layout.dlg_singlechoice);
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				TextView title = (TextView) window.findViewById(R.id.dlg_pe_title);
				final ListView items = (ListView) window.findViewById(R.id.dlg_pe_listview);
				TextView okB = (TextView) window.findViewById(R.id.dlg_pe_ok);
				TextView cancelB = (TextView) window.findViewById(R.id.dlg_pe_cancel);
				okB.setVisibility(View.GONE);
				cancelB.setVisibility(View.GONE);
				title.setText("选择年龄");
				List<Map<String, String>> infos = new ArrayList<Map<String, String>>();
				String[] names = getResources().getStringArray(R.array.AgeName);
				String[] values = getResources().getStringArray(R.array.AgeValue);
				for (int i = 0; i < names.length; i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", names[i]);
					map.put("value", values[i]);
					infos.add(map);
				}
				items.setAdapter(new Dlg_ListView_Adapter<Map<String, String>>(infos, FilterActivity.this));
				((Dlg_ListView_Adapter) items.getAdapter()).setPos(age_tv.getText().toString());
				items.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						((Dlg_ListView_Adapter<Map<String, String>>) arg0.getAdapter()).setPos(arg2);
						PublisherAge = ((Map<String, String>) items.getAdapter().getItem(((Dlg_ListView_Adapter<Map<String, String>>) items.getAdapter()).getPos())).get("value");
						age_tv.setText(((Map<String, String>) items.getAdapter().getItem(((Dlg_ListView_Adapter<Map<String, String>>) items.getAdapter()).getPos())).get("name"));
						cdlg.dismissDlg();
					}
				});
			}
		});
		cdlg.showDlg();
	}

	public void showTime() {
		final CustomerDialog cdlg = new CustomerDialog(this, R.layout.dlg_singlechoice);
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				TextView title = (TextView) window.findViewById(R.id.dlg_pe_title);
				final ListView items = (ListView) window.findViewById(R.id.dlg_pe_listview);
				TextView okB = (TextView) window.findViewById(R.id.dlg_pe_ok);
				TextView cancelB = (TextView) window.findViewById(R.id.dlg_pe_cancel);
				LinearLayout bottomLayout = (LinearLayout) window.findViewById(R.id.bottomLayout);
				title.setText("选择时间");
				bottomLayout.setVisibility(View.GONE);
				List<Map<String, String>> infos = new ArrayList<Map<String, String>>();
				String[] names = getResources().getStringArray(R.array.AppointTimespanType);
				String[] values = getResources().getStringArray(R.array.AppointTimespanValue);
				for (int i = 0; i < names.length; i++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", names[i]);
					map.put("value", values[i]);
					infos.add(map);
				}
				items.setAdapter(new Dlg_ListView_Adapter<Map<String, String>>(infos, FilterActivity.this));
				((Dlg_ListView_Adapter) items.getAdapter()).setPos(time_tv.getText().toString());
				items.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						((Dlg_ListView_Adapter<Map<String, String>>) arg0.getAdapter()).setPos(arg2);
						Timespan = ((Map<String, String>) items.getAdapter().getItem(((Dlg_ListView_Adapter<Map<String, String>>) items.getAdapter()).getPos())).get("value");
						time_tv.setText(((Map<String, String>) items.getAdapter().getItem(((Dlg_ListView_Adapter<Map<String, String>>) items.getAdapter()).getPos())).get("name"));
						cdlg.dismissDlg();
					}
				});
			}
		});
		cdlg.showDlg();
	}

	private void initViews() {
		final IssueAdapter adapter = new IssueAdapter(this, JoocolaApplication.getInstance().getIssueInfos() == null ? JoocolaApplication.getInstance().initAddData(new ArrayList<IssueInfo>(), JoocolaApplication.getInstance()) : JoocolaApplication.getInstance().getIssueInfos(), JoocolaApplication.getInstance().getBitmapCache());
		type_gv.setAdapter(adapter);
		type_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				adapter.setPos(arg2);
				TypeID = String.valueOf(((IssueInfo) adapter.getItem(arg2)).getPID());
				touch = arg2;
			}

		});
	}

	/**
	 * 
	 * 筛选类，用来记录上次的筛选记录
	 * 
	 */
	private class FilterInfo {

		String Timespan;// 时间段
		String PublishSexID;// 性别
		String TypeID;// 类型id
		String PublisherAge;// 年龄id
		int touch;// 记录上次点击的是哪一个类型
	}
}

package com.android.joocola.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.entity.BaseDataInfo;
import com.android.joocola.entity.IssuedinvitationInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.android.joocola.utils.ViewHelper;

/**
 * 邀约详细信息界面。
 * 
 * @author bb
 * 
 */
public class IssuedinvitationActivity extends BaseActivity {

	private RadioGroup sexGroup, cost_group;
	private int issueID;
	private int userPid;
	private SharedPreferences sharedPreferences;
	private IssuedinvitationInfo issuedinvitationInfo = new IssuedinvitationInfo();
	private EditText et_issue_theme, edit_state;
	private TextView btn_location;
	private TextView tv_issuetime;
	private Button btn_sureissue;
	private double LocationX;
	private double LocationY;
	private String LocationCityName;
	private String issueUrl = "Bus.AppointController.PubAppoint.ashx";// 发布地址
	private ProgressDialog progDialog = null;
	private RelativeLayout time_layout;
	private LinearLayout location_layout;
	@SuppressLint("HandlerLeak")
	private Handler issueHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (progDialog != null) {
					progDialog.dismiss();
				}
				String json = (String) msg.obj;
				Log.e("发布的回应", json);
				try {
					JSONObject jsonObject = new JSONObject(json);
					if (jsonObject.getInt("Item1") == 0) {
						Utils.toast(IssuedinvitationActivity.this, jsonObject.getString("Item2"));
					} else {
						int issue_pid = jsonObject.getInt("Item1");
						Intent intent = new Intent(IssuedinvitationActivity.this, IssuedinvitationCActivity.class);
						intent.putExtra("issue_pid", issue_pid);
						startActivity(intent);
						IssuedinvitationActivity.this.finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiy_issuedinvitation);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String title = bundle.getString("title");
		issueID = bundle.getInt("PID");
		sharedPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		String userpidString = sharedPreferences.getString(Constans.LOGIN_PID, "0");
		userPid = Integer.parseInt(userpidString);
		issuedinvitationInfo.setIssueUserID(userPid);
		issuedinvitationInfo.setIssueId(issueID);
		Log.e("userPid", userPid + "");
		initActionbar(title);
		initView();
	}

	private void initActionbar(String title) {
		useCustomerActionBar();
		getActionBarleft().setText("发布邀约:" + title);
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);

	}

	private void initView() {
		time_layout = (RelativeLayout) this.findViewById(R.id.layout_issue_time);
		location_layout = (LinearLayout) this.findViewById(R.id.layout_location);
		et_issue_theme = (EditText) this.findViewById(R.id.et_issue_theme);
		btn_location = (TextView) this.findViewById(R.id.btn_location);
		edit_state = (EditText) this.findViewById(R.id.edit_state);
		tv_issuetime = (TextView) this.findViewById(R.id.tv_issuetime);
		time_layout.setOnClickListener(new TimeOnclickListenr());
		sexGroup = (RadioGroup) this.findViewById(R.id.issue_sex_Group);
		cost_group = (RadioGroup) this.findViewById(R.id.issue_cost_group);
		btn_sureissue = (Button) this.findViewById(R.id.btn_sureissue);
		btn_sureissue.setOnClickListener(new IssueOnclick());
		location_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(IssuedinvitationActivity.this, GaodeMapActivity.class);
				startActivityForResult(intent, 30);
			}
		});
		// initRadioGroup(Constans.basedata_Sex, sexGroup);
		List<BaseDataInfo> infos = JoocolaApplication.getInstance().getBaseInfo(Constans.basedata_Sex);
		BaseDataInfo baseDataInfo = new BaseDataInfo();
		baseDataInfo.setPID(0);
		baseDataInfo.setItemName("不限");
		baseDataInfo.setSortNo(Integer.MAX_VALUE);
		infos.add(baseDataInfo);
		ViewHelper.radioGroupFillItems(IssuedinvitationActivity.this, sexGroup, infos);
		initRadioGroup(Constans.basedata_AppointCost, cost_group);
	}

	private void initRadioGroup(String string, RadioGroup radioGroup) {
		List<BaseDataInfo> infos = JoocolaApplication.getInstance().getBaseInfo(string);
		ViewHelper.radioGroupFillItems(IssuedinvitationActivity.this, radioGroup, infos);
	}

	private void doNext() {
		if (issuedinvitationInfo != null) {
			if (et_issue_theme != null) {
				String title = et_issue_theme.getText().toString();
				if (TextUtils.isEmpty(title)) {
					Utils.toast(IssuedinvitationActivity.this, "主题不能为空");
					return;
				} else {
					issuedinvitationInfo.setTitle(title);
				}
			}
			if (btn_location != null) {
				String location = btn_location.getText().toString();
				if (TextUtils.isEmpty(location)) {
					Utils.toast(IssuedinvitationActivity.this, "邀约地点不能为空");
					return;
				} else {
					issuedinvitationInfo.setLocationName(location);
				}
			}
			if (edit_state != null) {
				String state = edit_state.getText().toString();
				if (TextUtils.isEmpty(state)) {
					Utils.toast(IssuedinvitationActivity.this, "说明不能为空");
					return;
				} else {
					issuedinvitationInfo.setLocationDescription(state);
				}
			}
			if (tv_issuetime != null) {
				String issueTime = tv_issuetime.getText().toString();
				issuedinvitationInfo.setReserveDate(issueTime);
			}
			int sexID = (Integer) sexGroup.findViewById(sexGroup.getCheckedRadioButtonId()).getTag();
			issuedinvitationInfo.setSexId(sexID);
			int costID = (Integer) cost_group.findViewById(cost_group.getCheckedRadioButtonId()).getTag();
			issuedinvitationInfo.setCostId(costID);
			issuedinvitationInfo.setLocationX(LocationX);
			issuedinvitationInfo.setLocationY(LocationY);
			showProgressDialog("发布中");
			HttpPostInterface httpPostInterface = new HttpPostInterface();
			httpPostInterface.addParams(Constans.ISSUE_COSTID, issuedinvitationInfo.getCostId() + "");
			httpPostInterface.addParams(Constans.ISSUE_DESCRIPTION, issuedinvitationInfo.getLocationDescription());
			httpPostInterface.addParams(Constans.ISSUE_LOCATIONAME, issuedinvitationInfo.getLocationName());
			httpPostInterface.addParams(Constans.ISSUE_LOCATIONX, issuedinvitationInfo.getLocationX() + "");
			httpPostInterface.addParams(Constans.ISSUE_LOCATIONY, issuedinvitationInfo.getLocationY() + "");
			httpPostInterface.addParams(Constans.ISSUE_PUBLISHERID, userPid + "");
			httpPostInterface.addParams(Constans.ISSUE_RESERVEDATE, issuedinvitationInfo.getReserveDate());
			httpPostInterface.addParams(Constans.ISSUE_SEXID, issuedinvitationInfo.getSexId() + "");
			httpPostInterface.addParams(Constans.ISSUE_TITLE, issuedinvitationInfo.getTitle());
			httpPostInterface.addParams(Constans.ISSUE_TYPEID, issuedinvitationInfo.getIssueId() + "");
			httpPostInterface.addParams(Constans.ISSUE_LOCATIONCITYNAME, LocationCityName);
			httpPostInterface.getData(issueUrl, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					Message message = Message.obtain();
					message.what = 1;
					message.obj = result;
					issueHandler.sendMessage(message);
				}
			});
		}
	}

	class IssueOnclick implements OnClickListener {

		@Override
		public void onClick(View v) {
			doNext();
		}

	}

	class TimeOnclickListenr implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(IssuedinvitationActivity.this, TimeActivity.class);
			startActivityForResult(intent, Constans.INTENT_TIME);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Constans.INTENT_TIME:
			if (resultCode == Constans.BACKTOISSUE_OK) {
				Log.i("time返回", "ok");
				String dateTime = data.getStringExtra("time");
				if (tv_issuetime != null) {
					tv_issuetime.setText(dateTime);
				}
			} else if (resultCode == Constans.BACKTOISSUE_CANCEL) {
				Log.i("time返回", "CANCEL");
			}
			break;
		case 30:
			if (resultCode == 40) {
				LocationX = data.getDoubleExtra("locationX", 123.123456);
				LocationY = data.getDoubleExtra("locationY", 321.987654);
				String address = data.getStringExtra("address");
				btn_location.setText(address);
				LocationCityName = data.getStringExtra("LocationCityName");
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog(String title) {

		progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage(title);
		progDialog.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}
}

package com.android.joocola.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.IssuedinvitationInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;

/**
 * 邀约发布界面
 */
public class IssuedinvitationBActivity extends BaseActivity {
	private EditText et_cost_credit;
	private TextView credit_balance;
	private Button btn_sureissue;
	private String creditUrl = "Sys.UserController.GetUserCredit.ashx";// 获取信用余额
	private String issueUrl = "Bus.AppointController.PubAppoint.ashx";// 发布地址
	private IssuedinvitationInfo info;
	private int userid;// 用户id
	@SuppressLint("HandlerLeak")
	private Handler issueHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (credit_balance != null) {
					String credit = (String) msg.obj;
					credit_balance.setText(credit);
				}
				break;
			case 1:
				String json = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(json);
					if (jsonObject.getInt("Item1") == 0) {
						Utils.toast(IssuedinvitationBActivity.this,
								jsonObject.getString("Item2"));
					} else {
						int issue_pid = jsonObject.getInt("Item1");
						Intent intent = new Intent(
								IssuedinvitationBActivity.this,
								IssuedinvitationCActivity.class);
						intent.putExtra("issue_pid", issue_pid);
						startActivity(intent);
						IssuedinvitationBActivity.this.finish();
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
		this.setContentView(R.layout.activity_issueb);
		Intent intent = getIntent();
		info = (IssuedinvitationInfo) intent
				.getSerializableExtra("info"); 
		userid = info.getIssueUserID();
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("userID", userid + "");
		httpPostInterface.getData(creditUrl, new HttpPostCallBack() {
			
			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 0;
				message.obj = result;
				issueHandler.sendMessage(message);

			}
		});

		initActionbar();
		initView();
	}

	private void initView() {
		et_cost_credit = (EditText) this.findViewById(R.id.et_cost_credit);
		et_cost_credit.setInputType(InputType.TYPE_CLASS_NUMBER);
		credit_balance = (TextView) this.findViewById(R.id.credit_balance);
		btn_sureissue = (Button) this.findViewById(R.id.btn_sureissue);
		btn_sureissue.setOnClickListener(new issueBtnClick());
	}
	private void initActionbar() {
		useCustomerActionBar();
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
		getActionBarleft().setText("愿花费信用");
	}

	class issueBtnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (et_cost_credit == null) {
				return;
			}
			String cost_credit = et_cost_credit.getText().toString();
			if (Integer.parseInt(cost_credit) > Integer.parseInt(credit_balance
					.getText().toString())) {
				Utils.toast(IssuedinvitationBActivity.this, "信用余额不足");
				return;
			}
			HttpPostInterface httpPostInterface = new HttpPostInterface();
			httpPostInterface.addParams(Constans.ISSUE_COSTCREDIT, cost_credit);
			httpPostInterface.addParams(Constans.ISSUE_COSTID, info.getCostId()
					+ "");
			httpPostInterface.addParams(Constans.ISSUE_DESCRIPTION,
					info.getLocationDescription());
			httpPostInterface.addParams(Constans.ISSUE_LOCATIONAME,
					info.getLocationName());
			httpPostInterface.addParams(Constans.ISSUE_LOCATIONX,
					info.getLocationX() + "");
			httpPostInterface.addParams(Constans.ISSUE_LOCATIONY,
					info.getLocationY() + "");
			httpPostInterface
					.addParams(Constans.ISSUE_PUBLISHERID, userid + "");
			httpPostInterface.addParams(Constans.ISSUE_RESERVEDATE,
					info.getReserveDate());
			httpPostInterface.addParams(Constans.ISSUE_SEXID, info.getSexId()
					+ "");
			httpPostInterface.addParams(Constans.ISSUE_TITLE, info.getTitle());
			httpPostInterface.addParams(Constans.ISSUE_TYPEID,
					info.getIssueId() + "");
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
}

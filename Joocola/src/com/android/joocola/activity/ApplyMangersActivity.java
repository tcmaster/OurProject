package com.android.joocola.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.entity.SimpleUserInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * 回复管理界面
 * 
 * @author:bb思密达
 * @see:
 * @since:
 * @copyright © joocola.com
 * @Date:2014年9月9日
 */
public class ApplyMangersActivity extends BaseActivity {

	private final String applyUrl = "Sys.UserController.GetUserSimpleInfos.ashx";
	private String issue_pid;// 该邀约id.
	private String user_pid;// 操纵者id
	private String ReserveDate;// 到期时间
	private List<SimpleUserInfo> joinList = new ArrayList<SimpleUserInfo>();
	private List<SimpleUserInfo> unJoinList = new ArrayList<SimpleUserInfo>();
	private TextView reserveDateTextView;
	private BitmapCache bitmapCache;
	private SharedPreferences sharedPreferences;
	private TextView join_count, unjoin_count;
	private LinearLayout joinLayout, unjoinLayout;
	private LayoutInflater inflater;
	private Resources res;
	private ImageLoader mImageLoader;
	private final String myUrl = "Bus.AppointController.ApproveAppoint.ashx";
	private CustomerDialog myDialog;
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (joinLayout != null) {
					joinLayout.removeAllViews();
				}
				String result = (String) msg.obj;
				joinList = resoloveAlljoinJson(result);
				join_count.setText("已加入(" + joinList.size() + ")");
				loadItemView(joinLayout, joinList, false);

				break;
			/**
			 * 申请加入的
			 */
			case 2:
				if (unjoinLayout != null) {
					unjoinLayout.removeAllViews();
				}
				String result1 = (String) msg.obj;
				unJoinList = resoloveAlljoinJson(result1);
				unjoin_count.setText("未加入(" + unJoinList.size() + ")");
				loadItemView(unjoinLayout, unJoinList, true);
				break;
			/**
			 * 点击批准加入以后。
			 */
			case 3:
				String result2 = (String) msg.obj;
				resoloveApproveJson(result2);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_applymangers);
		sharedPreferences = getSharedPreferences(Constans.LOGIN_PREFERENCE, Context.MODE_PRIVATE);
		user_pid = sharedPreferences.getString(Constans.LOGIN_PID, "0");
		inflater = LayoutInflater.from(this);
		res = getResources();
		bitmapCache = new BitmapCache();
		mImageLoader = new ImageLoader(Volley.newRequestQueue(this), bitmapCache);
		Intent intent = getIntent();
		issue_pid = intent.getStringExtra("issue_pid");
		ReserveDate = intent.getStringExtra("ReserveDate");
		initActionbar();
		initView();
	}

	/**
	 * 加载Actionbar
	 */
	private void initActionbar() {
		useCustomerActionBar();
		getActionBarleft().setText("报名管理");
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
	}

	/**
	 * 加载view
	 */
	private void initView() {
		join_count = (TextView) this.findViewById(R.id.join_counttxt);
		unjoin_count = (TextView) this.findViewById(R.id.unjoin_counttxt);
		reserveDateTextView = (TextView) this.findViewById(R.id.apply_time);
		reserveDateTextView.setText("请在" + ReserveDate + "前做出选择");
		joinLayout = (LinearLayout) this.findViewById(R.id.join_ll);
		unjoinLayout = (LinearLayout) this.findViewById(R.id.unjoin_ll);
		initJoinList();
		initUnJoinList();
	}

	private void initJoinList() {

		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("AppointUserState", 30 + "");
		httpPostInterface.addParams("AppointID", issue_pid);
		httpPostInterface.getData(applyUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 1;
				message.obj = result;
				mHandler.sendMessage(message);
			}
		});
	}

	private void initUnJoinList() {
		HttpPostInterface httpPostInterface = new HttpPostInterface();
		httpPostInterface.addParams("AppointUserState", 10 + "");
		httpPostInterface.addParams("AppointID", issue_pid);
		httpPostInterface.getData(applyUrl, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				Message message = Message.obtain();
				message.what = 2;
				message.obj = result;
				mHandler.sendMessage(message);
			}
		});
	}

	/**
	 * 解析申请加入邀约的用户json
	 * 
	 * @param json
	 * @return
	 */
	private List<SimpleUserInfo> resoloveAlljoinJson(String json) {
		List<SimpleUserInfo> list = new ArrayList<SimpleUserInfo>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("Entities");
			for (int i = 0; i < jsonArray.length(); i++) {
				SimpleUserInfo simpleUserInfo = new SimpleUserInfo();
				JSONObject simpleJsonObject = jsonArray.getJSONObject(i);
				simpleUserInfo.setPhotoUrl(simpleJsonObject.getString("PhotoUrl"));
				simpleUserInfo.setPid(simpleJsonObject.getInt("PID"));
				simpleUserInfo.setSignature(simpleJsonObject.getString("Signature"));
				simpleUserInfo.setNickName(simpleJsonObject.getString("NickName"));
				simpleUserInfo.setPhone(simpleJsonObject.getString("Phone"));
				simpleUserInfo.setAge(simpleJsonObject.getInt("Age"));
				simpleUserInfo.setAstro(simpleJsonObject.getString("Astro"));
				simpleUserInfo.setSexID(simpleJsonObject.getInt("SexID"));

				list.add(simpleUserInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	private void loadItemView(LinearLayout layout, List<SimpleUserInfo> datas, boolean ifShowButton) {
		ImgOnclickListener imgOnclickListener = new ImgOnclickListener(datas);
		ShowDialogListener shDialogListener = new ShowDialogListener(datas);
		for (int i = 0; i < datas.size(); i++) {
			View view = inflater.inflate(R.layout.simpleapply_item, null);
			Button accept = (Button) view.findViewById(R.id.simple_accept);
			TextView age = (TextView) view.findViewById(R.id.simple_age);
			NetworkImageView img = (NetworkImageView) view.findViewById(R.id.simple_img);
			TextView name = (TextView) view.findViewById(R.id.simple_name);
			ImageView sex = (ImageView) view.findViewById(R.id.simple_seximg);
			TextView signature = (TextView) view.findViewById(R.id.simple_signature);
			TextView astro = (TextView) view.findViewById(R.id.simple_astro);
			TextView phone = (TextView) view.findViewById(R.id.simple_phone);
			if (ifShowButton) {
				accept.setVisibility(View.VISIBLE);
				accept.setTag(i);
				accept.setOnClickListener(shDialogListener);
			}
			SimpleUserInfo simpleUserInfo = datas.get(i);
			name.setText(simpleUserInfo.getNickName());
			if (TextUtils.isEmpty(simpleUserInfo.getSignature())) {
				signature.setText("暂无个性签名");
			} else {
				signature.setText(simpleUserInfo.getSignature());
			}
			String url = simpleUserInfo.getPhotoUrl();
			final String publishID = simpleUserInfo.getPid() + "";
			astro.setText(simpleUserInfo.getAstro());
			age.setText(simpleUserInfo.getAge() + "");
			if (TextUtils.isEmpty(simpleUserInfo.getPhone())) {
				phone.setText("暂无电话信息");
			} else {
				phone.setText(simpleUserInfo.getPhone());
			}
			if (simpleUserInfo.getSexID() == 1) {
				sex.setImageResource(R.drawable.boy);
				age.setTextColor(this.getResources().getColor(R.color.lanse));
				astro.setTextColor(res.getColor(R.color.lanse));
			} else {
				sex.setImageResource(R.drawable.girl);
				age.setTextColor(res.getColor(R.color.fense));
				astro.setTextColor(res.getColor(R.color.fense));
			}
			img.setTag(i);
			img.setErrorImageResId(R.drawable.photobg);
			img.setDefaultImageResId(R.drawable.photobg);
			img.setImageUrl(Utils.processResultStr(Constans.URL + url, "_150_"), mImageLoader);
			img.setOnClickListener(imgOnclickListener);
			layout.addView(view);
			View line = new View(this);
			LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
			line.setLayoutParams(layoutParams);
			line.setBackgroundColor(res.getColor(R.color.gray2));
			layout.addView(line);

		}
	}

	class ImgOnclickListener implements OnClickListener {

		private List<SimpleUserInfo> infos;

		public ImgOnclickListener(List<SimpleUserInfo> info) {
			infos = info;
		}

		@Override
		public void onClick(View v) {
			int i = (Integer) v.getTag();
			final String publishID = infos.get(i).getPid() + "";
			Intent intent = new Intent(ApplyMangersActivity.this, PersonalDetailActivity.class);
			intent.putExtra("userId", publishID + "");
			startActivity(intent);
		}

	}

	class ItemOnclickListener implements OnClickListener {

		private SimpleUserInfo myinfo;

		public ItemOnclickListener(SimpleUserInfo info) {
			myinfo = info;
		}

		@Override
		public void onClick(View v) {
			final String publishID = myinfo.getPid() + "";
			HttpPostInterface httpPostInterface = new HttpPostInterface();
			httpPostInterface.addParams("appointID", issue_pid);
			httpPostInterface.addParams("opUserID", user_pid);
			httpPostInterface.addParams("applyUserID", publishID);
			httpPostInterface.getData(myUrl, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					Message message = Message.obtain();
					message.what = 3;
					message.obj = result;
					mHandler.sendMessage(message);
				}
			});
		}

	}

	/**
	 * 解析点击审核通过按钮后的json
	 * 
	 * @param result
	 */
	private void resoloveApproveJson(String result) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			boolean isTrue = jsonObject.getBoolean("Item1");
			String error = jsonObject.getString("Item2");
			if (myDialog != null) {
				myDialog.dismissDlg();
			}
			if (isTrue) {
				Utils.toast(ApplyMangersActivity.this, "审批完成");
				initJoinList();
				initUnJoinList();
			} else {
				Utils.toast(ApplyMangersActivity.this, error);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showDialog(final SimpleUserInfo infos) {
		myDialog = new CustomerDialog(this, R.layout.dlg_message);
		myDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				window.setGravity(Gravity.CENTER);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
				TextView title = (TextView) dlg.findViewById(R.id.dlg_pe_title);
				TextView content = (TextView) dlg.findViewById(R.id.dlg_message);
				TextView ok = (TextView) dlg.findViewById(R.id.dlg_pe_ok);
				TextView cancel = (TextView) dlg.findViewById(R.id.dlg_pe_cancel);
				title.setText("审批管理");
				ok.setText("确认");
				cancel.setText("取消");
				content.setText("确认接受" + infos.getNickName() + "参加你的活动吗?");
				ok.setOnClickListener(new ItemOnclickListener(infos));
				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						myDialog.dismissDlg();
					}
				});
			}
		});
		myDialog.showDlg();
	}

	class ShowDialogListener implements OnClickListener {

		private List<SimpleUserInfo> infos;

		public ShowDialogListener(List<SimpleUserInfo> info) {
			infos = info;
		}

		@Override
		public void onClick(View v) {
			int i = (Integer) v.getTag();
			SimpleUserInfo simpleUserInfo = infos.get(i);
			showDialog(simpleUserInfo);
		}

	}
}

package com.android.joocola.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.activity.PersonalDetailActivity;
import com.android.joocola.activity.ShwoEvaluateActivity;
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.BitmapCache;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

public class EvaluateItemAdapter extends BaseAdapter {

	private List<UserInfo> mUserInfos;
	private LayoutInflater inflater;
	private ViewHolder holder;
	private ImageLoader mImageLoader;
	private Context mContext;

	private String pjUrlString = "Bus.AppointController.ScoreAppoint.ashx";
	private String mUserPid;
	private String mAppointID;
	private Handler handler;
	private boolean isPublish;

	public EvaluateItemAdapter(List<UserInfo> userInfos, Context context, BitmapCache bitmapCache) {
		mUserInfos = userInfos;
		mContext = context;
		inflater = LayoutInflater.from(context);
		mImageLoader = new ImageLoader(Volley.newRequestQueue(context), bitmapCache);
	}

	public void setData(List<UserInfo> userInfos) {
		this.mUserInfos = userInfos;
	}

	@Override
	public int getCount() {
		return mUserInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return mUserInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_evaluate, null);
			holder.age = (TextView) convertView.findViewById(R.id.evaluate__age);
			holder.evaluate = (Button) convertView.findViewById(R.id.evaluate__pj);
			holder.name = (TextView) convertView.findViewById(R.id.evaluate_name);
			holder.sex = (ImageView) convertView.findViewById(R.id.evaluate__seximg);
			holder.signature = (TextView) convertView.findViewById(R.id.evaluate__signature);
			holder.state = (TextView) convertView.findViewById(R.id.evaluate_zt);
			holder.astro = (TextView) convertView.findViewById(R.id.evaluate__astro);
			holder.networkImageView = (NetworkImageView) convertView.findViewById(R.id.evaluate_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		UserInfo userInfo = mUserInfos.get(position);
		final String publishID = userInfo.getPID();
		String imgUrl = userInfo.getPhotoUrl();
		holder.age.setText(userInfo.getAge());
		holder.astro.setText(userInfo.getAstro());
		holder.name.setText(userInfo.getNickName());
		if (TextUtils.isEmpty(userInfo.getSignature())) {
			holder.signature.setText("此人暂时没有签名");
		} else {

			holder.signature.setText(userInfo.getSignature());
		}
		if (userInfo.getSexID().equals("1")) {
			holder.sex.setImageResource(R.drawable.boy);
			holder.age.setTextColor(mContext.getResources().getColor(R.color.lanse));
			holder.astro.setTextColor(mContext.getResources().getColor(R.color.lanse));
		} else {
			holder.sex.setImageResource(R.drawable.girl);
			holder.age.setTextColor(mContext.getResources().getColor(R.color.fense));
			holder.astro.setTextColor(mContext.getResources().getColor(R.color.fense));
		}
		holder.networkImageView.setErrorImageResId(R.drawable.photobg);
		holder.networkImageView.setDefaultImageResId(R.drawable.photobg);
		holder.networkImageView.setImageUrl(Utils.processResultStr(Constants.URL + imgUrl, "_150_"), mImageLoader);
		holder.networkImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PersonalDetailActivity.class);
				intent.putExtra("userId", publishID + "");
				mContext.startActivity(intent);

			}
		});
		boolean isMe = publishID.equals(mUserPid) ? true : false;
		if (isMe) {
			holder.state.setVisibility(View.INVISIBLE);
			holder.evaluate.setVisibility(View.INVISIBLE);
		} else {
			WriteEvaluateListener writeEvaluateListener = new WriteEvaluateListener(userInfo.getNickName(), publishID, mUserPid);
			LookEvaluateListener lookEvaluateListener;
			if (isPublish) {

				lookEvaluateListener = new LookEvaluateListener(mAppointID, publishID);
			} else {
				lookEvaluateListener = new LookEvaluateListener(mAppointID, mUserPid);
			}
			int AppointScoreStateID = userInfo.getAppointScoreStateID();
			switch (AppointScoreStateID) {
			case 0:
				holder.state.setText("尚未评价");
				holder.evaluate.setText("评价");
				holder.evaluate.setOnClickListener(writeEvaluateListener);
				break;
			case 10:
				holder.state.setText("我已评价");
				holder.evaluate.setText("查看评价");
				holder.evaluate.setOnClickListener(lookEvaluateListener);
				break;
			case 20:
				holder.state.setText("对方已评");
				holder.evaluate.setText("评价");
				holder.evaluate.setOnClickListener(writeEvaluateListener);
				break;
			case 30:
				holder.state.setText("双方已评");
				holder.evaluate.setText("查看评价");
				holder.evaluate.setOnClickListener(lookEvaluateListener);
				break;

			default:
				break;
			}
		}

		return convertView;
	}

	public String getmUserPid() {
		return mUserPid;
	}

	public void setmUserPid(String mUserPid) {
		this.mUserPid = mUserPid;
	}

	public String getmAppointID() {
		return mAppointID;
	}

	public void setmAppointID(String mAppointID) {
		this.mAppointID = mAppointID;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public boolean isPublish() {
		return isPublish;
	}

	public void setPublish(boolean isPublish) {
		this.isPublish = isPublish;
	}

	/**
	 * 查看评价时 首先读取 已有评价 判断当前用户是不是发布者 如果是 RelateUserID=当前被查看的用户的ID。 否则RelateUserID = 当前登录用户的ID.
	 * 
	 * @author bb
	 * 
	 */
	class LookEvaluateListener implements OnClickListener {

		private String AppointID;
		private String UserID;

		public LookEvaluateListener(String mAppoint, String mUserID) {
			AppointID = mAppoint;
			UserID = mUserID;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent((Activity) mContext, ShwoEvaluateActivity.class);
			intent.putExtra("AppointID", AppointID);
			intent.putExtra("RelateUserID", UserID);
			mContext.startActivity(intent);
		}

	}

	class WriteEvaluateListener implements OnClickListener {

		private String userName;// 被评价的名字
		private String userID;// 被评价的用户ID.
		private String meID;// 当前用户的id;

		public WriteEvaluateListener(String mUserName, String mUserID, String mMeId) {
			userName = mUserName;
			userID = mUserID;
			meID = mMeId;
		}

		@Override
		public void onClick(View v) {
			final CustomerDialog customerDialog = new CustomerDialog((Activity) mContext, R.layout.dlg_evaluate);
			customerDialog.setOnCustomerViewCreated(new CustomerViewInterface() {

				@Override
				public void getCustomerView(Window window, AlertDialog dlg) {
					window.setGravity(Gravity.CENTER);
					TextView title = (TextView) dlg.findViewById(R.id.dlg_ev_title);
					title.setText("评价" + userName);
					final RadioGroup radioGroup = (RadioGroup) dlg.findViewById(R.id.dlg_evaluate_rg);
					RadioButton radioButton = (RadioButton) dlg.findViewById(R.id.dlg_evaluate_haoping);
					radioButton.setChecked(true);
					final EditText editText = (EditText) dlg.findViewById(R.id.dlg_evaluate_pj);
					TextView ok = (TextView) dlg.findViewById(R.id.dlg_evaluate_ok);
					TextView cancle = (TextView) dlg.findViewById(R.id.dlg_evaluate_cancel);
					ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String Comment = editText.getText().toString();
							if (TextUtils.isEmpty(Comment)) {
								Utils.toast(mContext, "评价内容不能为空");
								return;
							}
							RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
							int ScoreID = 10;
							if (radioButton.getText().toString().equals("好评")) {
								ScoreID = 10;
							} else if (radioButton.getText().toString().equals("中评")) {
								ScoreID = 20;
							} else if (radioButton.getText().toString().equals("差评")) {
								ScoreID = 30;
							}

							HttpPostInterface httpPostInterface = new HttpPostInterface();
							httpPostInterface.addParams("AppointID", mAppointID);
							httpPostInterface.addParams("FromUserID", meID);
							httpPostInterface.addParams("ToUserID", userID);
							httpPostInterface.addParams("ScoreID", ScoreID + "");
							httpPostInterface.addParams("Comment", Comment);
							httpPostInterface.getData(pjUrlString, new HttpPostCallBack() {

								@Override
								public void httpPostResolveData(String result) {
									try {
										JSONObject jsonObject = new JSONObject(result);
										String content = jsonObject.getString("Item2");
										if (jsonObject.getBoolean("Item1")) {
											handler.sendEmptyMessage(1);
											customerDialog.dismissDlg();
										} else {
											Message message = Message.obtain();
											message.what = 2;
											message.obj = content;
											handler.sendMessage(message);
											customerDialog.dismissDlg();
										}
									} catch (JSONException e) {

										e.printStackTrace();
									}

								}
							});
						}
					});
					cancle.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							customerDialog.dismissDlg();
						}
					});

				}
			});
			customerDialog.showDlg();
		}
	}

	class ViewHolder {

		NetworkImageView networkImageView;
		TextView name, age, signature, astro;// 签名
		ImageView sex;
		Button evaluate;// 评价
		TextView state;// 状态
	}
}

package com.android.joocola.activity;

import java.io.File;
import java.util.Date;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.SingleChatAdapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.chat.SingleChat;
import com.android.joocola.chat.XMPPChat;
import com.android.joocola.entity.ChatOfflineInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.CustomerDialog;
import com.android.joocola.utils.CustomerDialog.CustomerViewInterface;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 聊天界面 该界面用到了XUtils
 * 
 * @author lixiaosong
 * 
 */
public class ChatActivity extends BaseActivity {
	// 标识位
	private static final int PICKPICTURE = 1;
	private static final int TAKEPHOTO = 2;
	/**
	 * 聊天窗口
	 */
	@ViewInject(R.id.chat_container_lv)
	private ListView lv_container;
	/**
	 * 发送消息的内容
	 */
	@ViewInject(R.id.add_content)
	private EditText et_content;
	/**
	 * 消息发送按钮
	 */
	@ViewInject(R.id.send_btn)
	private Button btn_send;
	/**
	 * 点击查看历史记录
	 */
	@ViewInject(R.id.chat_showHistory_tv)
	private TextView tv_showHistoty;

	@ViewInject(R.id.chat_add_pic_iv)
	private ImageView iv_add_pic;

	private ChatReceiver receiver;

	/**
	 * 判断该窗口是单人聊天窗口还是多人聊天窗口,默认是单人
	 */
	private boolean isSingle = true;
	/**
	 * 单人聊天时的聊天对象
	 */
	private String nickName = "bb";
	/**
	 * 单人聊天时的用户id
	 */
	private String userId = "userId";
	private SingleChatAdapter adapter;
	private Handler handler;
	/**
	 * 显示还是隐藏历史记录，true为当前显示，false为当前隐藏
	 */
	private boolean isShowHistory = false;
	/**
	 * 得到图片的临时名称（拍照时）
	 */
	private String tempName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		ViewUtils.inject(this);
		nickName = getIntent().getStringExtra("nickName");
		userId = getIntent().getStringExtra("userId");
		initActionBar();
		handler = new Handler(getMainLooper());
		if (isSingle) {
			IntentFilter filter = new IntentFilter(Constans.CHAT_ACTION);
			receiver = new ChatReceiver();
			registerReceiver(receiver, filter);
			adapter = new SingleChatAdapter(this, nickName);
			getUserImgUrl(new String[] { userId,
					JoocolaApplication.getInstance().getUserInfo().getPID() });
			adapter.updateNoReadData();
			lv_container.setAdapter(adapter);
			scrollBottom();
		}
	}

	@Override
	protected void onResume() {
		if (XMPPChat.getInstance().getConnection().isConnected()) {
			XMPPChat.getInstance().setPresence(XMPPChat.ONLINE);
		}
		super.onResume();
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarleft().setText("聊天");
		getActionBarTitle().setText(nickName);
		getActionBarRight().setText("");

	}

	public class ChatReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			adapter.updateNoReadData();
			scrollBottom();
		}
	}

	@OnClick({ R.id.send_btn, R.id.chat_showHistory_tv, R.id.chat_add_pic_iv })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_btn:
			String content = et_content.getText().toString();
			if (content == null || content.equals("")) {
				Utils.toast(this, "输入内容不能为空");
				return;
			}
			// 得到会话
			Chat chat = SingleChat.getInstance().getFriendChat(nickName, null);
			try {
				chat.sendMessage(content);
			} catch (XMPPException e) {
				e.printStackTrace();
			}
			ChatOfflineInfo info = new ChatOfflineInfo();
			info.setContent(content);
			info.setIsFrom(XMPPChat.getInstance().getConnection().getUser()
					.split("@")[0]);
			info.setIsTo(nickName);
			info.setIsRead(0);
			info.setKey(info.getIsFrom() + "-" + info.getIsTo());
			info.setUser(JoocolaApplication.getInstance().getUserInfo()
					.getUserName());
			info.setTime(Utils.formatDate(new Date(System.currentTimeMillis())));
			try {
				JoocolaApplication.getInstance().getDB().save(info);
			} catch (DbException e) {
				e.printStackTrace();
			}
			adapter.updateNoReadData();
			scrollBottom();
			et_content.setText("");
			break;
		case R.id.chat_showHistory_tv:
			if (isShowHistory == false) {
				adapter.showHistory();
				tv_showHistoty.setText(getResources().getString(
						R.string.hideHistory));

			} else {
				adapter.hideHistory();
				tv_showHistoty.setText(getResources().getString(
						R.string.showHistory));
			}
			isShowHistory = !isShowHistory;
			break;
		case R.id.chat_add_pic_iv:
			doSendImage();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		if (receiver != null)
			unregisterReceiver(receiver);
		// 将数据库中的未读信息变为已读信息
		adapter.saveHistory();
		super.onDestroy();
	}

	/**
	 * listview滑动到底部
	 */
	public void scrollBottom() {
		lv_container.post(new Runnable() {

			@Override
			public void run() {
				lv_container.setSelection(lv_container.getBottom());
			}
		});
	}

	/**
	 * 根据用户的PID查询用户的头像地址
	 */
	public void getUserImgUrl(String[] userPIDs) {
		HttpPostInterface interface1 = new HttpPostInterface();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < userPIDs.length; i++) {
			if (i == userPIDs.length - 1)
				builder.append(userPIDs[i]);
			else
				builder.append(userPIDs[i] + ",");
		}
		interface1.addParams("UserIDs", builder.toString());
		interface1.getData(Constans.USERINFOURL, new HttpPostCallBack() {

			@Override
			public void httpPostResolveData(String result) {
				if (result == null || result.equals("")) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							Utils.toast(ChatActivity.this, "获取用户头像资料失败");
						}
					});
				} else {
					JSONObject object;
					try {
						object = new JSONObject(result);
						JSONArray array = object.getJSONArray("Entities");
						for (int i = 0; i < array.length(); i++) {
							final JSONObject userObject = array
									.getJSONObject(i);
							handler.post(new Runnable() {

								@Override
								public void run() {
									try {
										adapter.addPhotos(
												userObject
														.getString("NickName")
														.toLowerCase(),
												userObject
														.getString("PhotoUrl"));
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

			}
		});
	}

	private void doSendImage() {
		final CustomerDialog cdlg = new CustomerDialog(this,
				R.layout.dlg_chat_addpic);
		cdlg.setOnCustomerViewCreated(new CustomerViewInterface() {

			@Override
			public void getCustomerView(Window window, AlertDialog dlg) {
				LinearLayout send_pic = (LinearLayout) window
						.findViewById(R.id.send_pic_ll);
				LinearLayout take_pic = (LinearLayout) window
						.findViewById(R.id.take_pic_ll);
				Button cancel = (Button) window.findViewById(R.id.dlg_cancel);
				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.send_pic_ll:
							getPhotoFromGallery();
							cdlg.dismissDlg();
							break;
						case R.id.take_pic_ll:
							getPhotoByTakePicture();
							cdlg.dismissDlg();
							break;
						case R.id.dlg_cancel:
							cdlg.dismissDlg();
							break;
						default:
							break;
						}
					}
				};
				send_pic.setOnClickListener(listener);
				take_pic.setOnClickListener(listener);
				cancel.setOnClickListener(listener);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICKPICTURE && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				String path = Utils.getRealPathFromURI(uri, ChatActivity.this);
				File file = new File(path);
				// 上传
				try {
					XMPPChat.getInstance().sendFile(nickName, file);
				} catch (XMPPException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else if (requestCode == TAKEPHOTO && resultCode == RESULT_OK) {
			File file = new File(Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DCIM).getAbsolutePath()
					+ File.separator + tempName);

			String phoneName = android.os.Build.MODEL;
			Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
			// 有些型号的手机不支持图片旋转
			if (!phoneName.equals("HUAWEI C8813D")) {
				// 这里需要对照片的角度进行校正
				bm = Utils.rotaingImageView(
						Utils.rotateImg(file.getAbsolutePath()), bm);
			}
			File resultFile = Utils.createBitmapFile(bm);
			// 上传
			try {
				XMPPChat.getInstance().sendFile(nickName, resultFile);
			} catch (XMPPException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}

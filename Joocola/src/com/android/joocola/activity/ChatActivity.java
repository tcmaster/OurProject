package com.android.joocola.activity;

import java.io.File;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.joocola.R;
import com.android.joocola.adapter.SingleChatAdapter;
import com.android.joocola.app.JoocolaApplication;
import com.android.joocola.chat.EaseMobChat;
import com.android.joocola.entity.MyChatInfo;
import com.android.joocola.utils.Constans;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
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
	@ViewInject(R.id.chat_add_pic_iv)
	ImageView iv_add_pic;
	/**
	 * 点击查看历史记录
	 */
	@ViewInject(R.id.chat_showHistory_tv)
	private TextView tv_showHistoty;
	private String tempName = "";
	/**
	 * 聊天类型,由上一个页面传入
	 */
	private ChatType chatType = ChatType.Chat;
	/**
	 * 聊天用户,由上一个页面传入(得到的值为userId)
	 */
	private String userName = "test1";
	private String TAG = "ChatActivity";
	private boolean DEBUG = true;
	/**
	 * 接收消息的广播，如果有新消息，将进行消息接收
	 */
	private MyReceive receive;
	/**
	 * 该界面的适配器
	 */
	private SingleChatAdapter adapter;
	/**
	 * 数据库
	 */
	public DbUtils db;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		ViewUtils.inject(this);
		initActionBar();
		userName = "u" + getIntent().getStringExtra("userId");
		// 多人聊天时没有传送图片功能，单人也暂时没有了。。
		iv_add_pic.setVisibility(View.GONE);
		db = JoocolaApplication.getInstance().getDB();
		adapter = new SingleChatAdapter(this, userName);
		lv_container.setAdapter(adapter);
		handler = new Handler();
		scrollBottom();
		receive = new MyReceive();
		IntentFilter filter = new IntentFilter(Constans.CHAT_ACTION);
		registerReceiver(receive, filter);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarleft().setText("聊天");
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
			if (DEBUG) {
				Log.e(TAG, userName);
				Log.e(TAG, chatType.toString());
				Log.e(TAG, content);
			}
			EaseMobChat.getInstance().sendTxtMessage(userName, chatType, content, new EMCallBack() {

				@Override
				public void onSuccess() {
					// 发送成功，发送的消息需要显示在聊天会话上
					if (DEBUG)
						Log.e(TAG, "发送消息成功 ");
					MyChatInfo info = new MyChatInfo();
					EMConversation conversation = EMChatManager.getInstance().getConversation(userName);
					EMMessage message = conversation.getLastMessage();
					info.messageId = message.getMsgId();
					info.user = userName;
					List<MyChatInfo> temp = null;
					try {
						temp = db.findAll(Selector.from(MyChatInfo.class).where("user", "=", userName));
						if (temp == null || temp.size() == 0) {
							db.save(info);
						} else {
							info = temp.get(0);
							info.messageId = message.getMsgId();
							db.update(info, "user");
						}
					} catch (DbException e) {
						e.printStackTrace();
					}
					// 这里需要更新adapter,过了测试阶段这些代码需要加上
					handler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							adapter.updateChatList();
							et_content.setText("");
							scrollBottom();
						}
					});

				}

				@Override
				public void onProgress(int arg0, String arg1) {
					// 发送中...
				}

				@Override
				public void onError(int arg0, String arg1) {
					// 发送失败，该条消息发送作废
					if (DEBUG)
						Log.e(TAG, "发送消息失败 " + arg0 + " " + arg1);
					handler.post(new Runnable() {

						@Override
						public void run() {
							et_content.setText("");
							scrollBottom();
						}
					});

				}
			});

			break;
		case R.id.chat_showHistory_tv:
			adapter.getHistory();
			break;
		case R.id.chat_add_pic_iv:
			break;

		default:
			break;
		}

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

	@Override
	protected void onDestroy() {
		if (receive != null)
			unregisterReceiver(receive);
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
				} else {
					JSONObject object;
					try {
						object = new JSONObject(result);
						JSONArray array = object.getJSONArray("Entities");
						for (int i = 0; i < array.length(); i++) {
							final int temp = i;
							final JSONObject userObject = array.getJSONObject(i);

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICKPICTURE && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				String path = Utils.getRealPathFromURI(uri, ChatActivity.this);
				File file = new File(path);
				// 上传

			}
		} else if (requestCode == TAKEPHOTO && resultCode == RESULT_OK) {
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + tempName);
			String phoneName = android.os.Build.MODEL;
			Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
			// 有些型号的手机不支持图片旋转
			if (!phoneName.equals("HUAWEI C8813D")) {
				// 这里需要对照片的角度进行校正
				bm = Utils.rotaingImageView(Utils.rotateImg(file.getAbsolutePath()), bm);
			}
			File resultFile = Utils.createBitmapFile(bm);
			// 上传
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private class MyReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			adapter.updateChatList();
			scrollBottom();
		}

	}
}

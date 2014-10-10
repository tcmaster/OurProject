package com.android.joocola.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.android.joocola.entity.UserInfo;
import com.android.joocola.utils.Constants;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.JsonUtils;
import com.android.joocola.utils.Utils;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 聊天界面 该界面用到了XUtils
 * 
 * @author lixiaosong
 * 
 */
public class ChatActivity extends BaseActivity {

	private String TAG = "ChatActivity";
	private boolean DEBUG = true;
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
	/**
	 * 聊天类型,由上一个页面传入
	 */
	private ChatType chatType = ChatType.Chat;
	/**
	 * 这次聊天是单聊/群聊
	 */
	private boolean isSingle = true;
	private ArrayList<String> users;
	/**
	 * 聊天用户昵称/房间名
	 */
	private String userName = "test1";
	/**
	 * 与该界面聊天的用户ID/房间ID(注意，是uX或aX,不是X)
	 */
	private String userId = "u0";

	/**
	 * 接收消息的广播，如果有新消息，将进行消息接收
	 */
	private MyReceive receive;
	/**
	 * 该界面单聊的适配器
	 */
	private SingleChatAdapter adapter;
	// /**
	// * 该界面群聊的适配器,暂时单聊就可以使用
	// */
	// private MultiChatAdapter adapter_m;
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
		initData();
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initActionBar() {
		useCustomerActionBar();
		getActionBarleft().setText(userName);
		getActionBarTitle().setText("");
		getActionBarRight().setText("");
	}

	/**
	 * 初始化本界面相关数据
	 * 
	 * @author: LiXiaosong
	 * @date:2014-9-25
	 */
	private void initData() {
		isSingle = getIntent().getBooleanExtra("isSingle", true);
		if (isSingle) {
			userId = "u" + getIntent().getStringExtra("userId");
			chatType = ChatType.Chat;
		} else {
			userId = getIntent().getStringExtra("userId");
			users = getIntent().getStringArrayListExtra("users");
			chatType = ChatType.GroupChat;
		}
		userName = getIntent().getStringExtra("userNickName");
		db = JoocolaApplication.getInstance().getDB();
		adapter = new SingleChatAdapter(this, userId);
		handler = new Handler();
		receive = new MyReceive();
		IntentFilter filter = new IntentFilter(Constants.CHAT_ACTION);
		registerReceiver(receive, filter);
		// 将数据库中的本次对话设置为已读
		MyChatInfo info = new MyChatInfo();
		info.isRead = true;
		try {
			db.update(info, WhereBuilder.b("user", "=", userId), "isRead");
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化本界面视图
	 * 
	 * @author: LiXiaosong
	 * @date:2014-9-25
	 */
	private void initViews() {
		initActionBar();
		if (isSingle)
			getUserImgUrl(new String[] { JoocolaApplication.getInstance().getPID(), getIntent().getStringExtra("userId") });
		else {
			if (users != null)
				getUserImgUrl(users.toArray(new String[] {}));
		}
		lv_container.setAdapter(adapter);
		scrollBottom();
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
				Log.e(TAG, userId);
				Log.e(TAG, chatType.toString());
				Log.e(TAG, content);
			}
			EaseMobChat.getInstance().sendTxtMessage(userId, chatType, content, new EMCallBack() {

				@Override
				public void onSuccess() {
					// 发送成功
					doSendMessageSuccess();
				}

				@Override
				public void onProgress(int arg0, String arg1) {
					// 发送中...
				}

				@Override
				public void onError(int arg0, String arg1) {
					// 发送失败
					doSendMessageFail();
				}
			});

			break;
		case R.id.chat_showHistory_tv:
			final int pos = adapter.getHistory();
			lv_container.post(new Runnable() {

				@Override
				public void run() {
					lv_container.setSelection(pos);
				}
			});

			break;
		case R.id.chat_add_pic_iv:
			findPic();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		if (receive != null)
			unregisterReceiver(receive);
		if (isSingle) {// 如果进行了私聊，在聊天结束时，需要告知服务器他们之间进行过通讯
			HttpPostInterface interface1 = new HttpPostInterface();
			interface1.addParams("userID1", "u" + JoocolaApplication.getInstance().getPID());
			interface1.addParams("userID2", userId);
			interface1.getData(Constants.CHAT_MARK_URL, new HttpPostCallBack() {

				@Override
				public void httpPostResolveData(String result) {
					if (result.equals("true"))
						LogUtils.v("聊天信息成功记录");

				}
			});
		}
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
		interface1.getData(Constants.USERINFOURL, new HttpPostCallBack() {

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
							final UserInfo info = new UserInfo();
							JsonUtils.getUserInfo(userObject, info);
							handler.post(new Runnable() {

								@Override
								public void run() {
									adapter.addPhotos("u" + info.getPID(), info.getPhotoUrl());
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PICKPICTURE && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				String path = Utils.getRealPathFromURI(uri, ChatActivity.this);
				File file = new File(path);
				// 发送
				sendImgToOther(file);

			}
		} else if (requestCode == TAKEPHOTO && resultCode == RESULT_OK) {
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + tempName);
			// String phoneName = android.os.Build.MODEL;
			// Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
			// // 有些型号的手机不支持图片旋转
			// // if (!phoneName.equals("HUAWEI C8813D")) {
			// // // 这里需要对照片的角度进行校正
			// // bm = Utils.rotaingImageView(Utils.rotateImg(file.getAbsolutePath()), bm);
			// // }
			// File resultFile = Utils.createBitmapFile(bm);
			// 发送
			sendImgToOther(file);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void sendImgToOther(File file) {
		EaseMobChat.getInstance().sendImgMessage(userId, chatType, file.getAbsolutePath(), new EMCallBack() {

			@Override
			public void onSuccess() {
				// 发送成功
				doSendMessageSuccess();
			}

			@Override
			public void onProgress(int arg0, String arg1) {
				// 处理中
			}

			@Override
			public void onError(int arg0, String arg1) {
				// 发送消息失败
				doSendMessageFail();
			}
		});
	}

	/**
	 * 发送消息成功的后续处理
	 * 
	 * @author: LiXiaosong
	 * @date:2014-9-25
	 */
	public void doSendMessageSuccess() {
		// 发送成功，发送的消息需要显示在聊天会话上
		if (DEBUG)
			Log.e(TAG, "发送消息成功 ");
		MyChatInfo info = new MyChatInfo();
		LogUtils.e("确定了，userId在发送时是 " + userId);
		EMConversation conversation = EMChatManager.getInstance().getConversation(userId);
		EMMessage message = conversation.getLastMessage();
		info.messageId = message.getMsgId();
		info.user = userId;
		info.isRead = true;
		info.PID = JoocolaApplication.getInstance().getPID();
		info.chatType = message.getChatType() == ChatType.GroupChat ? Constants.CHAT_TYPE_MULTI : Constants.CHAT_TYPE_SINGLE;
		List<MyChatInfo> temp = null;
		try {
			temp = db.findAll(Selector.from(MyChatInfo.class).where("user", "=", userId).and("PID", "=", info.PID));
			if (temp == null || temp.size() == 0) {
				db.save(info);
			} else {
				info = temp.get(0);
				info.messageId = message.getMsgId();
				db.update(info, WhereBuilder.b("user", "=", userId).and("PID", "=", info.PID), "messageId", "isRead");
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
		// 这里需要更新adapter,过了测试阶段这些代码需要加上
		handler.post(new Runnable() {

			@Override
			public void run() {
				adapter.updateChatList();
				et_content.setText("");
				scrollBottom();
			}
		});
	}

	public void doSendMessageFail() {
		// 发送失败，该条消息发送作废
		if (DEBUG)
			Log.e(TAG, "发送消息失败 ");
		handler.post(new Runnable() {

			@Override
			public void run() {
				Utils.toast(ChatActivity.this, "消息发送失败");
				et_content.setText("");
				scrollBottom();
			}
		});
	}

	public void findPic() {
		/**
		 * 增加图片并上传的逻辑
		 */
		AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
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
	}

	private class MyReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			adapter.updateChatList();
			scrollBottom();
		}
	}
}

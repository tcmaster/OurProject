package com.example.huanxindemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.easemob.EMCallBack;
import com.easemob.chat.EMMessage.ChatType;
import com.example.chat.EaseMobChat;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EaseMobChat.getInstance().registerAccount("test1", "123456");
		EaseMobChat.getInstance().sendTxtMessage("test2", ChatType.Chat,
				"人生不纠结", new EMCallBack() {

					@Override
					public void onSuccess() {
						Log.e("test", "发送已经成功");

					}

					@Override
					public void onProgress(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError(int arg0, String arg1) {
						// TODO Auto-generated method stub

					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package com.android.joocola;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.joocola.activity.FindPasswordActivity;
import com.android.joocola.activity.RegisterOneActivity;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;

public class MainActivity extends Activity implements OnClickListener {

	protected String url_b = "Sys.UserController.AppLogon.ashx";
	private EditText nameEdit, pswdEdit;
	private Button loginButton, registerButton;
	private TextView forget_pswd;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String result = (String) msg.obj;
				if (result.equals("0")) {
					Toast.makeText(MainActivity.this,
							getString(R.string.loginerror), Toast.LENGTH_SHORT)
							.show();
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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();

	}

	private void initView() {
		nameEdit = (EditText) this.findViewById(R.id.name_edit);
		pswdEdit = (EditText) this.findViewById(R.id.pswd_edit);
		loginButton = (Button) this.findViewById(R.id.login);
		forget_pswd = (TextView) this.findViewById(R.id.forget_pswd);
		registerButton = (Button) this.findViewById(R.id.register);
		registerButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		forget_pswd.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			Utils.hideSoftInputMode(MainActivity.this, v);
			String name = nameEdit.getText().toString();
			String pswd = pswdEdit.getText().toString();
			if (name.length() == 0) {
				Toast.makeText(MainActivity.this,
						getString(R.string.name_hint), Toast.LENGTH_SHORT)
						.show();
				break;
			}
			if (pswd.length() == 0) {
				Toast.makeText(MainActivity.this,
						getString(R.string.input_pswd), Toast.LENGTH_SHORT)
						.show();
				break;
			}
			if (Utils.judgeAccount(nameEdit.getText().toString())) {
				HttpPostInterface mHttpPostInterface = new HttpPostInterface();
				mHttpPostInterface.addParams("userName", name);
				mHttpPostInterface.addParams("pwd", pswd);
				// mHttpPostInterface.addParma("dataType", "Profession");
				mHttpPostInterface.getData(url_b, new HttpPostCallBack() {

					@Override
					public void httpPostResolveData(String result) {
						// 在这里用handler 把json 发出去 进行更新UI的操作
						Message message = Message.obtain();
						message.what = 0;
						message.obj = result;
						mHandler.sendMessage(message);
					}
				});
			} else {
				Toast.makeText(MainActivity.this,
						getString(R.string.input_right), Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.forget_pswd:
			Intent forgetIntent = new Intent(MainActivity.this,
					FindPasswordActivity.class);
			startActivity(forgetIntent);
			break;
		case R.id.register:
			Intent registerIntent = new Intent(MainActivity.this,
					RegisterOneActivity.class);
			startActivity(registerIntent);
			break;
		default:
			break;
		}
	}
}

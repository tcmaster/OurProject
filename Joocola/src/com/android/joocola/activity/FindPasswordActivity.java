package com.android.joocola.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.joocola.R;
import com.android.joocola.utils.HttpPostInterface;
import com.android.joocola.utils.HttpPostInterface.HttpPostCallBack;
import com.android.joocola.utils.Utils;

public class FindPasswordActivity extends BaseActivity implements
		OnClickListener {
	private String url = "Sys.UserController.ApplyForgetPWDVerifyCode.ashx"; // 找回密码需要的网址
	private String amend_url = "Sys.UserController.ResetPWD.ashx";// 修改新密码的网址
	private EditText edit_pm, edit_security, edit_new_pwsd;
	private Button get_security_code, find_done;
	private String security_code = "";
	private Handler findHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				String result = (String) msg.obj;
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getBoolean("Item1")) {
						// 此处应该添加1句 判断item2 是不是6位数字 如果是就赋值
						security_code = jsonObject.getString("Item2");
					} else {
						Toast.makeText(FindPasswordActivity.this,
								jsonObject.getString("Item2"),
								Toast.LENGTH_SHORT).show();
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
		this.setContentView(R.layout.activiy_findpswd);
		mActionBar.setTitle(getString(R.string.forgetpswd));
		initView();
	}

	private void initView() {
		edit_pm = (EditText) this.findViewById(R.id.edit_pm);
		edit_security = (EditText) this.findViewById(R.id.edit_security);
		edit_new_pwsd = (EditText) this.findViewById(R.id.edit_new_pwsd);
		get_security_code = (Button) this.findViewById(R.id.get_security_code);
		find_done = (Button) this.findViewById(R.id.find_done);
		find_done.setOnClickListener(this);
		get_security_code.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_security_code:
			String input = edit_pm.getText().toString();
			if (edit_pm != null && Utils.judgeAccount(input)
					&& !Utils.stringIsNullOrEmpty(input)) {
				// do请求
				Log.e("test", input);
				HttpPostInterface httpPostInterface = new HttpPostInterface();
				httpPostInterface.addParams("userName", input);
				httpPostInterface.getData(url, new HttpPostCallBack() {

					@Override
					public void httpPostResolveData(String result) {
						Log.e("result", result);
						Message message = Message.obtain();
						message.what = 0;
						message.obj = result;
						findHandler.sendMessage(message);
					}
				});
			} else {
				Toast.makeText(FindPasswordActivity.this,
						getString(R.string.input_right), Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.find_done:
			Log.e("修改密码", "点击修改密码按钮");
			String input_name = edit_pm.getText().toString();
			String input_security_code = edit_security.getText().toString();// 用户输入的验证码
			String newPswd = edit_new_pwsd.getText().toString();// 用户输入的新密码
			if (input_security_code.length() != 6) {
				Toast.makeText(FindPasswordActivity.this, "请输入正确的验证码",
						Toast.LENGTH_SHORT).show();
				break;
			}
			Log.e("点击修改密码之后的保存下来的security_code", security_code);
			Log.e("点击修改密码之后用户输入的security_code", input_security_code);
			if (input_security_code.equals(security_code)) {
				Log.e("修改密码", "开始发送修改密码的请求");
				HttpPostInterface httpPostInterface = new HttpPostInterface();
				httpPostInterface.addParams("userName", input_name);
				httpPostInterface.addParams("newPWD", newPswd);
				httpPostInterface.addParams("verifyCode", input_security_code);
				httpPostInterface.getData(amend_url, new HttpPostCallBack() {

					@Override
					public void httpPostResolveData(String result) {
						Log.e("修改密码", result);
					}
				});
			}
			break;
		default:
			break;
		}
	}
}
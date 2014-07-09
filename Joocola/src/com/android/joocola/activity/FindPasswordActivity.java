package com.android.joocola.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.joocola.R;
import com.android.joocola.utils.Utils;

public class FindPasswordActivity extends BaseActivity implements
		OnClickListener {
	private String url = "Sys.UserController.ApplyForgetPWDVerifyCode.ashx"; // 找回密码需要的网址
	private EditText edit_pm, edit_security, edit_new_pwsd;
	private Button get_security_code;

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
				Log.e("test", "请求");
			} else {
				Toast.makeText(FindPasswordActivity.this, "请输入正确的账号",
						Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}
	}

}

package com.example.joocola;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.joocola.utils.HttpPostInterface;
import com.example.joocola.utils.HttpPostInterface.HttpPostCallBack;

public class MainActivity extends Activity {

	protected String url_b = "Sys.UserController.AppLogon.ashx";
	protected String test_sqString = "Sys.BaseDataController.GetDatas.ashx";
	private EditText nameText, pwsdText;
	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nameText = (EditText) this.findViewById(R.id.name_edit);
		pwsdText = (EditText) this.findViewById(R.id.editText1);
		loginButton = (Button) this.findViewById(R.id.login);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HttpPostInterface mHttpPostInterface = new HttpPostInterface();
				mHttpPostInterface.addParma("userName", nameText.getText()
						.toString());
				mHttpPostInterface.addParma("pwd", pwsdText.getText()
						.toString());
				// mHttpPostInterface.addParma("dataType", "Profession");
				mHttpPostInterface.getData(test_sqString,
						new HttpPostCallBack() {

							@Override
							public void httpPostResolveData(String result) {
								// 在这里用handler 把json 发出去 进行更新UI的操作
							}
						});
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package com.android.joocola.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.joocola.R;

/**
 * 发布邀约完成的界面。 可以进入邀约详情界面。
 * 
 * @author bb
 * 
 */
public class IssuedinvitationCActivity extends BaseActivity {
	private int issue_pid;
	private Button check_details_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issuec);
		Intent intent = getIntent();
		issue_pid = intent.getIntExtra("issue_pid", 0);
		initActionbar();
		check_details_btn = (Button) this.findViewById(R.id.check_details_btn);
		check_details_btn.setOnClickListener(new CheckDetailsClickListener());
	}

	class CheckDetailsClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(IssuedinvitationCActivity.this,
					IssuedinvitationDetailsActivity.class);
			intent.putExtra("issue_pid", issue_pid);
			startActivity(intent);
			IssuedinvitationCActivity.this.finish();

		}

	}
	private void initActionbar() {
		useCustomerActionBar();
		getActionBarTitle().setVisibility(View.INVISIBLE);
		getActionBarRight().setVisibility(View.INVISIBLE);
		getActionBarleft().setText("发布成功");
	}
}

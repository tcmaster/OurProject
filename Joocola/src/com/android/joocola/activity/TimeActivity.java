package com.android.joocola.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.joocola.R;
import com.android.joocola.utils.Constans;

public class TimeActivity extends Activity implements OnClickListener {

	private TextView showDate = null;

	private Button pickDate = null;

	private TextView showTime = null;

	private Button pickTime = null;

	private static final int SHOW_DATAPICK = 0;
	private static final int DATE_DIALOG_ID = 1;

	private static final int SHOW_TIMEPICK = 2;

	private static final int TIME_DIALOG_ID = 3;

	private int mYear;

	private int mMonth;

	private int mDay;

	private int mHour;

	private int mMinute;
	private Button ok, cancel;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.time);

		initializeViews();

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);

		mMonth = c.get(Calendar.MONTH);

		mDay = c.get(Calendar.DAY_OF_MONTH);

		mHour = c.get(Calendar.HOUR_OF_DAY);

		mMinute = c.get(Calendar.MINUTE);

		setDateTime();

		setTimeOfDay();


	}

	/**
	 * 
	 * 初始化控件和UI视图
	 */

	private void initializeViews() {

		showDate = (TextView) findViewById(R.id.showdate);

		pickDate = (Button) findViewById(R.id.pickdate);

		showTime = (TextView) findViewById(R.id.showtime);

		pickTime = (Button) findViewById(R.id.picktime);
		ok = (Button) findViewById(R.id.time_ok);
		cancel = (Button) findViewById(R.id.time_cancel);
		pickDate.setOnClickListener(this);
		pickTime.setOnClickListener(this);
		ok.setOnClickListener(this);
		cancel.setOnClickListener(this);

	}

	/**
	 * 
	 * 设置日期
	 */
	private void setDateTime() {

		final Calendar c = Calendar.getInstance();

		mYear = c.get(Calendar.YEAR);

		mMonth = c.get(Calendar.MONTH);

		mDay = c.get(Calendar.DAY_OF_MONTH);

		updateDateDisplay();

	}

	/**
	 * 
	 * 更新日期显示
	 */

	private void updateDateDisplay() {

		showDate.setText(new StringBuilder().append(mYear).append("-")

		.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-")

				.append((mDay < 10) ? "0" + mDay : mDay));

	}

	/**
	 * 日期控件的事件
	 */

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,

		int dayOfMonth) {

			mYear = year;

			mMonth = monthOfYear;

			mDay = dayOfMonth;
			updateDateDisplay();
		}
	};

	/**
	 * 
	 * 设置时间
	 */
	private void setTimeOfDay() {

		final Calendar c = Calendar.getInstance();

		mHour = c.get(Calendar.HOUR_OF_DAY);

		mMinute = c.get(Calendar.MINUTE);

		updateTimeDisplay();

	}

	/**
	 * 
	 * 更新时间显示
	 */
	private void updateTimeDisplay() {

		showTime.setText(new StringBuilder().append(mHour).append(":")

		.append((mMinute < 10) ? "0" + mMinute : mMinute));

	}

	/**
	 * 
	 * 时间控件事件
	 */
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			mHour = hourOfDay;

			mMinute = minute;

			updateTimeDisplay();

		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {

		case DATE_DIALOG_ID:

			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,

			mDay);

		case TIME_DIALOG_ID:

			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					true);

		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {

		switch (id) {

		case DATE_DIALOG_ID:

			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);

			break;
		case TIME_DIALOG_ID:

			((TimePickerDialog) dialog).updateTime(mHour, mMinute);

			break;

		}

	}

	/**
	 * 
	 * 处理日期和时间控件的Handler
	 */
	Handler dateandtimeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TimeActivity.SHOW_DATAPICK:
				showDialog(DATE_DIALOG_ID);
				break;
			case TimeActivity.SHOW_TIMEPICK:
				showDialog(TIME_DIALOG_ID);
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pickdate:
			Message msg = new Message();

			if (pickDate.equals((Button) v)) {

				msg.what = TimeActivity.SHOW_DATAPICK;

			}

			TimeActivity.this.dateandtimeHandler.sendMessage(msg);
			break;
		case R.id.picktime:
			Message msg2 = new Message();

			if (pickTime.equals((Button) v)) {

				msg2.what = TimeActivity.SHOW_TIMEPICK;

			}

			TimeActivity.this.dateandtimeHandler.sendMessage(msg2);
			break;
		case R.id.time_ok:
			Intent intent = new Intent(TimeActivity.this,
					IssuedinvitationActivity.class);
			String date = showDate.getText().toString();
			String time = showTime.getText().toString();
			String dateTime = date + " " + time + ":00";
			intent.putExtra("time", dateTime);
			setResult(Constans.BACKTOISSUE_OK, intent);
			TimeActivity.this.finish();
			break;
		case R.id.time_cancel:
			Intent intent2 = new Intent(TimeActivity.this,
					IssuedinvitationActivity.class);
			setResult(Constans.BACKTOISSUE_CANCEL, intent2);
			TimeActivity.this.finish();
			break;
		default:
			break;
		}

	}
}